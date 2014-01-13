package com.ensibs.omeca.model.entities;

public class Player extends GameEntity {
	private String username;
	private int avatar;
	private int id;
	
	public Player(){
		this.username = "";
		this.avatar = 0;
		this.id = 0;
	}
	public Player(String name, int avatar, int id){
		this.username = name;
		this.avatar = avatar;
		this.id = id;
	}
	public int getId() {
		return id;
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
