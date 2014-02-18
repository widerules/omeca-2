package com.ensibs.omeca.model.entities;

import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * Model for the board entity
 * 
 * @author Nicolas
 * 
 */
public class Board extends GameEntity {
	
	/**
	 * Max number of Player on the table
	 */
	public static final int NB_PLAYER_MAX = 8;
	
	private static final long serialVersionUID = 1L;
	
	private int cardsToDeal;
	private Hashtable<Integer, Player> players;
	private DrawPile drawPile;
	private DiscardPile discardPile;

	/**
	 * Constructor
	 */
	public Board() {
		this.players = new Hashtable<Integer, Player>();
		this.drawPile = new DrawPile();
		this.discardPile = new DiscardPile();
		cardsToDeal = 0;
	}

	/**
	 * Init the drawpile filling it with cards
	 * 
	 * @param shuffle
	 *            If the cards have to be shuffled
	 */
	public void initDrawPile(boolean shuffle) {
		Card tmp;
		for (String c : Card.COLORS) {
			for (int v : Card.VALUES) {
				tmp = new Card(v, c);
				drawPile.addCard(tmp);
			}
		}
		drawPile.addCard(new Card(0, Card.JOKERS[0]));
		drawPile.addCard(new Card(0, Card.JOKERS[1]));
		if (shuffle)
			drawPile.shuffle();
	}

	/**
	 * Returns the list of Players
	 * 
	 * @return players The Hashtable of Players
	 */
	public Hashtable<Integer, Player> getPlayers() {
		return players;
	}

	/**
	 * Sets the list of Players on the table
	 * 
	 * @param players
	 *            The list of Players
	 */
	public void setPlayers(Hashtable<Integer, Player> players) {
		this.players = players;
	}

	/**
	 * Returns the DrawPile
	 * 
	 * @return drawPile The DrawPile attached to the board
	 */
	public DrawPile getDrawPile() {
		return drawPile;
	}

	/**
	 * Sets the DrawPile
	 * 
	 * @param drawPile
	 *            The DrawPile
	 */
	public void setDrawPile(DrawPile drawPile) {
		this.drawPile = drawPile;
	}

	/**
	 * Returns the DiscardPile
	 * 
	 * @return discardPile The DiscardPile attached to the board
	 */
	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	/**
	 * Sets the DiscardPile
	 * 
	 * @param discardPile
	 *            The DiscardPile
	 */
	public void setDiscardPile(DiscardPile discardPile) {
		this.discardPile = discardPile;
	}

	/**
	 * Returns the number of cards which has to be distributed automatically
	 * 
	 * @return cardsToDeal The number of cards to deal
	 */
	public int getCardsToDeal() {
		return cardsToDeal;
	}

	/**
	 * Set the number of cards which has to be distributed automatically
	 * 
	 * @param cardsToDeal
	 *            The number of cards to deal
	 */
	public void setCardsToDeal(int cardsToDeal) {
		this.cardsToDeal = cardsToDeal;
	}

	/**
	 * Deal cards to each Player on the table depending on the number of cards
	 * to deal (cardsToDeal)
	 * 
	 * @param from
	 *            Place of the Player the distribution has to begin from
	 * @deprecated
	 */
	public void dealCardsAutomatically(int from) {
		if (cardsToDeal * players.size() <= Card.getNumberOfCards()) {
			int position = from + 1;
			for (int i = 0; i < cardsToDeal; i++) {
				for (int j = 0; j < players.size(); j++) {
					if (position >= players.size())
						position = 0;
					drawCard(position);
					position++;
				}
			}
		}

	}

	/**
	 * Add a Player on the table
	 * 
	 * @param place
	 *            The Place of the new Player
	 * @param p
	 *            The Player
	 */
	public void addPlayer(int place, Player p) {
		if (!players.containsKey(place))
			players.put(place, p);
	}

	/**
	 * Remove a Player from the table
	 * 
	 * @param place
	 *            The place of the Player to remove
	 */
	public void removePlayer(int place) {
		players.remove(place);
	}

	/**
	 * Pick the first card of the DrawPile
	 * 
	 * @param player
	 *            The Player who will receive the card
	 * @deprecated
	 */
	public void drawCard(int player) {
		Card topCard = drawPile.cards.get(0);
		players.get(player).addCard(topCard);
		drawPile.removeCard(topCard);
	}

	/**
	 * Put a Player card into the DiscardPile
	 * 
	 * @param player
	 *            The Player
	 * @param card
	 *            The Card
	 * @deprecated
	 * 
	 */
	public void discardCard(int player, Card card) {
		players.get(player).removeCard(card);
		discardPile.addCard(card);
	}

	/**
	 * Get the place of a Player on the table
	 * 
	 * @param player
	 *            The Player
	 * @return The place of the Player on the table
	 */
	public int getPlace(Player player) {
		if (player != null) {
			for (Entry<Integer, Player> e : players.entrySet()) {
				if (player.getId() == e.getValue().getId())
					return e.getKey();
			}
		}
		return -1;
	}

	/**
	 * Switch two Players on the table
	 * 
	 * @param p1
	 *            The first Player
	 * @param p2
	 *            The Second Player
	 */
	public void switchPlayers(Player p1, Player p2) {
		int p1Place = getPlace(p1);
		int p2Place = getPlace(p2);
		players.remove(p1Place);
		players.remove(p2Place);
		if (p2 != null)
			addPlayer(p1Place, p2);
		if (p1 != null)
			addPlayer(p2Place, p1);
	}

	/**
	 * Add a Player to the first empty place on the board
	 * 
	 * @param p
	 *            The Player
	 */
	public void addPlayerToTheFirstEmptyPlace(Player p) {
		for (int i = 0; i < NB_PLAYER_MAX; i++) {
			if (players.get(i) == null) {
				players.put(i, p);
				break;
			}
		}
	}

	/**
	 * Move a Player on the board at a the place given in parameters
	 * 
	 * @param player
	 *            The Player
	 * @param index
	 *            The place
	 */
	public void movePlayerTo(Player player, int index) {
		int i = (index >= NB_PLAYER_MAX) ? index - (NB_PLAYER_MAX) : index;
		players.remove(getPlace(player));
		players.put(i, player);
	}

}
