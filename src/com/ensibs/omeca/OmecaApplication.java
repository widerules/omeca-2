package com.ensibs.omeca;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.wifidirect.WifiDirectManager;

import android.app.Application;

public class OmecaApplication extends Application {

	private WifiDirectManager wifiDirectManager;
	private ActionController controler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//create controller 
		setController(new ActionController(this));

		// Creates the WifiDirectManager
		setWifiDirectManager(new WifiDirectManager(this));
		
	}

	public ActionController getControler() {
		return controler;
	}

	public void setController(ActionController controler) {
		this.controler = controler;
	}

	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}
	
}
