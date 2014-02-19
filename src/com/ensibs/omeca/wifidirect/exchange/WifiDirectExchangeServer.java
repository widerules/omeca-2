package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Implementation of WifiDirectIExchange for server
 */
public class WifiDirectExchangeServer extends WifiDirectIExchange{

	/**
	 * Is the accepting thread is running ?
	 */
	private WifiDirectAcceptServer acceptServer = null;
	/**
	 * List of client socket
	 */
	private ArrayList<Socket> client = new ArrayList<Socket>();
	/**
	 * The acceptation thread
	 */
	private WifiDirectSendThread sendThread = null;
	/**
	 * List of received thread (1 by client)
	 */
	private ArrayList<WifiDirectReceivedThread> receivedThread = new ArrayList<WifiDirectReceivedThread>();

	/**
	 * 
	 * @param wifiDirectNotificationCenter
	 */
	public WifiDirectExchangeServer(WifiDirectNotificationCenter wifiDirectNotificationCenter) {
		super(wifiDirectNotificationCenter);
		this.acceptServer = new WifiDirectAcceptServer(this);
	}

	/**
	 * Add a new client
	 * @param socket socket client
	 */
	public void addClient(Socket client) {
		if(sendThread == null){
			this.sendThread = new WifiDirectSendThread();
			this.sendThread.addSender(client);
			this.sendThread.start();
		}
		else{
			this.sendThread.addSender(client);
		}
		this.client.add(client);
		WifiDirectReceivedThread tmp = new WifiDirectReceivedThread(this,client);
		tmp.start();
		receivedThread.add(tmp);
		wifiDirectNotificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.NEW_CLIENT));
	}

	/**
	 * Delete a client
	 * @param socket socket client to delete
	 */
	public void removeClient(Socket client) {
		// TODO Auto-generated method stub
	}

	/**
	 * Stop the accepting thread
	 */
	public void stopAccept(){
		if(this.acceptServer !=null && this.acceptServer.getRun()){
			this.acceptServer.stopAccept();
		}
	}

	/**
	 * Start thread
	 */
	@Override
	public void startExchange() {
		if(this.acceptServer !=null){
			this.acceptServer.start();
		}
	}

	/**
	 * Stop the thread
	 */
	@Override
	public void stopExchange() {
		this.stopAccept();
		if(this.sendThread != null && this.sendThread.getRun())
			this.sendThread.stopThread();
		for(int i =0;i<this.receivedThread.size();i++){
			if(this.receivedThread.get(i).getRun()){
				this.receivedThread.get(i).stopThread();
			}
		}
		for(int i =0;i<this.client.size();i++){
			if(!this.client.get(i).isClosed()){
				try {
					this.client.get(i).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Send event to clients
	 * @param event the event
	 */
	@Override
	public void sendEvent(WifiDirectEventImpl event) {
		Log.i(WifiDirectProperty.TAG, "Client ="+this.receivedThread.size());
		if(this.sendThread != null && this.sendThread.getRun()){
			this.sendThread.sendEvent(event);
		}
	}
}