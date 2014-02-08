package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.actions.AknowlegmentConnectionAction;
import com.ensibs.omeca.model.actions.ConnectionAction;
import com.ensibs.omeca.model.actions.DisconnectionAction;
import com.ensibs.omeca.model.actions.ReturnCardAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
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

	WifiDirectManager wifiDirectManager;
	static ActionController controller;
	AlertDialog popupMenu;
	OmecaApplication app;
	private static GameActivity instance;
	private OmecaHandler omecaHandler;

	public static GameActivity getActivity() {
		return instance;
	}

	public OmecaHandler getOmecaHandler() {
		return omecaHandler;
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
		
		//Handler of UIThread
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
		
		if(wifiDirectManager.getMod() == WifiDirectMod.CLIENT){
			wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new ConnectionAction(ActionController.user)));
			Log.w("Client", "Client");
		}
		else{
			ActionController.board.addPlayer(0, ActionController.user);
			Log.w("Host", "Host");
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupExit.show(this);
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		Log.i(WifiDirectProperty.TAG,"Event update");
		if (data instanceof WifiDirectEventImpl && ((WifiDirectEventImpl)data).getEvent() == WifiDirectEvent.EVENT) {
			WifiDirectEventImpl event = (WifiDirectEventImpl)data;
			Log.i(WifiDirectProperty.TAG,"Event game");
			Object dataObject = event.getData();
			if(dataObject instanceof DisconnectionAction){
				Log.i(WifiDirectProperty.TAG,"Disconnection");
				omecaHandler.sendEmptyMessage(OmecaHandler.DECONNEXION);
			}
			else if(dataObject instanceof ConnectionAction){
				if(wifiDirectManager.getMod() == WifiDirectMod.HOST){
					Player p = ((ConnectionAction) dataObject).getPlayer();
					p.setId(ActionController.board.getPlayers().size());
					ActionController.board.addPlayerToTheFirstEmptyPlace(p);
					wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new AknowlegmentConnectionAction(p, ActionController.board)));
					omecaHandler.sendEmptyMessage(OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
				}
			}
			else if(dataObject instanceof AknowlegmentConnectionAction){
				Player p = ((AknowlegmentConnectionAction) dataObject).getPlayer();
				if(p.getMacAddress().equals(ActionController.user.getMacAddress())){
					ActionController.user = p;
				}
				ActionController.board = ((AknowlegmentConnectionAction) dataObject).getBoard();
				omecaHandler.sendEmptyMessage(OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
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
			}
		}
	}

	@Override
	public void onBackPressed() {
		OmecaPopupExit.show(this);
	}
	
	public synchronized void finishGameActivity(){
		OmecaPopupExit.dismiss();
		this.wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new DisconnectionAction(ActionController.user.getId())));
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
