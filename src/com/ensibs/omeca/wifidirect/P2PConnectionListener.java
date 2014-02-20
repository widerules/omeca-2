package com.ensibs.omeca.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Listener for connection or deconnection of wifi direct network
 */
public class P2PConnectionListener extends BroadcastReceiver{
	
	/**
	 * notification center for api wifi direct
	 */
	private WifiDirectNotificationCenter notificationCenter;
	
	/**
	 * Constructor
	 * @param notificationCenter notification center for api wifi direct
	 */
	public P2PConnectionListener(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}

	/**
	 * Check for type of event receive from the api
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(WifiDirectProperty.TAG, "ConnectionListener");
		NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
		if (networkInfo.isConnected()){
			Log.i(WifiDirectProperty.TAG, "ConnectionListener connected");

			this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.CONNECTED));
		}
		else{
			this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.DISCONNECTED));
		}
	}
}