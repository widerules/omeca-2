package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.OmecaPopupMenu;
import com.ensibs.omeca.view.BoardDragListener;
import com.ensibs.omeca.view.DiscardPileView;
import com.ensibs.omeca.view.DrawPileView;
import com.ensibs.omeca.view.SlidebarDragListener;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.ConnectionWifiDirectEvent;

public class MainActivity extends Activity implements Observer {
	private static final int EDIT_AVATAR = 2;

	WifiDirectManager wifiDirectManager;
	ControlerView controler;
	AlertDialog popupMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//create controler 
		controler = new ControlerView(this);

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Creates the WifiDirectManager
		wifiDirectManager = new WifiDirectManager(this);
		wifiDirectManager.addObserver(this);
		Card.loadConfig(getApplicationContext().getResources().openRawResource(R.raw.config));
		Log.w("MainActivity", Card.getCardsConfig());

		// Launches the stuff
		setContentView(R.layout.view_homescreen);
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
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
	 * Hosts a game after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void host(View view) {
		System.out.println("Host !!!");
		Toast.makeText(this, "Hosting...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setRole(true);
		this.wifiDirectManager.discoverPeers();
		LayoutInflater inflater = this.getLayoutInflater();
		View gameView = inflater.inflate(R.layout.view_game, null);
		setContentView(gameView);
		RelativeLayout boardView = (RelativeLayout)(gameView.findViewById(R.id.view_board));
		Board board = new Board();
		board.initDrawPile(true);
		
		DrawPileView drawPileView = new DrawPileView(this, board.getDrawPile());
		DiscardPileView discardPileView = new DiscardPileView(this);
		
		boardView.setOnDragListener(new BoardDragListener());
		gameView.findViewById(R.id.view_slidebar).setOnDragListener(new SlidebarDragListener());
		boardView.addView(drawPileView);
		boardView.addView(discardPileView);
	}

	/**
	 * Joins a game after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void join(View view) {
		System.out.println("Join !!!");
		Toast.makeText(this, "Joining...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setRole(false);
		this.wifiDirectManager.discoverPeers();
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
	 * Jumps to the avatar creation/modification after
	 * the corresponding menu button have been pressed
	 * @param view
	 */
	public void avatar(View view) {
		Intent editProfilActivityIntent = new Intent(this, AvatarActivity.class);
    	startActivityForResult(editProfilActivityIntent, EDIT_AVATAR);
		OmecaPopupMenu.dismiss();
	}

	/**
	 * Exits the current game after the corresponding
	 * menu button have been pressed
	 * @param view
	 */
	public void disconnect(View view) {
		System.out.println("Disconnecting !!!");
		setContentView(R.layout.view_homescreen);
		OmecaPopupMenu.dismiss();
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
