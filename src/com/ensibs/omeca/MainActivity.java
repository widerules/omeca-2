package com.ensibs.omeca;

import java.util.Observable;
import java.util.Observer;

import model.entities.Card;

import com.ensibs.omeca.wifidirect.WifiDirectManager;
import com.ensibs.omeca.wifidirect.event.ConnectionWifiDirectEvent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements Observer {

	WifiDirectManager wifiDirectManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.action_settings:
				wifiDirectManager.removeWifiDirect();
				this.finish();
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void host(View view) {
		System.out.println("Host !!!");
		Toast.makeText(this, "Hosting...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setRole(true);
		this.wifiDirectManager.discoverPeers();
		setContentView(R.layout.view_game);
	}

	public void join(View view) {
		System.out.println("Join !!!");
		Toast.makeText(this, "Joining...", Toast.LENGTH_SHORT).show();
		this.wifiDirectManager.setRole(false);
		this.wifiDirectManager.discoverPeers();
	}

	public void options(View view) {
		System.out.println("Options !!!");
		Toast.makeText(this, "Options", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof ConnectionWifiDirectEvent) {
			if (!wifiDirectManager.isHost()) {
				setContentView(R.layout.view_game);
			}
		}
	}
}
