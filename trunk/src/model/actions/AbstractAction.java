package model.actions;

import model.entities.Board;

public abstract class AbstractAction implements ActionEvent{
	protected Board board;
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return this.board;
	}
}
