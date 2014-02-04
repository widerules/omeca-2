package com.ensibs.omeca.wifidirect.event;

import java.io.Serializable;

public class WifiDirectEventImpl implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private WifiDirectEvent event;
	private String source;
	private Object data;
	
	public 	WifiDirectEventImpl(WifiDirectEvent event){
		this.event = event;
		this.data = null;
		this.source = null;
	}
	
	public 	WifiDirectEventImpl(WifiDirectEvent event, Object data){
		this.event = event;
		this.data = data;
		this.source = null;
	}
	
	public 	WifiDirectEventImpl(WifiDirectEvent event, Object data, String source){
		this.event = event;
		this.data = data;
		this.source = source;
	}

	public WifiDirectEvent getEvent() {
		return event;
	}

	public Object getData() {
		return data;
	}

	public String getSource() {
		return source;
	}	
}