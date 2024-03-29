package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;

/**
 * Event when the host allows a new player to be part of the game
 * 
 * @author OMECA 2.0 Team (Raphael GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class AknowlegmentConnectionAction implements Action {

	private static final long serialVersionUID = 1L;
	private Player player;
	private Player[] players;
	private Card[] discardPileCards;
	private Card[] drawPileCards;

	/**
	 * 
	 * @param p
	 *            Player who will be added
	 * @param players
	 *            The other players
	 * @param drawPileCards
	 *            The DrawPile cards
	 * @param discardPileCards
	 *            The DiscardPile cards
	 */
	public AknowlegmentConnectionAction(Player p, Player[] players,
			Card[] drawPileCards, Card[] discardPileCards) {
		this.player = p;
		this.players = players;
		this.discardPileCards = discardPileCards;
		this.drawPileCards = drawPileCards;
	}

	/**
	 * Getter on player
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Setter on player
	 * 
	 * @param player
	 *            The new player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Getter on players
	 * 
	 * @return players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Setter on players
	 * 
	 * @param player
	 *            The new players tab
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}

	/**
	 * Getter on discardPileCards
	 * 
	 * @return discardPileCards
	 */
	public Card[] getDiscardPileCards() {
		return discardPileCards;
	}

	/**
	 * Setter on discardPileCards
	 * 
	 * @param player
	 *            The new discardPileCards tab
	 */
	public void setDiscardPileCards(Card[] discardPileCards) {
		this.discardPileCards = discardPileCards;
	}

	/**
	 * Getter on drawPileCards
	 * 
	 * @return drawPileCards
	 */
	public Card[] getDrawPileCards() {
		return drawPileCards;
	}

	/**
	 * Setter on drawPileCards
	 * 
	 * @param player
	 *            The new drawPileCards tab
	 */
	public void setDrawPileCards(Card[] drawPileCards) {
		this.drawPileCards = drawPileCards;
	}

	/**
	 * Execute the aknowlegment of a connection action
	 */
	@Override
	public void execute() {
		Player p = getPlayer();
		if (p.getMacAddress().equals(GA.user.getMacAddress())) {
			GA.user = p;
		}

		for (int i = 0; i < getPlayers().length; i++) {
			if (getPlayers()[i] != null)
				GA.board.addPlayer(i, getPlayers()[i]);
		}

		GA.board.getDrawPile().getCards().clear();
		for (Card c : drawPileCards)
			GA.board.getDrawPile().addCard(c);
		GA.board.getDiscardPile().getCards().clear();
		for (Card c : discardPileCards)
			GA.board.getDiscardPile().addCard(c);

		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.AKNOWLEGMENT_CONNECTION_ACTION);
	}
}
