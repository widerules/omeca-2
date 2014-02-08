package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Card;

public class MoveCardAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String src;
	private String idSource;
	private String target;
	private String idTarget;
	private Card card;
	
	public MoveCardAction(String source, String target, Card card){
		this.src = source;
		this.idSource = null;
		this.target = target;
		this.idTarget = null;
		this.card = card;
	}
	
	public MoveCardAction(String source, String idSource, Card card, String target){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = null;
		this.card = card;
	}
	
	public MoveCardAction(String source, Card card, String target, String idTarger){
		this.src = source;
		this.idSource = null;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
	}
	
	public MoveCardAction(String source, String idSource, String target, String idTarger, Card card){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
	}

	public String getSrc() {
		return src;
	}

	public String getIdSource() {
		return idSource;
	}

	public String getTarget() {
		return target;
	}

	public String getIdTarget() {
		return idTarget;
	}

	public Card getCard() {
		return card;
	}
}
