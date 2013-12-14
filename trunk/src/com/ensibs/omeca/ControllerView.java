package com.ensibs.omeca;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;


public class ControllerView {
	public OmecaApplication maActivity;
	public Board board;
	public int myId;
	public ControllerView(OmecaApplication omecaApplication) {
		this.maActivity= omecaApplication;
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
