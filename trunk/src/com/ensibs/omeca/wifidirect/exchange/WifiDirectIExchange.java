package com.ensibs.omeca.wifidirect.exchange;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * 
 *
 */
public abstract class WifiDirectIExchange {
	
	protected WifiDirectNotificationCenter wifiDirectNotificationCenter;
	
	public WifiDirectIExchange(WifiDirectNotificationCenter wifiDirectNotificationCenter){
		this.wifiDirectNotificationCenter = wifiDirectNotificationCenter;
	}
		
	public abstract void startExchange();
	
	public abstract void stopExchange();
		
	public abstract void sendEvent(WifiDirectEventImpl event);
	
	public void receivedEvent(WifiDirectEventImpl event){
		wifiDirectNotificationCenter.notifyManager(event);
	}	
}