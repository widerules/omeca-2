package com.ensibs.omeca;

import com.ensibs.omeca.model.entities.Board;

public class ControlerView {
	public MainActivity maActivity;
	public Board board;
	public ControlerView(MainActivity ma) {
		this.maActivity= ma;
		board = new Board();
	}

	
	
}
