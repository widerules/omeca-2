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

/**
 * Generic thread to send event
 */
public class WifiDirectSendThread extends Thread{

	//List of client
	private Map<String,ObjectOutputStream> client =  new HashMap<String, ObjectOutputStream>();
	//List of client socket
	private Map<String,Socket> socket = new HashMap<String, Socket>();
	//Is thread is running ?
	private volatile boolean run = false;
	//Event to send
	private WifiDirectEventImpl eventTmp = null;

	/**
	 * Add a new client
	 * @param socket socket client
	 */
	public void addSender(Socket socket){
		try {
			this.socket.put(socket.getInetAddress().getHostAddress(), socket);
			this.client.put(socket.getInetAddress().getHostAddress(), new ObjectOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			this.removeSender(socket);
		}
	}

	/**
	 * Delete a client
	 * @param socket socket client to delete
	 */
	public void removeSender(Socket socket){
		try {
			this.client.get(socket.getInetAddress().getHostAddress()).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client.remove(socket.getInetAddress().getHostAddress());
		this.socket.remove(socket.getInetAddress().getHostAddress());
	}

	/**
	 * Thread loop
	 */
	public synchronized void run(){
		this.run = true;
		while(run){
			try{
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

	/**
	 * Add event to send and notify the thread
	 * @param event
	 */
	public synchronized void sendEvent(WifiDirectEventImpl event){
		this.eventTmp = event;
		this.notify();
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