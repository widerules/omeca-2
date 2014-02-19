package com.ensibs.omeca.wifidirect.exchange;

import java.net.Socket;

import android.util.Log;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Implementation of WifiDirectIExchange for client
 */
public class WifiDirectExchangeClient extends WifiDirectIExchange{
	
	/**
	 * Thread for received event from host
	 */
	private WifiDirectReceivedThread wifiDirectReceivedThread = null;
	/**
	 * Thread for send event
	 */
	private WifiDirectSendThread wifiDirectSendThread = null;
	/**
	 * ip adress of the host
	 */
	private String ipAdress = null;
	/**
	 * Socket of the device
	 */
	private Socket socket = null;

	/**
	 * Constrcutor
	 * @param wifiDirectNotificationCenter notification center
	 * @param ipAdress ip adress of the host
	 */
	public WifiDirectExchangeClient(WifiDirectNotificationCenter wifiDirectNotificationCenter,String ipAdress) {
		super(wifiDirectNotificationCenter);
		this.ipAdress = ipAdress;
	}

	/**
	 * Start thread
	 */
	@Override
	public void startExchange() {
		(new WifiDirectInitThreadClient(this)).start();
	}

	/**
	 * Stop the thread
	 */
	@Override
	public void stopExchange() {
		if(this.wifiDirectReceivedThread != null && this.wifiDirectReceivedThread.getRun())
			this.wifiDirectReceivedThread.stopThread();
		if(this.wifiDirectSendThread != null && this.wifiDirectSendThread.getRun())
			this.wifiDirectSendThread.stopThread();
	}

	/**
	 * Add event to send and notify the thread
	 * @param event
	 */
	@Override
	public void sendEvent(WifiDirectEventImpl event) {
		if(this.wifiDirectSendThread != null && this.wifiDirectSendThread.getRun()){
			Log.i(WifiDirectProperty.TAG, "J'envoi");
			this.wifiDirectSendThread.sendEvent(event);
		}
	}

	/**
	 * Return receive event thread
	 * @return WifiDirectReceivedThread
	 */
	public WifiDirectReceivedThread getWifiDirectReceivedThread() {
		return wifiDirectReceivedThread;
	}

	/**
	 * Return send event thread
	 * @return WifiDirectSendThread
	 */
	public WifiDirectSendThread getWifiDirectSendThread() {
		return wifiDirectSendThread;
	}

	/**
	 * Return the socket of the client
	 * @return socket client
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Modification of the thread to receive event
	 * @param wifiDirectReceivedThread received thread
	 */
	public void setWifiDirectReceivedThread(
			WifiDirectReceivedThread wifiDirectReceivedThread) {
		this.wifiDirectReceivedThread = wifiDirectReceivedThread;
	}

	/**
	 * Modification of the thread to send event
	 * @param wifiDirectSendThread send thread
	 */
	public void setWifiDirectSendThread(WifiDirectSendThread wifiDirectSendThread) {
		this.wifiDirectSendThread = wifiDirectSendThread;
	}

	/**
	 * Modification of the device socket
	 * @param socket device socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Get ip adress of the device
	 * @return ip adress
	 */
	public String getIpAdress() {
		return ipAdress;
	}
}