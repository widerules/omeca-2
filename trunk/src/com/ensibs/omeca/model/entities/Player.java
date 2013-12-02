package com.ensibs.omeca.model.entities;

public class Player extends GameEntity {
	private String username;
	private String avatar;
	
	public Player(){
		this.username = "";
		this.avatar = null;
	}
	public Player(String name, String avatar){
		this.username = name;
		this.avatar = avatar;
	}
	public String getName() {
		return username;
	}
	public void setName(String name) {
		this.username = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}	
	
}
