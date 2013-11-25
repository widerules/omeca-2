package com.ensibs.omeca;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;


public class ControlerView {
	public MainActivity maActivity;
	public Board board;
	public int myId;
	public ControlerView(MainActivity ma) {
		this.maActivity= ma;
		board = new Board();
		//TODO set the userID 
		myId= 1;
		Player p = new Player();
		//board.addPlayer(0, p);
	}

	public void dealCard(int nbToDeal){
		board.setCardsToDeal(nbToDeal);
		board.dealCardsAutomatically(myId);
	}
	
	
}
