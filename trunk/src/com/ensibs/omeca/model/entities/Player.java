package com.ensibs.omeca.model.entities;

import com.ensibs.omeca.wifidirect.WifiDirectManager;

public class Player extends GameEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private int avatar;
	private int id;
	private String macAddress;

	public Player(){
		this.username = "";
		this.avatar = 0;
		this.id = 0;
		this.macAddress = WifiDirectManager.getMACAddress();
	}
	public Player(String name, int avatar, int id){
		this.username = name;
		this.avatar = avatar;
		this.id = id;
		this.macAddress = WifiDirectManager.getMACAddress();
	}
	
	public int getId() {
		return id;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public int getAvatar() {
		return avatar;
	}
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}	
	
}
