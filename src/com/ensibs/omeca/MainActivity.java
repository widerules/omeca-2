package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.OmecaPopupMenu;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;

public class MainActivity extends Activity implements Observer {
	private static final int EDIT_AVATAR = 2;

	WifiDirectManager wifiDirectManager;
	ActionController controler;
	AlertDialog popupMenu;
	OmecaApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Retrieve application
		app = (OmecaApplication) getApplication();

		// Create controler
		controler = app.getControler();

		// Creates the WifiDirectManager
		wifiDirectManager = app.getWifiDirectManager();
		wifiDirectManager.addObserver(this);
		Card.loadConfig(getApplicationContext().getResources().openRawResource(
				R.raw.config));

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Launches the stuff
		setContentView(R.layout.view_homescreen);

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupMenu.show(this);
	    return false;
	}

	@Override
	public void onBackPressed() {
		OmecaPopupMenu.show(this);

	}

	/**
	 * Hosts a game after the corresponding menu button have been pressed
	 * 
	 * @param view
	 */
	public void host(View view) {
		System.out.println("Host !!!");
		Toast.makeText(this, "Hosting...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setMode(WifiDirectMod.HOST);
		this.wifiDirectManager.startVisible();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);

	}

	/**
	 * Joins a game after the corresponding menu button have been pressed
	 * 
	 * @param view
	 */
	public void join(View view) {
		System.out.println("Join !!!");
		Toast.makeText(this, "Joining...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setMode(WifiDirectMod.CLIENT);
		this.wifiDirectManager.startVisible();
		this.wifiDirectManager.startDiscoverPeers();
	}

	/**
	 * Opens the popup menu after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void options(View view) {
		System.out.println("Options !!!");
		OmecaPopupMenu.show(this);
	}

	/**
	 * Jumps to the avatar creation/modification after the corresponding menu
	 * button have been pressed
	 * 
	 * @param view
	 */
	public void avatar(View view) {
		Intent editProfilActivityIntent = new Intent(this, AvatarActivity.class);
		startActivityForResult(editProfilActivityIntent, EDIT_AVATAR);
		OmecaPopupMenu.dismiss();
	}

	/**
	 * Exits properly the program after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void exit(View view) {
		OmecaPopupMenu.dismiss();
		System.out.println("Exit !!!");
		// wifiDirectManager.removeWifiDirect();
		this.finish();
	}

	@Override
	public void update(Observable arg0, Object arg1) {

	}

}
