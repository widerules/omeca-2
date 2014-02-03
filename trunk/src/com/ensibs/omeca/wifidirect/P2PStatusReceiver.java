package com.ensibs.omeca.wifidirect;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class P2PStatusReceiver extends BroadcastReceiver{
	
	private WifiDirectNotificationCenter notificationCenter;
	
	public P2PStatusReceiver(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,WifiP2pManager.WIFI_P2P_STATE_DISABLED);
		switch (state) {
		case (WifiP2pManager.WIFI_P2P_STATE_ENABLED): 
			Log.i(WifiDirectProperty.TAG, "Framework P2P enabled");
			notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.ENABLED));
			break;
		default:
			Log.i(WifiDirectProperty.TAG, "Framework P2P status changed");
			break;
		}
	}
}
