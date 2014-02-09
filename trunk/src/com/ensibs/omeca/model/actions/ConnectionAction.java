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
	
	private Player player;
	
	/**
	 * Constructor
	 * @param user player
	 */
	public ConnectionAction(Player user){
		this.player = user;
	}

	/**
	 * Getter on player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
}
