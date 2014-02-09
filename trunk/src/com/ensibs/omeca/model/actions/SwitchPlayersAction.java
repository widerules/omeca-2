package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Player;

/**
 * Event when two Players are switched
 * @author Nicolas
 *
 */
public class SwitchPlayersAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Player player1;
	private Player player2;
	private int position;
	
	/**
	 * Constructor
	 * @param player1 player1
	 * @param player2 player2
	 * @param pos position
	 */
	public SwitchPlayersAction(Player player1, Player player2, int pos){
		this.player1 = player1;
		this.player2 = player2;
		this.position = pos;
	}

	/**
	 * Getter on player1
	 * @return player1
	 */
	public Player getP1() {
		return player1;
	}
	
	/**
	 * Getter on player2
	 * @return player2
	 */
	public Player getP2() {
		return player2;
	}

	/**
	 * Getter on position
	 * @return position
	 */
	public int getPosition() {
		return position;
	}
	
}
