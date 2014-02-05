package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Player;

/**
 * Event when the host allows a new player to be part of the game
 * @author Nicolas
 *
 */
public class AknowlegmentConnectionAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Player player;
	
	/**
	 * Constructor
	 * @param user Player id
	 */
	public AknowlegmentConnectionAction(Player user){
		this.player = user;
	}

	/**
	 * Getter on Player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
}
