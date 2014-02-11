package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Card;

/**
 * Event when the draw pile is shuffled
 * @author Nicolas
 *
 */
public class ShuffleAction implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Card[] cards;
	
	/**
	 * Constructor
	 * @param cards2 the draw pile cards
	 */
	public ShuffleAction(Card[] cards){
		this.cards = cards;
	}

	/**
	 * Getter on cards
	 * @return cards
	 */
	public Card[] getCards() {
		return cards;
	}
}
