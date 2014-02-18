package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * Generic thread to receive event
 */
public class WifiDirectReceivedThread extends Thread{

	//Is thread is running ?
	private volatile boolean run = false;
	//Notification point
	private WifiDirectIExchange wifiDirectIExchange;
	//Socket of the client
	private Socket socket = null;
	//Related input stream of the socket
	private ObjectInputStream objectInputStream;
	
	/**
	 * Constructor
	 * @param wifiDirectIExchange interface notification point
	 * @param socket socket to listen
	 */
	public WifiDirectReceivedThread(WifiDirectIExchange wifiDirectIExchange, Socket socket ){
		this.wifiDirectIExchange = wifiDirectIExchange;
		this.socket = socket;
	}

	/**
	 * Thread loop
	 */
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.run = false;
	}

	/**
	 * Is the thread running ?
	 * @return boolean isRunning 
	 */
	public synchronized boolean getRun(){
		return this.run;
	}

	/**
	 * Stop the thread
	 */
	public synchronized void stopThread(){
		this.run = false;
		this.interrupt();
	}
}
