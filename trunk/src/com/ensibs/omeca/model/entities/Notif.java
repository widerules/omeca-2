package com.ensibs.omeca.model.entities;

import java.util.Date;

public class Notif extends GameEntity{
	private String date;
	private Player source;
	private Player target;
	private String event;
	
	public Notif(){
	
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date2) {
		this.date = date2;
	}

	public Player getSource() {
		return source;
	}

	public void setSource(Player source) {
		this.source = source;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	

}
