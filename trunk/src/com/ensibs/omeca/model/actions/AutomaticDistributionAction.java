package com.ensibs.omeca.model.actions;

import android.os.Bundle;
import android.os.Message;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.OmecaHandler;

/**
 * Event to launch the automatic distribution
 * 
 * @author OMECA 2.0 Team (Raphael GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class AutomaticDistributionAction implements Action {

	private static final long serialVersionUID = 1L;
	private int place;
	private int dealNumber;

	/**
	 * Constructor
	 * 
	 * @param place
	 *            Place the distribution begins from
	 * @param dealNumber
	 *            Number of cards to distribute
	 */
	public AutomaticDistributionAction(int place, int dealNumber) {
		this.place = place;
		this.dealNumber = dealNumber;
	}

	/**
	 * Getter on place
	 * 
	 * @return place
	 */
	public int getPlace() {
		return place;
	}

	/**
	 * Getter on dealNumber
	 * 
	 * @return dealNumber
	 */
	public int getDealNumber() {
		return dealNumber;
	}

	/**
	 * Execute the automatic distribution action
	 */
	@Override
	public void execute() {
		Message msg = GameActivity.getActivity().getOmecaHandler()
				.obtainMessage();
		msg.what = OmecaHandler.AUTOMATIC_DRAW_ACTION;
		Bundle dataMessage = new Bundle();
		dataMessage.putInt("From", getPlace());
		dataMessage.putInt("Number", getDealNumber());
		msg.setData(dataMessage);
		GameActivity.getActivity().getOmecaHandler().sendMessage(msg);

	}

}
