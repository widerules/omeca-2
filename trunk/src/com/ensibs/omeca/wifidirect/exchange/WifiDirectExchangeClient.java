package com.ensibs.omeca.wifidirect.exchange;

import java.net.Socket;

import android.util.Log;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * 
 *
 */
public class WifiDirectExchangeClient extends WifiDirectIExchange{
	
	private WifiDirectReceivedThread wifiDirectReceivedThread = null;
	private WifiDirectSendThread wifiDirectSendThread = null;
	private String ipAdress = null;
	private Socket socket = null;

	public WifiDirectExchangeClient(WifiDirectNotificationCenter wifiDirectNotificationCenter,String ipAdress) {
		super(wifiDirectNotificationCenter);
		this.ipAdress = ipAdress;
	}

	@Override
	public void startExchange() {
		(new WifiDirectInitThreadClient(this)).start();
	}

	@Override
	public void stopExchange() {
		if(this.wifiDirectReceivedThread != null && this.wifiDirectReceivedThread.getRun())
			this.wifiDirectReceivedThread.stopThread();
		if(this.wifiDirectSendThread != null && this.wifiDirectSendThread.getRun())
			this.wifiDirectSendThread.stopThread();
	}

	@Override
	public void sendEvent(WifiDirectEventImpl event) {
		if(this.wifiDirectSendThread != null && this.wifiDirectSendThread.getRun()){
			Log.i(WifiDirectProperty.TAG, "J'envoi");
			this.wifiDirectSendThread.sendEvent(event);
		}
	}

	public WifiDirectReceivedThread getWifiDirectReceivedThread() {
		return wifiDirectReceivedThread;
	}

	public WifiDirectSendThread getWifiDirectSendThread() {
		return wifiDirectSendThread;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setWifiDirectReceivedThread(
			WifiDirectReceivedThread wifiDirectReceivedThread) {
		this.wifiDirectReceivedThread = wifiDirectReceivedThread;
	}

	public void setWifiDirectSendThread(WifiDirectSendThread wifiDirectSendThread) {
		this.wifiDirectSendThread = wifiDirectSendThread;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getIpAdress() {
		return ipAdress;
	}
}