package com.ensibs.omeca.wifidirect;

import java.util.Observable;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * Class used by all listener to notify of a wifi direct event 
 */
public class WifiDirectNotificationCenter extends Observable{
	
	/**
	 * Notify for an event
	 * @param event the wifi direct impl event
	 */
	public void notifyManager(WifiDirectEventImpl event){
		setChanged();
		notifyObservers(event);
	}
}
