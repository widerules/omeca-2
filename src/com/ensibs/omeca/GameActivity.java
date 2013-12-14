package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.OmecaPopupMenu;
import com.ensibs.omeca.utils.SlidingUpPanelLayout;
import com.ensibs.omeca.utils.SlidingUpPanelLayout.PanelSlideListener;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.SlidebarDragListener;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.ConnectionWifiDirectEvent;

public class GameActivity extends Activity implements Observer {
	WifiDirectManager wifiDirectManager;
	ControllerView controler;
	AlertDialog popupMenu;
	OmecaApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retrieve application 
		app = (OmecaApplication) getApplication();

		// Create controller 
		controler = app.getControler();

		// Creates the WifiDirectManager
		wifiDirectManager = app.getWifiDirectManager();
		wifiDirectManager.addObserver(this);
		wifiDirectManager.setRole(true);
		wifiDirectManager.discoverPeers();

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Card.loadConfig(getApplicationContext().getResources().openRawResource(R.raw.config));
		Log.w("GameActivity", Card.getCardsConfig());

		// Launches the stuff
		LayoutInflater inflater = this.getLayoutInflater();
		View gameView = inflater.inflate(R.layout.view_game, null);
		setContentView(gameView);
		BoardView boardView = (BoardView)(gameView.findViewById(R.id.view_board));
		Board board = new Board();
		board.initDrawPile(true);
		boardView.builtBoard(board);

		SlidingUpPanelLayout slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

		slide.setPanelSlideListener(new PanelSlideListener() {

			private boolean isExpanded = false;
			private int height = -1;

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            	if(height == -1){
            		if(findViewById(R.id.view_slidebar) != null){
            			height = findViewById(R.id.view_slidebar).getHeight();
            		}
            	}
            }

            @Override
            public void onPanelExpanded(View panel) {
            	//Log.i("OMECA", "Hand");
            	if(!isExpanded){
	            	LinearLayout linear = (LinearLayout) findViewById(R.id.linear_slidinguppanel);
	            	linear.removeView(findViewById(R.id.view_slidebar));
	            	linear.removeView(findViewById(R.id.linear_slidebar_board));
	            	linear.addView(View.inflate(panel.getContext(), R.layout.view_slidebar_hand, null),0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
	            			height));
	            	isExpanded = true;
            	}
            }

            @Override
            public void onPanelCollapsed(View panel) {
            	//Log.i("OMECA", "Board");
            	if(isExpanded){
	            	LinearLayout linear = (LinearLayout) findViewById(R.id.linear_slidinguppanel);
	            	linear.removeView(findViewById(R.id.view_slidebar));
	            	linear.removeView(findViewById(R.id.linear_slidebar_hand));
	            	linear.addView(View.inflate(panel.getContext(), R.layout.view_slidebar_board, null),0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
	            			height));
	            	isExpanded = false;
            	}
            }

            @Override
            public void onPanelAnchored(View panel) {

            }
        });

		gameView.findViewById(R.id.view_slidebar).setOnDragListener(new SlidebarDragListener());
	}


	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof ConnectionWifiDirectEvent) {
			if (!wifiDirectManager.isHost()) {
				setContentView(R.layout.view_game);
			}
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			OmecaPopupMenu.show(this);
		}

		return super.onKeyDown(keyCode, event);
	}


	public void showDealPopup(){
		if(controler.board.getDrawPile().getNumberOfCards()> 0){
			final DealView dv = new DealView( controler.board.getDrawPile().getNumberOfCards(),
					controler.board.getPlayers().size(), this.getApplicationContext());
			AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
			builder.setTitle("Distribuer les cartes");
			builder.setView(dv);
			final AlertDialog alert = builder.create();
			alert.show();
			dv.buttonSave.setOnClickListener( new OnClickListener() {
				public void onClick(View v) {
					controler.dealCard(dv.getDealNumber());
					alert.dismiss();
				}
			});
			dv.buttonCancle.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					alert.cancel();
				}
			});
		}else Toast.makeText(getApplicationContext(), "La pile est vide",
				   Toast.LENGTH_LONG).show();
		
	}


	/**
	 * Opens the popup menu after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void options(View view) {
		System.out.println("Options !!!");
		OmecaPopupMenu.show(this);
	}


	/**
	 * Exits the current game after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void disconnect(View view) {
		System.out.println("Disconnecting !!!");
		this.finish();
	}


	/**
	 * Exits properly the program after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void exit(View view) {
		System.out.println("Exit !!!");
		wifiDirectManager.removeWifiDirect();
		this.finish();
	}

}
