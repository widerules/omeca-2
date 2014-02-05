package com.ensibs.omeca.model.actions;

import java.io.Serializable;

public class DisconnectionAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idPlayer;
	
	public DisconnectionAction(int idPlayer){
		this.idPlayer = idPlayer;
	}

	public int getIdPlayer() {
		return idPlayer;
	}

}
