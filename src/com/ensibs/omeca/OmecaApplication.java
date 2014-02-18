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

	/**
	 * Getter on controller of application
	 * @return controller
	 */
	public GA getControler() {
		return controler;
	}

	/**
	 * Setter on controller of application
	 * @param controler New controller
	 */
	public void setController(GA controler) {
		this.controler = controler;
	}

	/**
	 * Getter on wifi direct manager
	 * @return Curent 
	 */
	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	/**
	 * Setter on current Wifi Direct Manager
	 * @param wifiDirectManager
	 */
	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}
	
}
