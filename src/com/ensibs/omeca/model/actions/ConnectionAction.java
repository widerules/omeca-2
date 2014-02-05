package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Player;

/**
 * Event when a Player join the table
 * @author Nicolas
 *
 */
public class ConnectionAction implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Player idPlayer;
	
	/**
	 * Constructor
	 * @param user Player id
	 */
	public ConnectionAction(Player user){
		this.idPlayer = user;
	}

	public Player getPlayer() {
		return idPlayer;
	}
}
