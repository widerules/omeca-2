package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WifiDirectAcceptExchange extends Thread{
	
	private WifiDirectServerExchange serverExchange;
	private volatile boolean run;
	private ServerSocket server;
	
	public WifiDirectAcceptExchange(WifiDirectServerExchange serverExchange, ServerSocket server){
		this.server = server;
		this.serverExchange = serverExchange;
	}
	
	public void run(){
		run = true;
		while(run){
			try {
				Socket client = server.accept();
				if(run){
					serverExchange.addClient(client);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stopAccept(){
		run = false;
	}	

}
