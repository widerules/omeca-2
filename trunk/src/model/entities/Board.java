package model.entities;

import java.util.Hashtable;

public class Board extends GameEntity{
	int cardsToDeal;
	Hashtable<Integer, Player> players;
	DrawPile drawPile;
	DiscardPile discardPile;
	
	public Board(){
		this.players = new Hashtable<Integer, Player>();
		this.drawPile = new DrawPile();
		this.discardPile = new DiscardPile();
		cardsToDeal = 0;
	}
	
	public int getCardsToDeal() {
		return cardsToDeal;
	}

	public void setCardsToDeal(int cardsToDeal) {
		this.cardsToDeal = cardsToDeal;
	}

	public void dealCardsAutomatically(int from){
		if(cardsToDeal*players.size() <= Card.getNumberOfCards()){
			int position = from+1;
			for(int i=0;i<cardsToDeal;i++){
				for(int j=0;j<players.size();j++){
					if(position >= players.size())
						position = 0;
					drawCard(position);
					position++;
				}
			}
		}
		
	}
	
	public void addPlayer(int place, Player p){
		if(!players.containsKey(place))
			players.put(place, p);
	}
	
	public void removePlayer(int place){
		players.remove(place);
	}
	
	public void drawCard(int player){
		Card topCard = drawPile.cards.get(0);
		players.get(player).addCard(topCard);
		drawPile.removeCard(topCard);
	}
	
	public void discardCard(int player, Card card){
		players.get(player).removeCard(card);
		discardPile.addCard(card);
	}

}
