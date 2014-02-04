package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
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
import com.ensibs.omeca.model.actions.DisconnectionAction;
import com.ensibs.omeca.model.entities.Board;
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
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class GameActivity extends Activity implements Observer {

	WifiDirectManager wifiDirectManager;
	static ActionController controller;
	AlertDialog popupMenu;
	OmecaApplication app;
	private static GameActivity instance;

	public static GameActivity getActivity() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// Retrieve application
		app = (OmecaApplication) getApplication();

		// Create controller
		controller = app.getControler();

		// Creates the WifiDirectManager
		 wifiDirectManager = app.getWifiDirectManager();
		 wifiDirectManager.addObserver(this);
		 //wifiDirectManager.setApplicationContext(this);

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
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupExit.show(this);
		return false;
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof WifiDirectEventImpl && ((WifiDirectEventImpl)data).getEvent() == WifiDirectEvent.EVENT) {
			Looper.prepare();
			WifiDirectEventImpl event = (WifiDirectEventImpl)data;
			Log.i(WifiDirectProperty.TAG,"Event");
			if(event.getData() instanceof DisconnectionAction){
				Log.i(WifiDirectProperty.TAG,"Disconnection");
			}
		}
	}

	@Override
	public void onBackPressed() {
		OmecaPopupExit.show(this);
	}

	public static void showDealPopup() {
		DealPopup.show(this);
	}
	
	public void finishGameActivity(){
		OmecaPopupExit.dismiss();
		//Log.i(WifiDirectProperty.TAG, "End");
		this.wifiDirectManager.sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT, new DisconnectionAction(ActionController.user.getId())));
		this.wifiDirectManager.disconnect();
		finish();
	}
}
