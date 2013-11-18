package model.actions;

import java.io.Serializable;

import model.entities.Board;

import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;

public interface ActionEvent extends Serializable, WifiDirectEvent {

	public void executeAction();
	
	public void setBoard(Board board);
	
	public Board getBoard();
}
