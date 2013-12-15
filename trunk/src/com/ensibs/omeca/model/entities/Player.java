package com.ensibs.omeca.model.entities;

public class Player extends GameEntity {
	private String username;
	private int avatar;
	
	public Player(){
		this.username = "";
		this.avatar = 0;
	}
	public Player(String name, int avatar){
		this.username = name;
		this.avatar = avatar;
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
