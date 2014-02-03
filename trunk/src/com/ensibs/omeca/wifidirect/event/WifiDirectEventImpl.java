package com.ensibs.omeca.wifidirect.event;

import java.io.Serializable;

public class WifiDirectEventImpl implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private WifiDirectEvent event;
	
	private Object data;
	
	public 	WifiDirectEventImpl(WifiDirectEvent event){
		this.event = event;
		this.data = null;
	}
	
	public 	WifiDirectEventImpl(WifiDirectEvent event, Object data){
		this.event = event;
		this.data = data;
	}

	public WifiDirectEvent getEvent() {
		return event;
	}

	public Object getData() {
		return data;
	}	
}