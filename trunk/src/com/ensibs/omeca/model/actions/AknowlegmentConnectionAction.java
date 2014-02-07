package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Board;
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
	private Board board;
	/**
	 * Constructor
	 * @param user Player
	 * @param board Board
	 */
	public AknowlegmentConnectionAction(Player user, Board board){
		this.player = user;
		this.board = board;
	}

	/**
	 * Getter on Player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Getter on Board
	 * @return board
	 */
	public Board getBoard() {
		return board;
	}
}
