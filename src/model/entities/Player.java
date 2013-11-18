package model.entities;

import android.net.Uri;

public class Player extends GameEntity {
	private String name;
	private Uri avatar;
	
	public Player(){
		this.name = "";
		this.avatar = null;
	}
	public Player(String name, Uri avatar){
		this.name = name;
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Uri getAvatar() {
		return avatar;
	}
	public void setAvatar(Uri avatar) {
		this.avatar = avatar;
	}
	
	
}
