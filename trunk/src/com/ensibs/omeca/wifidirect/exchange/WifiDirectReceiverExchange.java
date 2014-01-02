package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;

public class WifiDirectReceiverExchange extends Thread{
	
	private ObjectInputStream ois;
	private WifiDirectIExchange iExchange;
	private volatile boolean run;
	
	public WifiDirectReceiverExchange(WifiDirectIExchange iExchange, ObjectInputStream ois){
		this.iExchange = iExchange;
		this.ois = ois;
	}
	
	public void run(){
		run = true;
		while(run){
			Object obj;
			try {
				obj = ois.readObject();
		         if (obj instanceof WifiDirectEvent) {
		        	 WifiDirectEvent event = (WifiDirectEvent) obj ;
		        	 iExchange.receivedEvent(event);
		         }
			} catch (OptionalDataException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void stopReceiver(){
		run = false;
	}	
}
