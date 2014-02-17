package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * 
 *
 */
public class WifiDirectAcceptServer extends Thread{

	private volatile boolean run;
	private ServerSocket server;	
	private WifiDirectExchangeServer exchangeServer;

	public WifiDirectAcceptServer(WifiDirectExchangeServer exchangeServer){
		this.exchangeServer = exchangeServer;
	}

	public void run(){
		this.run = true;
		try {
			this.server = new ServerSocket();
			this.server.setReuseAddress(true);
	        this.server.bind(new InetSocketAddress(WifiDirectProperty.PORT));
			while(run){
				Socket client;
				try {
					client = server.accept();
					if(run){
						exchangeServer.addClient(client);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.server != null  && this.server.isBound()){
			try {
				this.server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		this.run = false;
	}

	public synchronized void stopAccept(){
		this.run = false;
		this.interrupt();
		try {
			this.server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}	

	public synchronized boolean getRun(){
		return this.run;
	}
}