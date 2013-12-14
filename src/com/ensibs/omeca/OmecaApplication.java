package com.ensibs.omeca;

import com.ensibs.omeca.wifidirect.WifiDirectManager;

import android.app.Application;

public class OmecaApplication extends Application {

	private WifiDirectManager wifiDirectManager;
	private ControllerView controler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//create controller 
		setController(new ControllerView(this));

		// Creates the WifiDirectManager
		setWifiDirectManager(new WifiDirectManager(this));
		
	}

	public ControllerView getControler() {
		return controler;
	}

	public void setController(ControllerView controler) {
		this.controler = controler;
	}

	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}
	
}
