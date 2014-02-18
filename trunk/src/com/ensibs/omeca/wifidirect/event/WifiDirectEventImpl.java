package com.ensibs.omeca.wifidirect.event;

import java.io.Serializable;

/**
 * Implementation of a wifi direct event from the API
 */
public class WifiDirectEventImpl implements Serializable{
	
	//Serializable number
	private static final long serialVersionUID = 1L;
	//Type of the event
	private WifiDirectEvent event;
	//Id player source of the event
	private int source;
	//Binary data with the event
	private Object data;
	
	/**
	 * Constructor
	 * @param event type of the event
	 */
	public 	WifiDirectEventImpl(WifiDirectEvent event){
		this.event = event;
		this.data = null;
		this.source = -1;
	}
	
	/**
	 * Constructor
	 * @param event
	 * @param data
	 */
	public 	WifiDirectEventImpl(WifiDirectEvent event, Object data){
		this.event = event;
		this.data = data;
		this.source = -1;
	}

	/**
	 * Access of the event
	 * @return
	 */
	public WifiDirectEvent getEvent() {
		return event;
	}

	/**
	 * Acces to the binary object
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Ge the id player source
	 * @return
	 */
	public int getSource() {
		return source;
	}

	/**
	 * Change the id player source of the event
	 * @param i id player source
	 */
	public void setSource(int i) {
		this.source = i;
	}	
}