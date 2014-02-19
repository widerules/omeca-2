package com.ensibs.omeca;

import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.wifidirect.WifiDirectManager;

import android.app.Application;

/**
 * Omeca general application
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class OmecaApplication extends Application {

	private WifiDirectManager wifiDirectManager;
	private GA controller;

	/**
	 * Creates tha application
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		// Creates the WifiDirectManager
		setWifiDirectManager(new WifiDirectManager(this));

		// create controller
		setController(new GA(this));
	}

	/**
	 * Getter on controller of application
	 * 
	 * @return controller
	 */
	public GA getController() {
		return controller;
	}

	/**
	 * Setter on controller of application
	 * 
	 * @param controler
	 *            New controller
	 */
	public void setController(GA controller) {
		this.controller = controller;
	}

	/**
	 * Getter on wifi direct manager
	 * 
	 * @return wifiDirectManager
	 */
	public WifiDirectManager getWifiDirectManager() {
		return wifiDirectManager;
	}

	/**
	 * Setter on current Wifi Direct Manager
	 * 
	 * @param wifiDirectManager
	 *            The WifiDirect manager
	 */
	public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
		this.wifiDirectManager = wifiDirectManager;
	}

}
