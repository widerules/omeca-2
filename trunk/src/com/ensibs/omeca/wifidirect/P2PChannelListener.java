package com.ensibs.omeca.wifidirect;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

import android.net.wifi.p2p.WifiP2pManager.ChannelListener;

public class P2PChannelListener implements ChannelListener{
	
	private WifiDirectNotificationCenter notificationCenter;
	
	public P2PChannelListener(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}

	@Override
	public void onChannelDisconnected() {
		// TODO Unregistred and reinitialise channel + notify
		this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.CHANNEL_LOST));
	}
}
