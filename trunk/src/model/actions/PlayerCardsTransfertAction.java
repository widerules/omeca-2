package model.actions;

import model.entities.Card;

public class PlayerCardsTransfertAction extends AbstractAction{
	int source;
	int target;
	Card card;
	
	public PlayerCardsTransfertAction(int source, int target, Card card) {
		this.source = source;
		this.target = target;
		this.card = card;
	}
	
	@Override
	public Event getEvent() {
		return Event.ACTION;
	}

	@Override
	public Object getData() {
		return this;
	}

	@Override
	public void executeAction() {
		//Do model & view update
		
	}
	


}
