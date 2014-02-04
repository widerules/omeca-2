package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class WifiDirectSendThread extends Thread{

	private Map<String,ObjectOutputStream> client =  new HashMap<String, ObjectOutputStream>();
	private Map<String,Socket> socket = new HashMap<String, Socket>();
	private volatile boolean run = false;
	private WifiDirectEventImpl eventTmp = null;

	public void addSender(Socket socket){
		try {
			this.socket.put(socket.getInetAddress().getHostAddress(), socket);
			this.client.put(socket.getInetAddress().getHostAddress(), new ObjectOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			this.removeSender(socket);
		}
	}

	public void removeSender(Socket socket){
		try {
			this.client.get(socket.getInetAddress().getHostAddress()).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		this.client.remove(socket.getInetAddress().getHostAddress());
		this.socket.remove(socket.getInetAddress().getHostAddress());
	}

	public synchronized void run(){
		this.run = true;
		while(run & client.size() != 0){
			try{
					this.wait();
					if(run && eventTmp != null){
						Set<String> key = client.keySet();
						for(String s : key){
							ObjectOutputStream tmp = client.get(s);
							if(socket.get(s).isConnected()){
								try {
									Log.i(WifiDirectProperty.TAG, "msg en envoi");
									tmp.writeObject(eventTmp);
									tmp.flush();
								} catch (IOException e) {
									//TODO : send deco
									this.removeSender(socket.get(s));
									e.printStackTrace();
								}
							}
							else{
								this.removeSender(socket.get(s));
							}
						}
						this.eventTmp = null;
					}
				
			}catch(InterruptedException exception){
				//TODO
				exception.printStackTrace();

			}
		}
		if(client.size() != 0){
			Set<String> key = client.keySet();
			for(String s : key){
				this.removeSender(socket.get(s));
			}
		}
		this.run = false;
		Log.i(WifiDirectProperty.TAG, "Thread envoi fini");
	}

	public synchronized void sendEvent(WifiDirectEventImpl event){
		this.eventTmp = event;
		this.notify();
	}

	public synchronized boolean getRun(){
		return this.run;
	}

	public synchronized void stopThread(){
		this.run = false;
		this.interrupt();
	}
}