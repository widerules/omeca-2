package com.ensibs.omeca.wifidirect;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

import android.net.wifi.p2p.WifiP2pManager.ChannelListener;

/**
 * Listen for wifi direct channel set or disconnect
 */
public class P2PChannelListener implements ChannelListener{
	
	/**
	 * notification center for api wifi direct
	 */
	private WifiDirectNotificationCenter notificationCenter;
	
	/**
	 * Constructor
	 * @param notificationCenter notification center for api wifi direct
	 */
	public P2PChannelListener(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}

	@Override
	public void onChannelDisconnected() {
		// TODO Unregistred and reinitialise channel + notify
		this.notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.CHANNEL_LOST));
	}
}
