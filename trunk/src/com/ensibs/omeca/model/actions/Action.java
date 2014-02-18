package com.ensibs.omeca.model.actions;

import java.io.Serializable;

/**
 * Interface for all the types of actions which will be shared throught the
 * network
 * 
 * @author Nicolas
 * 
 */
public interface Action extends Serializable {

	/**
	 * Execute the action depending on its type
	 */
	public void execute();
}
