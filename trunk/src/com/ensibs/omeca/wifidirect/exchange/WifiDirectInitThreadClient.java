package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Thread used to initialize all threads client and socket outside of the UIThread
 */
public class WifiDirectInitThreadClient extends Thread{
	
	/**
	 * The interface
	 */
	private WifiDirectExchangeClient wifiDirectExchangeClient;
	
	/**
	 * Constructor
	 * @param wifiDirectExchangeClient the interface
	 */
	public WifiDirectInitThreadClient(WifiDirectExchangeClient wifiDirectExchangeClient){
		this.wifiDirectExchangeClient = wifiDirectExchangeClient;
	}
	
	/**
	 * Thread loop which initialize all client thread
	 */
	public void run(){
		try {
			this.wifiDirectExchangeClient.setSocket(new Socket(this.wifiDirectExchangeClient.getIpAdress(),WifiDirectProperty.PORT));
			this.wifiDirectExchangeClient.setWifiDirectReceivedThread(new WifiDirectReceivedThread(this.wifiDirectExchangeClient, this.wifiDirectExchangeClient.getSocket()));
			this.wifiDirectExchangeClient.setWifiDirectSendThread(new WifiDirectSendThread());
			this.wifiDirectExchangeClient.getWifiDirectSendThread().addSender(this.wifiDirectExchangeClient.getSocket());
			this.wifiDirectExchangeClient.getWifiDirectReceivedThread().start();
			this.wifiDirectExchangeClient.getWifiDirectSendThread().start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
