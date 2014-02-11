package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.OmecaPopupExit;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;

public class MainActivity extends Activity implements Observer {
	public static final String SHARED_PREFERENCES_VIBRATION = "vibration";
	public static final String SHARED_PREFERENCES_SOUND = "sound";
	private final int EDIT_AVATAR = 2;
	private WakeLock wakeLock = null;

	WifiDirectManager wifiDirectManager;
	AlertDialog popupMenu;
	OmecaApplication app;
	private static MainActivity instance;

	public static MainActivity getActivity() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		// Retrieve application
		app = (OmecaApplication) getApplication();
		// Creates the WifiDirectManager
		wifiDirectManager = app.getWifiDirectManager();
		WifiDirectManager.setContext(this);
		wifiDirectManager.addObserver(this);
		Card.loadConfig(getApplicationContext().getResources().openRawResource(
				R.raw.config));
		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// Launches the stuff
		setContentView(R.layout.view_homescreen);
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "WakeLock");
        wakeLock.acquire();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		OmecaPopupExit.show(this);
	    return false;
	}

	@Override
	public void onBackPressed() {
		OmecaPopupExit.show(this);
	}

	/**
	 * Hosts a game after the corresponding menu button have been pressed
	 * 
	 * @param view
	 */
	public void host(View view) {
		System.out.println("Host !!!");
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
		this.wifiDirectManager.setMode(WifiDirectMod.CLIENT);
		this.wifiDirectManager.startVisible();
		this.wifiDirectManager.startDiscoverPeers();
	}

	/**
	 * Jumps to the avatar creation/modification after the corresponding menu
	 * button have been pressed
	 * 
	 * @param view
	 */
	public void options(View view) {
		Intent editProfilActivityIntent = new Intent(this, AvatarActivity.class);
		startActivityForResult(editProfilActivityIntent, EDIT_AVATAR);
	}

	/**
	 * Opens the popup menu after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void help(View view) {
		Toast.makeText(this, "Aide", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void update(Observable source, Object data) {
		if (data instanceof WifiDirectEventImpl && ((WifiDirectEventImpl)data).getEvent() == WifiDirectEvent.CONNECTED) {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * Exits properly the program after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void finishMainActivity(){
		OmecaPopupExit.dismiss();
		wifiDirectManager.stopP2P();
		wakeLock.release();
		this.finish();
		System.exit(0);
	}

}
