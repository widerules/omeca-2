package com.ensibs.omeca.model.actions;

import java.io.Serializable;

/**
 * Event to launch the automatic distribution
 * @author Nicolas
 *
 */
public class AutomaticDistributionAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int place;
	private int dealNumber;
	
	/**
	 * Constructor
	 * @param place Place from
	 * @param dealNumber Number of cards to distribute
	 */
	public AutomaticDistributionAction(int place, int dealNumber) {
		this.place = place;
		this.dealNumber = dealNumber;
	}

	/**
	 * Getter on place
	 * @return place
	 */
	public int getPlace() {
		return place;
	}

	/**
	 * Getter on dealNumber
	 * @return dealNumber
	 */
	public int getDealNumber() {
		return dealNumber;
	}
	
	
}
