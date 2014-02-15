package com.ensibs.omeca;

import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.actions.Action;
import com.ensibs.omeca.model.actions.ConnectionAction;
import com.ensibs.omeca.model.actions.DisconnectionAction;
import com.ensibs.omeca.model.actions.GameReinitAction;
import com.ensibs.omeca.model.actions.PilesReinitAction;
import com.ensibs.omeca.model.actions.ShuffleAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.DealPopup;
import com.ensibs.omeca.utils.OmecaPopupExit;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.HandView;
import com.ensibs.omeca.view.PlayerView;
import com.ensibs.omeca.view.SlideBarCardGalleryDragListener;
import com.ensibs.omeca.view.SlidebarDragListener;
import com.ensibs.omeca.view.SlidebarPanelSlideListener;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class GameActivity extends Activity implements Observer {

	private WifiDirectManager wifiDirectManager;
	static ActionController controller;
	private OmecaApplication app;
	private static GameActivity instance;
	private OmecaHandler omecaHandler;
	private DrawerLayout mDrawerLayout;
	private ListView mListView;

	public static GameActivity getActivity() {
		return instance;
	}

	public OmecaHandler getOmecaHandler() {
		return omecaHandler;
	}

	public OmecaApplication getOmecaApplication() {
		return app;
	}

	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// Retrieve application
		app = (OmecaApplication) getApplication();

		// Handler of UIThread
		omecaHandler = new OmecaHandler(Looper.getMainLooper());

		// Creates the WifiDirectManager
		wifiDirectManager = app.getWifiDirectManager();
		wifiDirectManager.addObserver(this);
		// wifiDirectManager.setApplicationContext(this);

		// Create controller
		controller = app.getControler();
		ActionController.init();

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Launches the content view
		LayoutInflater inflater = this.getLayoutInflater();
		View gameView = inflater.inflate(R.layout.view_game, null);
		setContentView(gameView);

		// Built the board
		BoardView boardView = (BoardView) (gameView
				.findViewById(R.id.view_board));
		Board board = ActionController.board;
		board.initDrawPile(true);
		boardView.buildBoard(board);
		boardView.updatePlayers();

		SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

		// Set player skin and properties
		((PlayerView) (findViewById(R.id.playerview))).setPlayer(
				ActionController.user, true);

		// Init cards gallery
		Gallery g = (Gallery) findViewById(R.id.playerview_slider_board_cardgallery);
		g.setAdapter(new SliderbarCardGallery(this));
		g.setSelection(ActionController.user.getNumberOfCards() / 2);
		g.setOnDragListener(new SlideBarCardGalleryDragListener());

		// Number of card initialization
		TextView nbDis = ((TextView) findViewById(R.id.nbDiscardPileCards));
		TextView nbDra = ((TextView) findViewById(R.id.nbDrawPileCards));
		nbDis.setText("" + board.getDiscardPile().getNumberOfCards());
		nbDra.setText("" + board.getDrawPile().getNumberOfCards());
		ViewGroup parent = (ViewGroup) nbDis.getParent();
		parent.removeViewInLayout(nbDis);
		parent.addView(nbDis);
		parent.removeViewInLayout(nbDra);
		parent.addView(nbDra);

		// Seekbar
		HandView handView = (HandView) findViewById(R.id.handview);
		((SeekBar) findViewById(R.id.cards_zoom_seekbar))
				.setOnSeekBarChangeListener(handView.new CardsZoomSeekbarChangeListener());

		// Sort actions
		((ImageView) (findViewById(R.id.button_value_order)))
				.setOnTouchListener(handView.new OrderByValueTouchListener());
		((ImageView) (findViewById(R.id.button_total_order)))
				.setOnTouchListener(handView.new TotalOrderTouchListener());
		((ImageView) (findViewById(R.id.button_color_order)))
				.setOnTouchListener(handView.new OrderByColorTouchListener());

		// Non-draggable zone exeptions
		findViewById(R.id.hand_actions).setOnDragListener(
				new SlidebarDragListener(slide));
		findViewById(R.id.collapse).setOnDragListener(
				new SlidebarDragListener(slide));
		findViewById(R.id.expand).setOnDragListener(
				new SlidebarDragListener(slide));

		// Setting up slidebar
		slide.setDragView(findViewById(R.id.expand));
		slide.setPanelSlideListener(new SlidebarPanelSlideListener(slide));

		if (wifiDirectManager.getMod() == WifiDirectMod.CLIENT) {
			wifiDirectManager.sendEvent(new WifiDirectEventImpl(
					WifiDirectEvent.EVENT, new ConnectionAction(
							ActionController.user)));
		} else {
			ActionController.board.addPlayer(0, ActionController.user);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mListView = (ListView) findViewById(R.id.left_drawer);
		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.row_layout, R.id.text_row,
				getResources().getStringArray(R.array.drawer_list_item));
		// Setting the adapter on mDrawerList
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_layout,
				mListView, false);
		mListView.addHeaderView(header, null, false);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				selectItem(position);
			}
		});

	}

	private void selectItem(int position) {
		switch (position) {
		case 1: {
			mDrawerLayout.closeDrawers();
			DealPopup.show(this);
		}
			break;
		case 2: {
			mDrawerLayout.closeDrawers();
			ActionController.board.getDrawPile().shuffle();
			Card[] cards = new Card[ActionController.board.getDrawPile()
					.getCards().size()];
			int i = 0;
			for (Card c : ActionController.board.getDrawPile().getCards()) {
				cards[i] = c;
				i++;
			}
			omecaHandler.sendEmptyMessage(OmecaHandler.SHUFFLE);
			wifiDirectManager.sendEvent(new WifiDirectEventImpl(
					WifiDirectEvent.EVENT, new ShuffleAction(cards)));
		}
			break;
		case 3: {
			mDrawerLayout.closeDrawers();
			BoardView boardView = (BoardView) findViewById(R.id.view_board);
			boardView.bringChildToFront(boardView.getCutCardsView());
			boardView.getCutCardsView().setVisibility(View.VISIBLE);
			boardView.getCutCardsView().updateView();
		}
			break;
		case 4: {
			mDrawerLayout.closeDrawers();
			Card[] cards = new Card[ActionController.board.getDiscardPile()
					.getCards().size()];
			int i = cards.length - 1;
			for (Card c : ActionController.board.getDiscardPile().getCards()) {
				cards[i] = c;
				i--;
			}
			for (Card c : cards) {
				c.setFaceUp(false);
				ActionController.board.getDrawPile().getCards().add(0, c);
			}
			omecaHandler.sendEmptyMessage(OmecaHandler.PILES_REINIT);
			wifiDirectManager.sendEvent(new WifiDirectEventImpl(
					WifiDirectEvent.EVENT, new PilesReinitAction()));
		}
			break;
		case 5: {
			mDrawerLayout.closeDrawers();
			ActionController.user.getCards().clear();
			ActionController.board.getDiscardPile().getCards().clear();
			ActionController.board.getDrawPile().getCards().clear();
			for (Entry<Integer, Player> e : ActionController.board.getPlayers()
					.entrySet()) {
				e.getValue().getCards().clear();
			}
			ActionController.board.initDrawPile(true);
			Card[] cards = new Card[ActionController.board.getDrawPile()
					.getCards().size()];
			int i = 0;
			for (Card c : ActionController.board.getDrawPile().getCards()) {
				cards[i] = c;
				i++;
			}
			omecaHandler.sendEmptyMessage(OmecaHandler.GAME_REINIT);
			wifiDirectManager.sendEvent(new WifiDirectEventImpl(
					WifiDirectEvent.EVENT, new GameReinitAction(cards)));

		}
		default:
			break;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupExit.show(this);
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof WifiDirectEventImpl
				&& ((WifiDirectEventImpl) data).getEvent() == WifiDirectEvent.EVENT) {
			WifiDirectEventImpl event = (WifiDirectEventImpl) data;
			Action dataObject = (Action) event.getData();
			Log.i(WifiDirectProperty.TAG, dataObject.getClass()
					.getCanonicalName());
			dataObject.execute();
		}
	}

	@Override
	public void onBackPressed() {
		OmecaPopupExit.show(this);
	}

	public synchronized void finishGameActivity() {
		OmecaPopupExit.dismiss();
		this.wifiDirectManager.sendEvent(new WifiDirectEventImpl(
				WifiDirectEvent.EVENT, new DisconnectionAction(
						ActionController.user)));
		try {
			this.wait(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.wifiDirectManager.disconnect();
		finish();
	}
}
