package com.ensibs.omeca.wifidirect.event;

public class ConnectionWifiDirectEvent implements WifiDirectEvent{

	@Override
	public Event getEvent() {
		return Event.CONNECTION;
	}

	@Override
	public Object getData() {
		return null;
	}

}
