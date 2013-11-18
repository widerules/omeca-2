package com.ensibs.omeca.wifidirect;

import android.net.wifi.p2p.WifiP2pManager.ChannelListener;

//Class implement channelListener which listen loosing connection
public class WifiDirectChannelListener implements ChannelListener{
	
	private WifiDirectManager manager;
	
	public WifiDirectChannelListener(WifiDirectManager manager){
		this.manager = manager;
	}

	//Call when the channel is loose
	@Override
	public void onChannelDisconnected() {
		manager.initializeWiFiDirect();
	}

}
