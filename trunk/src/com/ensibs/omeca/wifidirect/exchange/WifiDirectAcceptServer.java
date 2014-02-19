package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Thread used by the server to listen client connection
 */
public class WifiDirectAcceptServer extends Thread{

	/**
	 * Is running ?
	 */
	private volatile boolean run;
	/**
	 * Socket server which listen for client
	 */
	private ServerSocket server;	
	/**
	 * server interface for notification
	 */
	private WifiDirectExchangeServer exchangeServer;

	/**
	 * Constructeur
	 * @param exchangeServer server interface for notification
	 */
	public WifiDirectAcceptServer(WifiDirectExchangeServer exchangeServer){
		this.exchangeServer = exchangeServer;
	}

	/**
	 * Loop thread
	 */
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
			e.printStackTrace();
		}
		if(this.server != null  && this.server.isBound()){
			try {
				this.server.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		this.run = false;
	}

	/**
	 * Stop the thread by close accept server
	 */
	public synchronized void stopAccept(){
		this.run = false;
		this.interrupt();
		try {
			this.server.close();
		} catch (IOException e) {
		}
	}	

	/**
	 * Is the thread running ?
	 * @return true if is running, false else
	 */
	public synchronized boolean getRun(){
		return this.run;
	}
}