package com.ensibs.omeca.model.actions;

import android.os.Bundle;
import android.os.Message;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;

/**
 * Event when a card has been fliped
 * 
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class ReturnCardAction implements Action {

	private static final long serialVersionUID = 1L;

	private String src;
	private Card card;

	/**
	 * Constructor
	 * 
	 * @param source
	 *            The source where the card is fliped from
	 * @param card
	 *            The card which is fliped
	 */
	public ReturnCardAction(String source, Card card) {
		this.src = source;
		this.card = card;
	}

	/**
	 * Getter on src
	 * 
	 * @return src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * Getter on card
	 * 
	 * @return card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * Execute the Card flip action
	 */
	@Override
	public void execute() {
		Message msg = GameActivity.getActivity().getOmecaHandler()
				.obtainMessage();
		msg.what = OmecaHandler.RETURN_CARD;
		Bundle dataMessage = new Bundle();
		dataMessage.putString("Source", getSrc());
		dataMessage.putInt("Value", getCard().getValue());
		dataMessage.putString("Color", getCard().getColor());
		msg.setData(dataMessage);
		GameActivity.getActivity().getOmecaHandler().sendMessage(msg);
	}

}