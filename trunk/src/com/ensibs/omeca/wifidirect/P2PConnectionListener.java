package com.ensibs.omeca.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class P2PConnectionListener extends BroadcastReceiver{
	
	private WifiDirectNotificationCenter notificationCenter;
	
	public P2PConnectionListener(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO send ip and launch client or server
		NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
		if (networkInfo.isConnected()){
			this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.CONNECTED));
		}
		else{
			this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.DISCONNECTED));
		}
	}
}