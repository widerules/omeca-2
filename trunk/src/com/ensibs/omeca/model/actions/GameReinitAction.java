package com.ensibs.omeca.model.actions;

import java.util.Map.Entry;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;

/**
 * Event when a game have to be reinitialized
 * 
 * @author Nicolas
 * 
 */
public class GameReinitAction implements Action {

	private static final long serialVersionUID = 1L;

	private Card[] cards;

	/**
	 * Constructor
	 * 
	 * @param cards
	 *            DrawPile's cards
	 */
	public GameReinitAction(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * Getter on cards
	 * 
	 * @return cards
	 */
	public Card[] getCards() {
		return cards;
	}

	/**
	 * Execute the game reinitialization action
	 */
	@Override
	public void execute() {
		GA.user.getCards().clear();
		for (Entry<Integer, Player> e : GA.board.getPlayers()
				.entrySet()) {
			e.getValue().getCards().clear();
		}
		GA.board.getDiscardPile().getCards().clear();
		GA.board.getDrawPile().getCards().clear();
		for (Card c : getCards())
			GA.board.getDrawPile().addCard(c);
		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.GAME_REINIT);
	}
}
