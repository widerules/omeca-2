package com.ensibs.omeca;

import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.wifidirect.WifiDirectManager;

import android.app.Application;

public class OmecaApplication extends Application {

	private WifiDirectManager wifiDirectManager;
	private GA controler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Creates the WifiDirectManager
		setWifiDirectManager(new WifiDirectManager(this));
				
		//create controller 
		setController(new GA(this));
	}

	public GA getControler() {
		return controler;
	}

	public void setController(GA controler) {
		this.controler = controler;
	}

	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}
	
}
