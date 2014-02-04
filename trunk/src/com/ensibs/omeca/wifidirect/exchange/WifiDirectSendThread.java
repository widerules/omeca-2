package com.ensibs.omeca.wifidirect.exchange;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

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
			this.removeSender(socket);
		}
	}

	public void removeSender(Socket socket){
		try {
			this.client.get(socket.getInetAddress().getHostAddress()).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		this.client.remove(socket.getInetAddress().getHostAddress());
		this.socket.remove(socket.getInetAddress().getHostAddress());
	}

	public void run(){
		this.run = true;
		while(run & client.size() != 0){
			try{
				synchronized (this)
				{
					this.wait();
					if(run && eventTmp != null){
						Set<String> key = client.keySet();
						for(String s : key){
							ObjectOutputStream tmp = client.get(s);
							if(socket.get(s).isConnected()){
								try {
									tmp.writeObject(eventTmp);
									tmp.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else{
								this.removeSender(socket.get(s));
							}
						}
						this.eventTmp = null;
					}
				}
			}catch(InterruptedException exception){
				//TODO
			}
		}
		if(client.size() != 0){
			Set<String> key = client.keySet();
			for(String s : key){
				this.removeSender(socket.get(s));
			}
		}
		this.run = false;
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
	}
}