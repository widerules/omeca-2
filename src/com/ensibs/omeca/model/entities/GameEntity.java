package com.ensibs.omeca.model.entities;

import java.util.ArrayList;
import java.util.Random;

public abstract class GameEntity {
	protected ArrayList<Card> cards;
	
	public GameEntity(){
		cards = new ArrayList<Card>();
	}
	
	public void shuffle(){
        Random random = new Random();
        int r;
        for(int i=0 ; i<cards.size() ; i++){
            r = random.nextInt(cards.size());
            moveCard(cards.get(i), r);
        }
	}
	
	public void moveCard(Card card, int index){
		cards.remove(card);
        cards.add(index, card);
	}
	
	public boolean addCard(Card card){
		return cards.add(card);
	}
	
	public boolean removeCard(Card card){
		return cards.remove(card);
	}
	
	public int getNumberOfCards(){
		return cards.size();
	}
}
