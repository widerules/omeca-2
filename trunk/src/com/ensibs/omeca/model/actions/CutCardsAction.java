package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DrawPile;

/**
 * Event when the draw pile is cut
 * @author Nicolas
 *
 */
public class CutCardsAction implements Action{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Card[] cards;
	
	/**
	 * Constructor
	 * @param cards the draw pile cards
	 */
	public CutCardsAction(Card[] cards){
		this.cards = cards;
	}

	/**
	 * Getter on cards
	 * @return cards
	 */
	public Card[] getCards() {
		return cards;
	}

	@Override
	public void execute() {
		DrawPile nDrawPile = new DrawPile();
		for (Card c : getCards()) {
			nDrawPile.addCard(c);
		}
		ActionController.board.setDrawPile(nDrawPile);
		GameActivity.getActivity().getOmecaHandler().sendEmptyMessage(OmecaHandler.CUT);
		
	}
}
