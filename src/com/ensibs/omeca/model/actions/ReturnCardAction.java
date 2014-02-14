package com.ensibs.omeca.model.actions;

import android.os.Bundle;
import android.os.Message;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;

public class ReturnCardAction implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String src;
	private Card card;

	public ReturnCardAction(String source, Card card) {
		this.src = source;
		this.card = card;
	}

	public String getSrc() {
		return src;
	}

	public Card getCard() {
		return card;
	}

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