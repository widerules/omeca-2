package com.ensibs.omeca.model.actions;

import java.io.Serializable;

public interface Action extends Serializable{
	
	/**
	 * Execute the action depending on its type
	 */
	public void execute();
}
