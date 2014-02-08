package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Card;

public class MoveCardAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String src;
	private int idSource;
	private String target;
	private int idTarget;
	private Card card;
	
	public MoveCardAction(String source, String target, Card card){
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
	}
	
	public MoveCardAction(String source, int idSource, Card card, String target){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = -1;
		this.card = card;
	}
	
	public MoveCardAction(String source, Card card, String target, int idTarger){
		this.src = source;
		this.idSource = -1;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
	}
	
	public MoveCardAction(String source, int idSource, String target, int idTarger, Card card){
		this.src = source;
		this.idSource = idSource;
		this.target = target;
		this.idTarget = idTarger;
		this.card = card;
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
}
