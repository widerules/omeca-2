package com.ensibs.omeca.wifidirect.event;

import java.io.Serializable;

public class WifiDirectEventImpl implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private WifiDirectEvent event;
	private int source;
	private Object data;
	
	public 	WifiDirectEventImpl(WifiDirectEvent event){
		this.event = event;
		this.data = null;
		this.source = -1;
	}
	
	public 	WifiDirectEventImpl(WifiDirectEvent event, Object data){
		this.event = event;
		this.data = data;
		this.source = -1;
	}

	public WifiDirectEvent getEvent() {
		return event;
	}

	public Object getData() {
		return data;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int i) {
		this.source = i;
	}	
}