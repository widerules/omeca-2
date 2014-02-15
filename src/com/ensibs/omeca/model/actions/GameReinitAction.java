package com.ensibs.omeca.model.actions;

import java.util.Map.Entry;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.ActionController;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Card[] cards;

	/**
	 * Constructor
	 * 
	 * @param cards
	 *            draw pile's cards
	 */
	public GameReinitAction(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * Getter on board
	 * 
	 * @return board
	 */
	public Card[] getCards() {
		return cards;
	}

	@Override
	public void execute() {
		ActionController.user.getCards().clear();
		for (Entry<Integer, Player> e : ActionController.board.getPlayers()
				.entrySet()) {
			e.getValue().getCards().clear();
		}
		ActionController.board.getDiscardPile().getCards().clear();
		ActionController.board.getDrawPile().getCards().clear();
		for(Card c : getCards())
			ActionController.board.getDrawPile().addCard(c);
		GameActivity.getActivity().getOmecaHandler().sendEmptyMessage(OmecaHandler.GAME_REINIT);
	}
}
