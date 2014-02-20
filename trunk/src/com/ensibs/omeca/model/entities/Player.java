package com.ensibs.omeca.model.entities;

import com.ensibs.omeca.wifidirect.WifiDirectManager;

/**
 * Model for the Player entity
 * 
 * @author OMECA 2.0 Team (Raphael GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class Player extends GameEntity {
	
	
	private static final long serialVersionUID = 1L;
	private String username;
	private int avatar;
	private int id;
	private String macAddress;

	/**
	 * Constructor
	 */
	public Player() {
		this.username = "";
		this.avatar = 0;
		this.id = 0;
		this.macAddress = WifiDirectManager.getMACAddress();
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of the Player
	 * @param avatar
	 *            The avatar ID of the Player
	 * @param id
	 *            The Player id
	 */
	public Player(String name, int avatar, int id) {
		this.username = name;
		this.avatar = avatar;
		this.id = id;
		this.macAddress = WifiDirectManager.getMACAddress();
	}

	/**
	 * Returns the MAC Address of the Player matching his device
	 * 
	 * @return macAdress The MAC Address of the Player
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Returns the Player id
	 * 
	 * @return id the Player id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the Player id
	 * 
	 * @param id
	 *            The Player id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the name of the Player
	 * 
	 * @return username The name of the Player
	 */
	public String getName() {
		return username;
	}

	/**
	 * Sets the name of the Player
	 * 
	 * @param name
	 *            The name of the Player
	 */
	public void setName(String name) {
		this.username = name;
	}

	/**
	 * Returns the avatar ID of the Player
	 * 
	 * @return avatar The avatar ID of the Player
	 */
	public int getAvatar() {
		return avatar;
	}

	/**
	 * Sets the avatar ID of the Player
	 * 
	 * @param avatar
	 *            The avatar ID of the Player
	 */
	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

}
