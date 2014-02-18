package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;

/**
 * Event when the draw pile is shuffled
 * 
 * @author Nicolas
 * 
 */
public class ShuffleAction implements Action {

	private static final long serialVersionUID = 1L;

	private Card[] cards;

	/**
	 * Constructor
	 * 
	 * @param cards
	 *            the DrawPile cards
	 */
	public ShuffleAction(Card[] cards) {
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
	 * Execute the DrawPile shuffle action
	 */
	@Override
	public void execute() {
		DrawPile nDrawPile = new DrawPile();
		for (Card c : getCards()) {
			nDrawPile.addCard(c);
		}
		GA.board.setDrawPile(nDrawPile);
		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.SHUFFLE);

	}
}
