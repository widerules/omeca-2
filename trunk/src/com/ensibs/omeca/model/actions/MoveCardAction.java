package com.ensibs.omeca.model.actions;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.controller.OmecaHandler;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

/**
 * Event when a card has been moved
 * 
 * @author Nicolas
 * 
 */
public class MoveCardAction implements Action {

	private static final long serialVersionUID = 1L;

	private String src;
	private int idSource;
	private String target;
	private int idTarget;
	private Card card;
	private int pourcentageX;
	private int pourcentageY;

	/**
	 * Constructor 1
	 * 
	 * @param source
	 *            The source of the movement
	 * @param target
	 *            The target of the movement
	 * @param card
	 *            The card which is moved
	 */
	public MoveCardAction(String source, String target, Card card) {
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}

	/**
	 * Constructor 2
	 * 
	 * @param source
	 *            The source of the movement
	 * @param idSource
	 *            The source id
	 * @param card
	 *            The card which is moved
	 * @param target
	 *            The target of the movement
	 */
	public MoveCardAction(String source, int idSource, Card card, String target) {
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}

	/**
	 * Constructor 3
	 * 
	 * @param source
	 *            The source of the movement
	 * @param card
	 *            The card which is moved
	 * @param target
	 *            The target of the movement
	 * @param idTarget
	 *            The target id
	 */
	public MoveCardAction(String source, Card card, String target, int idTarget) {
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = idTarget;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
	}

	/**
	 * 
	 * @param source
	 *            The source of the movement
	 * @param idSource
	 *            The source id
	 * @param target
	 *            The target of the movement
	 * @param idTarget
	 *            The target id
	 * @param card
	 *            The card which is moved
	 */
	public MoveCardAction(String source, int idSource, String target,
			int idTarget, Card card) {
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = idTarget;
		this.card = card;
		this.pourcentageX = -1;
		this.pourcentageY = -1;
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
	 * Getter on idSource
	 * 
	 * @return idSource
	 */
	public int getIdSource() {
		return idSource;
	}

	/**
	 * Getter on target
	 * 
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Getter on idTarget
	 * 
	 * @return idTarget
	 */
	public int getIdTarget() {
		return idTarget;
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
	 * Getter on pourcentageX
	 * 
	 * @return pourcentageX
	 */
	public int getPourcentageX() {
		return pourcentageX;
	}

	/**
	 * Setter on poucentageX
	 * 
	 * @param pourcentageX
	 *            The new X pourcentage
	 */
	public void setPourcentageX(int pourcentageX) {
		this.pourcentageX = pourcentageX;
	}

	/**
	 * Getter on pourcentageY
	 * 
	 * @return pourcentageY
	 */
	public int getPourcentageY() {
		return pourcentageY;
	}

	/**
	 * Setter on pourcentageY
	 * 
	 * @param pourcentageX
	 *            The new Y pourcentage
	 */
	public void setPourcentageY(int pourcentageY) {
		this.pourcentageY = pourcentageY;
	}

	/**
	 * Execute the Card move action
	 */
	@Override
	public void execute() {
		Log.i(WifiDirectProperty.TAG, "Carte deplace");
		Message msg = GameActivity.getActivity().getOmecaHandler()
				.obtainMessage();
		msg.what = OmecaHandler.MOVE_CARD;
		Bundle dataMessage = new Bundle();
		dataMessage.putString("Source", getSrc());
		if (getIdSource() != -1)
			dataMessage.putInt("IDSource", getIdSource());
		dataMessage.putString("Target", getTarget());
		if (getIdTarget() != -1)
			dataMessage.putInt("IDTarget", getIdTarget());
		dataMessage.putInt("Value", getCard().getValue());
		dataMessage.putString("Color", getCard().getColor());
		if (getPourcentageX() != -1)
			dataMessage.putInt("PX", getPourcentageX());
		if (getPourcentageY() != -1)
			dataMessage.putInt("PY", getPourcentageY());
		dataMessage.putBoolean("Face", getCard().isFaceUp());
		msg.setData(dataMessage);
		GameActivity.getActivity().getOmecaHandler().sendMessage(msg);
	}
}
