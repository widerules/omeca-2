package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ToggleButton;

import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.utils.OmecaPopupExit;
import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class MainActivity extends Activity implements Observer {
	private final int EDIT_AVATAR = 2;
	private final String SHARED_PREFERENCES_FILE_NAME = "OMECA Profile";
	private final String SHARED_PREFERENCES_VIBRATION = "vibration";
	private final String SHARED_PREFERENCES_SOUND = "sound";
	private ToggleButton soundToggle = null;
	private ToggleButton vibrationToggle = null;
	private WakeLock wakeLock = null;
	private SharedPreferences profilPreferences;

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
		
		// Retrieve options
		retrieveOptions();
		
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
	 * Opens the popup menu after the corresponding menu button have been
	 * pressed
	 * 
	 * @param view
	 */
	public void options(View view) {
		OmecaPopupExit.show(this);
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

	/**
	 * Retrieves options for music and vibration preferences
	 */
	private void retrieveOptions() {		
		OnClickListener list = new OnClickListener() {
			@Override
			public void onClick(View v) {				
				SharedPreferences.Editor editor = profilPreferences.edit();
				editor.putBoolean(SHARED_PREFERENCES_SOUND, soundToggle.isChecked());
				editor.putBoolean(SHARED_PREFERENCES_VIBRATION, vibrationToggle.isChecked());
				editor.commit();
			}
		};
		
		profilPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		soundToggle = (ToggleButton) this.findViewById(R.id.homescreen_options_sound_toggle);
		soundToggle.setOnClickListener(list);
		soundToggle.setChecked(this.profilPreferences.getBoolean(SHARED_PREFERENCES_SOUND, false));
		vibrationToggle = (ToggleButton) this.findViewById(R.id.homescreen_options_vibration_toggle);		
		vibrationToggle.setOnClickListener(list);		
		vibrationToggle.setChecked(this.profilPreferences.getBoolean(SHARED_PREFERENCES_VIBRATION, false));
		
	}	

}
