package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.actions.AknowlegmentConnectionAction;
import com.ensibs.omeca.model.actions.AutomaticDistributionAction;
import com.ensibs.omeca.model.actions.ConnectionAction;
import com.ensibs.omeca.model.actions.DisconnectionAction;
import com.ensibs.omeca.model.actions.MoveCardAction;
import com.ensibs.omeca.model.actions.ReturnCardAction;
import com.ensibs.omeca.model.actions.ShuffleAction;
import com.ensibs.omeca.model.actions.SwitchPlayersAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.DealPopup;
import com.ensibs.omeca.utils.OmecaPopupExit;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.CardView;
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
		//wifiDirectManager.setApplicationContext(this);
		
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
		mListView = (ListView)findViewById(R.id.left_drawer);
		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
				 R.layout.row_layout,R.id.text_row, getResources().getStringArray(R.array.drawer_list_item));
		// Setting the adapter on mDrawerList
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_layout, mListView, false);
		mListView.addHeaderView(header, null, false);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				selectItem(position);
			}
		});

		
	}
	
	private void selectItem(int position) {
	    switch(position) {
	    case 1:
	    		mDrawerLayout.closeDrawers();
	            DealPopup.show(this);
	            break;
	    case 2:
	    	mDrawerLayout.closeDrawers();
	    	ActionController.board.getDrawPile().shuffle();
	    	Card[] cards = new Card[ActionController.board.getDrawPile().getCards().size()];
	    	int i = 0;
	    	for(Card c : ActionController.board.getDrawPile().getCards()){
	    		cards[i] = c;
	    		i++;
	    	}
	    	omecaHandler.sendEmptyMessage(OmecaHandler.SHUFFLE);
	    	wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new ShuffleAction(cards)));
	        break;
	    case 3:
	    	Log.i(WifiDirectProperty.TAG, "Click 2");
	    	mDrawerLayout.closeDrawers();
	           break;
	    case 4:
	    	Log.i(WifiDirectProperty.TAG, "Click 3");
	    	mDrawerLayout.closeDrawers();
	           break;
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
		Log.i(WifiDirectProperty.TAG, "Event update");
		if (data instanceof WifiDirectEventImpl
				&& ((WifiDirectEventImpl) data).getEvent() == WifiDirectEvent.EVENT) {
			WifiDirectEventImpl event = (WifiDirectEventImpl) data;
			Log.i(WifiDirectProperty.TAG, "Event game");
			Object dataObject = event.getData();
			if (dataObject instanceof DisconnectionAction) {
				Log.i(WifiDirectProperty.TAG, "Disconnection");
				Player p = ((DisconnectionAction) dataObject).getPlayer();
				Log.i("Player", p.getId() + "");
				for (Card c : p.getCards()) {
					ActionController.board.getDiscardPile().addCard(c);
				}
				ActionController.board.removePlayer(ActionController.board
						.getPlace(p));
				omecaHandler.sendEmptyMessage(OmecaHandler.DECONNEXION);
			} else if (dataObject instanceof ConnectionAction) {
				if (wifiDirectManager.getMod() == WifiDirectMod.HOST) {
					Player p = ((ConnectionAction) dataObject).getPlayer();
					p.setId(ActionController.board.getPlayers().size());
					ActionController.board.addPlayerToTheFirstEmptyPlace(p);
					wifiDirectManager.sendEvent(new WifiDirectEventImpl(
							WifiDirectEvent.EVENT,
							new AknowlegmentConnectionAction(p,
									ActionController.board)));
					omecaHandler
							.sendEmptyMessage(OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
				}
			} else if (dataObject instanceof AknowlegmentConnectionAction) {
				Player p = ((AknowlegmentConnectionAction) dataObject)
						.getPlayer();
				if (p.getMacAddress().equals(
						ActionController.user.getMacAddress())) {
					ActionController.user = p;
				}
				ActionController.board = ((AknowlegmentConnectionAction) dataObject)
						.getBoard();
				omecaHandler
						.sendEmptyMessage(OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
			} else if (dataObject instanceof SwitchPlayersAction) {
				Player p1 = ((SwitchPlayersAction) dataObject).getP1();
				Player p2 = ((SwitchPlayersAction) dataObject).getP2();
				if (p1 != null){
					ActionController.board.switchPlayers(
							p1, p2);
				}
				else {
					ActionController.board.movePlayerTo(p2, ((SwitchPlayersAction) dataObject).getPosition());
				}
				
				omecaHandler
				.sendEmptyMessage(OmecaHandler.SWITCH_PLAYERS_ACTION);
				
				
			}
			else if(dataObject instanceof ReturnCardAction){
				Log.i(WifiDirectProperty.TAG, "Carte retourner");
				ReturnCardAction retCardAction = (ReturnCardAction) dataObject;
				Message msg = omecaHandler.obtainMessage();
				msg.what = OmecaHandler.RETURN_CARD;
				Bundle dataMessage = new Bundle();
				dataMessage.putString("Source", retCardAction.getSrc());
				dataMessage.putInt("Value", retCardAction.getCard().getValue());
				dataMessage.putString("Color", retCardAction.getCard().getColor());
				msg.setData(dataMessage);
				omecaHandler.sendMessage(msg);
			}else if(dataObject instanceof MoveCardAction){
				Log.i(WifiDirectProperty.TAG, "Carte deplace");
				MoveCardAction moveCardAction = (MoveCardAction) dataObject;
				Message msg = omecaHandler.obtainMessage();
				msg.what = OmecaHandler.MOVE_CARD;
				Bundle dataMessage = new Bundle();
				dataMessage.putString("Source", moveCardAction.getSrc());
				if(moveCardAction.getIdSource() != -1)
					dataMessage.putInt("IDSource", moveCardAction.getIdSource());
				dataMessage.putString("Target", moveCardAction.getTarget());
				if(moveCardAction.getIdTarget() != -1)
					dataMessage.putInt("IDTarget", moveCardAction.getIdTarget());
				dataMessage.putInt("Value", moveCardAction.getCard().getValue());
				dataMessage.putString("Color", moveCardAction.getCard().getColor());
				if(moveCardAction.getPourcentageX() != -1)
					dataMessage.putInt("PX", moveCardAction.getPourcentageX());
				if(moveCardAction.getPourcentageY() != -1)
					dataMessage.putInt("PY", moveCardAction.getPourcentageY());
				dataMessage.putBoolean("Face", moveCardAction.getCard().isFaceUp());
				msg.setData(dataMessage);
				omecaHandler.sendMessage(msg);
			}
			else if(dataObject instanceof AutomaticDistributionAction){
				AutomaticDistributionAction autoDistrib = (AutomaticDistributionAction) dataObject;
				Message msg = omecaHandler.obtainMessage();
				msg.what = OmecaHandler.AUTOMATIC_DRAW_ACTION;
				Bundle dataMessage = new Bundle();
				dataMessage.putInt("From", autoDistrib.getPlace());
				dataMessage.putInt("Number", autoDistrib.getDealNumber());
				msg.setData(dataMessage);
				omecaHandler.sendMessage(msg);
			}
			else if (dataObject instanceof ShuffleAction){
				ShuffleAction shuffleAction = (ShuffleAction) dataObject;
				DrawPile nDrawPile = new DrawPile();
				for(Card c : shuffleAction.getCards()){
					nDrawPile.addCard(c);
				}
				ActionController.board.setDrawPile(nDrawPile);
				omecaHandler.sendEmptyMessage(OmecaHandler.SHUFFLE);
			}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.wifiDirectManager.disconnect();
		finish();
	}
}
