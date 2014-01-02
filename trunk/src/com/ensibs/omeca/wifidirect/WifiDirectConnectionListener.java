package com.ensibs.omeca.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;

public class WifiDirectConnectionListener extends BroadcastReceiver {

	private WifiDirectManager manager;

	public WifiDirectConnectionListener(WifiDirectManager manager) {
		this.manager = manager;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Extract the NetworkInfo
		String extraKey = WifiP2pManager.EXTRA_NETWORK_INFO;
		NetworkInfo networkInfo = (NetworkInfo) intent
				.getParcelableExtra(extraKey);
		// Check if we’re connected
		if (networkInfo.isConnected()) {
			Log.d("OmecaChat", "Wi-Fi Direct connect");
			manager.getWifiP2pManager().requestConnectionInfo(
					manager.getWifiDirectChannel(),
					new ConnectionInfoListener() {
						public void onConnectionInfoAvailable(WifiP2pInfo info) {
							// If the connection is established
							if (info.groupFormed) {
								System.out.println(info.groupOwnerAddress.getHostAddress());
								manager.createExchange(info.groupOwnerAddress.getHostAddress());
								manager.setConnected(true);
								manager.notifyConnection();
							}
						}
					});
		} else {
			Log.d("OmecaChat", "Wi-Fi Direct Disconnected");
			manager.setConnected(false);
		}
	}
}