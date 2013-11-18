package com.ensibs.omeca.wifidirect;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.util.Log;

//Class which implements ActionListener used to listen of wifi direct action
public class WifiDirectManagerActionListener implements ActionListener{
	
	private static final String TAG = "ChatOmecaWifiDirect";

	@Override
	public void onFailure(int reason) {
		String errorMessage = "WiFi Direct Failed: ";
	      switch (reason) {
	        case WifiP2pManager.BUSY : 
	          errorMessage += "Framework busy."; break;
	        case WifiP2pManager.ERROR : 
	          errorMessage += "Internal error."; break;
	        case WifiP2pManager.P2P_UNSUPPORTED : 
	          errorMessage += "Unsupported."; break;
	        default: 
	          errorMessage += "Unknown error."; break;
	      }
	      Log.d(TAG, errorMessage);
	    }

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		//Nothing, it broadcast response in a intent
	}
}
