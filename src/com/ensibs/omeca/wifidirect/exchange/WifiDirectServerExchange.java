package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;

public class WifiDirectServerExchange extends WifiDirectIExchange{
	
	private ServerSocket server;
	private Map<String,WifiDirectReceiverExchange> receiver;
	private Map<String,ObjectOutputStream> oos;
	private WifiDirectAcceptExchange acceptExchange;

	@Override
	public void sendEvent(WifiDirectEvent event) {
		Set<String> key = oos.keySet();
		for(String s : key){
			ObjectOutputStream tmp = oos.get(s);
			try {
				tmp.writeObject(event);
				tmp.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receivedEvent(WifiDirectEvent event) {
		setChanged();
	    notifyObservers(event);
	}

	@Override
	public void startExchange() {
		try {
			server = new ServerSocket(8888);
			acceptExchange = new WifiDirectAcceptExchange(this, server);
			receiver = new HashMap<String, WifiDirectReceiverExchange>();
			oos =  new HashMap<String, ObjectOutputStream>();
			acceptExchange.start();
			System.out.println("Server start");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopExchange() {
		stopAcceptClient();
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<String> key = receiver.keySet();
		for(String s : key){
			receiver.get(s).stopReceiver();
		}
		key = oos.keySet();
		for(String s : key){
			try {
				oos.get(s).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopAcceptClient(){
		acceptExchange.stopAccept();
	}
	
	public void addClient(Socket client){
		System.out.println("Connection client");
		try {
			receiver.put(client.getInetAddress().getHostAddress(), new WifiDirectReceiverExchange(this, new ObjectInputStream(client.getInputStream())));
			receiver.get(client.getInetAddress().getHostAddress()).start();
			oos.put(client.getInetAddress().getHostAddress(),new ObjectOutputStream(client.getOutputStream()));
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
