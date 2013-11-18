package com.ensibs.omeca.wifidirect.event;

public interface WifiDirectEvent {
	
	public static enum Event {CONNECTION, MESSAGE};
	
	public Event getEvent();
	
	public Object getData();
}