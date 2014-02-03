package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * 
 *
 */
public class WifiDirectInitThreadClient extends Thread{
	
	private WifiDirectExchangeClient wifiDirectExchangeClient;
	
	public WifiDirectInitThreadClient(WifiDirectExchangeClient wifiDirectExchangeClient){
		this.wifiDirectExchangeClient = wifiDirectExchangeClient;
	}
	
	public void run(){
		try {
			this.wifiDirectExchangeClient.setSocket(new Socket(this.wifiDirectExchangeClient.getIpAdress(),WifiDirectProperty.PORT));
			this.wifiDirectExchangeClient.setWifiDirectReceivedThread(new WifiDirectReceivedThread(this.wifiDirectExchangeClient, this.wifiDirectExchangeClient.getSocket()));
			this.wifiDirectExchangeClient.setWifiDirectSendThread(new WifiDirectSendThread());
			this.wifiDirectExchangeClient.getWifiDirectSendThread().addSender(this.wifiDirectExchangeClient.getSocket());
			this.wifiDirectExchangeClient.getWifiDirectReceivedThread().start();
			this.wifiDirectExchangeClient.getWifiDirectSendThread().start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

}
