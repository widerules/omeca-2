package com.ensibs.omeca.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Model that represents a GameEntity. It's a super class for all the others
 * entities which own Cards
 * 
 * @author OMECA 2.0 Team (Raphael GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public abstract class GameEntity implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<Card> cards;

	/**
	 * Constructor
	 */
	public GameEntity() {
		cards = new ArrayList<Card>();
	}

	/**
	 * Randomly shuffle the Cards of the GameEntity
	 */
	public void shuffle() {
		Random random = new Random();
		int r;
		for (int i = 0; i < cards.size(); i++) {
			r = random.nextInt(cards.size());
			moveCard(cards.get(i), r);
		}
	}

	/**
	 * Cuts the Cards of the GameEntity at the position given in parameters
	 * 
	 * @param index
	 *            The position of cut
	 */
	public void cut(int index) {
		List<Card> l1 = cards.subList(0, index);
		List<Card> l2 = cards.subList(index, cards.size());
		cards = new ArrayList<Card>();
		cards.addAll(l2);
		cards.addAll(l1);
	}

	/**
	 * Moves a Card at the given position
	 * 
	 * @param card
	 *            The Card to move
	 * @param index
	 *            The position
	 */
	public void moveCard(Card card, int index) {
		cards.remove(card);
		cards.add(index, card);
	}

	/**
	 * Adds a Card to the GameEntity cards
	 * 
	 * @param card
	 *            The Card to add
	 * @return true If the Card has well been added, false else
	 */
	public boolean addCard(Card card) {
		return cards.add(card);
	}

	/**
	 * Removes a Card from the GameEntity cards
	 * 
	 * @param card
	 *            The Card to remove
	 * @return true If the Card has well been removed, false else
	 */
	public boolean removeCard(Card card) {
		return cards.remove(card);
	}

	/**
	 * Removes the last Card from the GameEntity cards
	 * 
	 * @return true If the Card has well been removed, false else
	 */
	public Card removeLastCard() {
		return cards.remove(cards.size() - 1);
	}

	/**
	 * Removes the first Card from the GameEntity cards
	 * 
	 * @return true If the Card has well been removed, false else
	 */
	public Card removeFirstCard() {
		return cards.remove(0);
	}

	/**
	 * Returns the number of Cards of the GameEntity
	 * 
	 * @return the number of Cards
	 */
	public int getNumberOfCards() {
		return cards.size();
	}

	/**
	 * Returns all the Cards of the GameEntity
	 * 
	 * @return cards The ArrayList of Cards
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * Sets all the Cards of the GameEntity
	 * 
	 * @param cards
	 *            The ArrayList of Cards
	 */
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
}
