package com.ensibs.omeca.wifidirect.event;

public class MessageWifiDirectEvent implements WifiDirectEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object data;
	
	public MessageWifiDirectEvent(Object data){
		this.data = data;
	}

	@Override
	public Event getEvent() {
		return Event.MESSAGE;
	}

	@Override
	public Object getData() {
		return data;
	}
}
