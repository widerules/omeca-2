package com.ensibs.omeca.model.actions;

import com.ensibs.omeca.model.entities.Board;

public abstract class AbstractAction implements ActionEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1656992801053235921L;
	
	protected Board board;
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return this.board;
	}
}
