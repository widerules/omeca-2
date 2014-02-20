package com.ensibs.omeca.wifidirect;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listener for wifi direct device found
 */
public class P2PDiscoveryReceiver extends BroadcastReceiver{
	
	/**
	 * notification center for api wifi direct
	 */
	private WifiDirectNotificationCenter notificationCenter;
	
	/**
	 * Constructor
	 * @param notificationCenter notification center for api wifi direct
	 */
	public P2PDiscoveryReceiver(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}

	/**
	 * Check for type of event receive from the api
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(WifiDirectProperty.TAG, "Framework P2P peers find");
		notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.RECEIVED_P2P_LIST));
	}	
}
