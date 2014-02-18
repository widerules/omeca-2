package com.ensibs.omeca.wifidirect.exchange;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * Interface of exchange thread
 */
public abstract class WifiDirectIExchange {
	
	//Notification center
	protected WifiDirectNotificationCenter wifiDirectNotificationCenter;
	
	/**
	 * Default constructor
	 * @param wifiDirectNotificationCenter notification center fo wifi direct
	 */
	public WifiDirectIExchange(WifiDirectNotificationCenter wifiDirectNotificationCenter){
		this.wifiDirectNotificationCenter = wifiDirectNotificationCenter;
	}
		
	/**
	 * Stop thread
	 */
	public abstract void startExchange();
	
	/**
	 * Start thread
	 */
	public abstract void stopExchange();
	
	/**
	 * Send event to clients
	 * @param event the event
	 */
	public abstract void sendEvent(WifiDirectEventImpl event);
	
	/**
	 * Notify center when a event is received
	 * @param event the received event
	 */
	public void receivedEvent(WifiDirectEventImpl event){
		wifiDirectNotificationCenter.notifyManager(event);
	}	
}