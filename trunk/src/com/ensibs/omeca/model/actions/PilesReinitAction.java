package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;

/**
 * Event when the discard pile's cards have to be transfered to the draw pile
 * 
 * @author Nicolas
 * 
 */
public class PilesReinitAction implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute() {
		Card[] cards = new Card[ActionController.board.getDiscardPile()
				.getCards().size()];
		int i = cards.length-1;
		for (Card c : ActionController.board.getDiscardPile().getCards()) {
			cards[i] = c;
			i--;
		}
		for (Card c : cards) {
			c.setFaceUp(false);
			ActionController.board.getDrawPile().getCards().add(0, c);
		}
		GameActivity.getActivity().getOmecaHandler().sendEmptyMessage(OmecaHandler.PILES_REINIT);
	}

}
