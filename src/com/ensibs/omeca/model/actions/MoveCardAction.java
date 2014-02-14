package com.ensibs.omeca.model.actions;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class MoveCardAction implements Action{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String src;
	private int idSource;
	private String target;
	private int idTarget;
	private Card card;
	private int pourcentageX;
	private int pourcentageY;
	
	public MoveCardAction(String source, String target, Card card){
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;	
	}
	
	public MoveCardAction(String source, int idSource, Card card, String target){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}
	
	public MoveCardAction(String source, Card card, String target, int idTarger){
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}
	
	public MoveCardAction(String source, int idSource, String target, int idTarger, Card card){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}

	public String getSrc() {
		return src;
	}

	public int getIdSource() {
		return idSource;
	}

	public String getTarget() {
		return target;
	}

	public int getIdTarget() {
		return idTarget;
	}

	public Card getCard() {
		return card;
	}

	public int getPourcentageX() {
		return pourcentageX;
	}

	public void setPourcentageX(int pourcentageX) {
		this.pourcentageX = pourcentageX;
	}

	public int getPourcentageY() {
		return pourcentageY;
	}

	public void setPourcentageY(int pourcentageY) {
		this.pourcentageY = pourcentageY;
	}

	@Override
	public void execute() {
		Log.i(WifiDirectProperty.TAG, "Carte deplace");
		Message msg = GameActivity.getActivity().getOmecaHandler().obtainMessage();
		msg.what = OmecaHandler.MOVE_CARD;
		Bundle dataMessage = new Bundle();
		dataMessage.putString("Source", getSrc());
		if (getIdSource() != -1)
			dataMessage
					.putInt("IDSource", getIdSource());
		dataMessage.putString("Target",  getTarget());
		if (getIdTarget() != -1)
			dataMessage
					.putInt("IDTarget", getIdTarget());
		dataMessage
				.putInt("Value", getCard().getValue());
		dataMessage.putString("Color", getCard()
				.getColor());
		if (getPourcentageX() != -1)
			dataMessage.putInt("PX", getPourcentageX());
		if (getPourcentageY() != -1)
			dataMessage.putInt("PY", getPourcentageY());
		dataMessage.putBoolean("Face", getCard()
				.isFaceUp());
		msg.setData(dataMessage);
		GameActivity.getActivity().getOmecaHandler().sendMessage(msg);
	}
}
