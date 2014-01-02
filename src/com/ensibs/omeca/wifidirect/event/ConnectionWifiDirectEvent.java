package com.ensibs.omeca.wifidirect.event;

public class ConnectionWifiDirectEvent implements WifiDirectEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Event getEvent() {
		return Event.CONNECTION;
	}

	@Override
	public Object getData() {
		return null;
	}

}
