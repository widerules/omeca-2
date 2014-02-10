package com.ensibs.omeca.model.entities;

import java.util.Hashtable;
import java.util.Map.Entry;

import android.util.Log;

/**
 * Model for the board entity
 * @author Nicolas
 * 
 */
public class Board extends GameEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NB_PLAYER_MAX = 8;
	private int cardsToDeal;
	private Hashtable<Integer, Player> players;
	private DrawPile drawPile;
	private DiscardPile discardPile;
	
	/**
	 * Constructor
	 */
	public Board(){
		this.players = new Hashtable<Integer, Player>();
		this.drawPile = new DrawPile();
		this.discardPile = new DiscardPile();
		cardsToDeal = 0;
	}
	
	/**
	 * Init the drawpile with filling it with cards
	 * @param shuffle If the cards have to be shuffled
	 */
	public void initDrawPile(boolean shuffle){
		Card tmp;
		for(String c : Card.COLORS){
			for(int v : Card.VALUES){
				tmp = new Card(v, c);
				drawPile.addCard(tmp);
			}
		}
		drawPile.addCard(new Card(0, Card.JOKERS[0]));
		drawPile.addCard(new Card(0, Card.JOKERS[1]));
		if(shuffle)
			drawPile.shuffle();
	}
	
	public Hashtable<Integer, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Hashtable<Integer, Player> players) {
		this.players = players;
	}

	public DrawPile getDrawPile() {
		return drawPile;
	}

	public void setDrawPile(DrawPile drawPile) {
		this.drawPile = drawPile;
	}

	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(DiscardPile discardPile) {
		this.discardPile = discardPile;
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
	
	public int getPlace(Player player){
		if(player != null){
			for(Entry<Integer, Player> e : players.entrySet()){
				if(player.getId() == e.getValue().getId())
					return e.getKey();
			}
		}
		return -1;
	}
	
	public void switchPlayers(Player p1, Player p2){
		int p1Place = getPlace(p1);
		int p2Place = getPlace(p2);
		players.remove(p1Place);
		players.remove(p2Place);
		if(p2 != null)
			addPlayer(p1Place, p2);
		if(p1 != null)
			addPlayer(p2Place, p1);
	}

	public void addPlayerToTheFirstEmptyPlace(Player p) {
		for(int i=0 ; i<NB_PLAYER_MAX; i++){
			if(players.get(i) == null){
				players.put(i, p);
				break;
			}
		}
	}

	public void movePlayerTo(Player player, int index) {
		int i = (index >= NB_PLAYER_MAX) ? index-(NB_PLAYER_MAX) : index;
		players.remove(getPlace(player));
		players.put(i, player);
	}

}
