package com.ensibs.omeca.wifidirect.exchange;

import java.util.Observable;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;

public abstract class WifiDirectIExchange extends Observable{
	
	public abstract void sendEvent(WifiDirectEvent event);
	
	public abstract void receivedEvent(WifiDirectEvent event);
	
	public abstract void startExchange();
	
	public abstract void stopExchange();

}