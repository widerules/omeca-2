package com.ensibs.omeca;

import com.ensibs.omeca.wifidirect.WifiDirectManager;

import android.app.Application;

public class OmecaApplication extends Application {

	private WifiDirectManager wifiDirectManager;
	private ControlerView controler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//create controler 
		setControler(new ControlerView(this));

		// Creates the WifiDirectManager
		setWifiDirectManager(new WifiDirectManager(this));
		
	}

	public ControlerView getControler() {
		return controler;
	}

	public void setControler(ControlerView controler) {
		this.controler = controler;
	}

	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}
	
}
