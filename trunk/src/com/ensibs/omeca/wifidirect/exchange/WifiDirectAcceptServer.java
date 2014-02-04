package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
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
		try{
			this.server = new ServerSocket(WifiDirectProperty.PORT);
			while(run){
				Socket client = server.accept();
				if(run){
					exchangeServer.addClient(client);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.run = false;
	}

	public synchronized void stopAccept(){
		this.run = false;
		this.interrupt();
	}	

	public synchronized boolean getRun(){
		return this.run;
	}
}