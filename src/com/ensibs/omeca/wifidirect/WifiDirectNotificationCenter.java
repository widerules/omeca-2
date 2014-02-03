package com.ensibs.omeca.wifidirect;

import java.util.Observable;

import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

public class WifiDirectNotificationCenter extends Observable{
	
	public void notifyManager(WifiDirectEventImpl event){
		setChanged();
		notifyObservers(event);
	}
}
