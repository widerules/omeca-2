package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

import com.ensibs.omeca.wifidirect.WifiDirectNotificationCenter;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class WifiDirectExchangeServer extends WifiDirectIExchange{

	private WifiDirectAcceptServer acceptServer = null;
	private ArrayList<Socket> client = new ArrayList<Socket>();
	private WifiDirectSendThread sendThread = null;
	private ArrayList<WifiDirectReceivedThread> receivedThread = new ArrayList<WifiDirectReceivedThread>();

	public WifiDirectExchangeServer(WifiDirectNotificationCenter wifiDirectNotificationCenter) {
		super(wifiDirectNotificationCenter);
		this.acceptServer = new WifiDirectAcceptServer(this);
	}

	public void addClient(Socket client) {
		if(sendThread == null){
			Log.i(WifiDirectProperty.TAG, "Add Client int");
			this.sendThread = new WifiDirectSendThread();
			this.sendThread.addSender(client);
			this.sendThread.start();
		}
		else{
			Log.i(WifiDirectProperty.TAG, "Add Client no int");
			this.sendThread.addSender(client);
		}
		this.client.add(client);
		WifiDirectReceivedThread tmp = new WifiDirectReceivedThread(this,client);
		tmp.start();
		receivedThread.add(tmp);
		wifiDirectNotificationCenter.notifyManager(new WifiDirectEventImpl(WifiDirectEvent.NEW_CLIENT));
	}

	public void removeClient(Socket client) {
		// TODO Auto-generated method stub
	}

	public void stopAccept(){
		if(this.acceptServer !=null && this.acceptServer.getRun()){
			this.acceptServer.stopAccept();
		}
	}

	@Override
	public void startExchange() {
		Log.i(WifiDirectProperty.TAG, "startserver");
		if(this.acceptServer !=null){
			this.acceptServer.start();
		}
	}

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void sendEvent(WifiDirectEventImpl event) {
		Log.i(WifiDirectProperty.TAG, "Client ="+this.receivedThread.size());
		if(this.sendThread != null && this.sendThread.getRun()){
			this.sendThread.sendEvent(event);
		}
	}
}