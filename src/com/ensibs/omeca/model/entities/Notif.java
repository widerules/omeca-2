package com.ensibs.omeca.model.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model for Notif entity. Represents a game notification
 * 
 * @author Nicolas
 * 
 */
public class Notif implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String date;
	private Player source;
	private Player target;
	private String event;

	/**
	 * Constructor
	 */
	public Notif() {
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss",
				Locale.FRANCE);
		this.date = formatter.format(new Date());
		this.source = null;
		this.target = null;
		this.event = "";
	}

	/**
	 * Returns the date of the Notif
	 * 
	 * @return date The date of the Notif
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date of the Notif
	 * 
	 * @param date
	 *            The new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Returns the source of the Notif
	 * 
	 * @return source The source Player of the Notif
	 */
	public Player getSource() {
		return source;
	}

	/**
	 * Sets the source Player of the Notif
	 * 
	 * @param source
	 *            The source Player
	 */
	public void setSource(Player source) {
		this.source = source;
	}

	/**
	 * Returns the target of the Notif
	 * 
	 * @return target The target Player of the Notif
	 */
	public Player getTarget() {
		return target;
	}

	/**
	 * Sets the target Player of the Notif
	 * 
	 * @param source
	 *            The target Player
	 */
	public void setTarget(Player target) {
		this.target = target;
	}

	/**
	 * Returns the event of the Notif
	 * 
	 * @return event The event of the Notif
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * Sets the event of the Notif
	 * 
	 * @param event
	 *            The event of the Notif
	 */
	public void setEvent(String event) {
		this.event = event;
	}

}
