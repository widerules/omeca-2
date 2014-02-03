package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * 
 *
 */
public class WifiDirectReceivedThread extends Thread{

	private volatile boolean run = false;
	private WifiDirectIExchange wifiDirectIExchange;
	private Socket socket = null;
	private ObjectInputStream objectInputStream;

	public WifiDirectReceivedThread(WifiDirectIExchange wifiDirectIExchange, Socket socket ){
		this.wifiDirectIExchange = wifiDirectIExchange;
		this.socket = socket;
	}

	public void run(){
		this.run = true;
		try {
			this.objectInputStream =  new ObjectInputStream(this.socket.getInputStream());
			while(run && this.socket.isConnected()){
				Object obj;
				obj = objectInputStream.readObject();
				if (obj instanceof WifiDirectEventImpl && run) {
					WifiDirectEventImpl event = (WifiDirectEventImpl) obj ;
					wifiDirectIExchange.receivedEvent(event);
				}

			}
		}catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
		}
		this.run = false;
	}

	public synchronized boolean getRun(){
		return this.run;
	}

	public synchronized void stopThread(){
		this.run = false;
	}
}
