package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import android.util.Log;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

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
					Log.i(WifiDirectProperty.TAG, "msg recu");
					WifiDirectEventImpl event = (WifiDirectEventImpl) obj ;
					wifiDirectIExchange.receivedEvent(event);
				}

			}
		}catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}
		this.run = false;
	}

	public synchronized boolean getRun(){
		return this.run;
	}

	public synchronized void stopThread(){
		this.run = false;
		this.interrupt();
	}
}
