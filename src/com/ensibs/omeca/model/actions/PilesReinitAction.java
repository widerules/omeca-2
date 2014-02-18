package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;

/**
 * Event when the discard pile's cards have to be transfered to the draw pile
 * 
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class PilesReinitAction implements Action {

	private static final long serialVersionUID = 1L;

	/**
	 * Execute the cards transfert from the DiscardPile to the DrawPile
	 */
	@Override
	public void execute() {
		Card[] cards = new Card[GA.board.getDiscardPile()
				.getCards().size()];
		int i = cards.length - 1;
		for (Card c : GA.board.getDiscardPile().getCards()) {
			cards[i] = c;
			i--;
		}
		for (Card c : cards) {
			c.setFaceUp(false);
			GA.board.getDrawPile().getCards().add(0, c);
		}
		GameActivity.getActivity().getOmecaHandler()
				.sendEmptyMessage(OmecaHandler.PILES_REINIT);
	}

}
