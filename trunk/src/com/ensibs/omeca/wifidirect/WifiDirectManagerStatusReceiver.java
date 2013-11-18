package com.ensibs.omeca.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

public class WifiDirectManagerStatusReceiver extends BroadcastReceiver{
	
	private static final String TAG = "ChatOmecaWifiDirect";

	@Override
	public void onReceive(Context context, Intent intent) {
		 int state = intent.getIntExtra(
			        WifiP2pManager.EXTRA_WIFI_STATE,
			        WifiP2pManager.WIFI_P2P_STATE_DISABLED);
			      switch (state) {
			        case (WifiP2pManager.WIFI_P2P_STATE_ENABLED): 
			  	      Log.d(TAG, "Wifi direct P2P enabled");
			          break;
			        default: 
			  	      Log.d(TAG, "Default status receiver message");
			      }
	}
}