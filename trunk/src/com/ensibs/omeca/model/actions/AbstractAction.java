package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.model.entities.Board;

public abstract class AbstractAction implements ActionEvent{
	protected Board board;
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return this.board;
	}
}
