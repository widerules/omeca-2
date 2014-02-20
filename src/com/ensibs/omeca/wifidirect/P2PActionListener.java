package com.ensibs.omeca.wifidirect;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.util.Log;

/**
 * Wifi direct listen for result of action
 */
public class P2PActionListener implements ActionListener {
	
	/**
	 * notification center for api wifi direct
	 */
	private WifiDirectNotificationCenter notificationCenter;
	
	/**
	 * Constructor
	 * @param notificationCenter notification center for api wifi direct
	 */
	public P2PActionListener(WifiDirectNotificationCenter notificationCenter){
		this.notificationCenter = notificationCenter;
	}
	
	/**
	 * If the action failed in launch it
	 */
	@Override
	public void onFailure(int reason) {
		switch (reason) {
		case WifiP2pManager.BUSY:
			Log.e(WifiDirectProperty.TAG, "Framework P2P busy");
			notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.BUSY));
			break;
		case WifiP2pManager.ERROR:
			Log.e(WifiDirectProperty.TAG, "Framework P2P internal error");
			notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.ERROR));
			break;
		case WifiP2pManager.P2P_UNSUPPORTED:
			Log.e(WifiDirectProperty.TAG, "Framework P2P unsupported");
			notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.UNSUPPORTED));
			break;
		default:
			Log.e(WifiDirectProperty.TAG, "Framework P2P unknown error");
			notificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.ERROR));
			break;
		}
	}

	/**
	 * If the action succeed in launch it
	 */
	@Override
	public void onSuccess() {
		// Nothing to do.
	}
}
