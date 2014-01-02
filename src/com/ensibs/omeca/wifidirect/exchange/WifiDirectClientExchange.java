package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;

public class WifiDirectClientExchange extends WifiDirectIExchange{
	
	private Socket client;
	private String adress;
	private WifiDirectReceiverExchange receiver;
	private ObjectOutputStream oos;
	
	public WifiDirectClientExchange(String ip){
		adress = ip;
	}

	@Override
	public void sendEvent(WifiDirectEvent event) {
		try {
			oos.writeObject(event);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void receivedEvent(WifiDirectEvent event) {
		setChanged();
	    notifyObservers(event);
	}

	@Override
	public void startExchange() {
		Thread start = new Thread(new StartClient(this));
		start.start();
	}

	@Override
	public void stopExchange() {
		try {
			receiver.stopReceiver();
			oos.close();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private class StartClient implements Runnable{

		private WifiDirectIExchange iexchange;
		
		public StartClient(WifiDirectIExchange iexchange){
			this.iexchange = iexchange;
		}
		
		@Override
		public void run() {
			try {
				client =  new Socket(adress,8888);
				oos = new ObjectOutputStream(client.getOutputStream());
				receiver = new WifiDirectReceiverExchange(iexchange, new ObjectInputStream(client.getInputStream()));
				receiver.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
