package com.ensibs.omeca.wifidirect.event;

import java.io.Serializable;

public interface WifiDirectEvent extends Serializable{
	
	public static enum Event {CONNECTION, MESSAGE, ACTION};
	
	public Event getEvent();
	
	public Object getData();
}