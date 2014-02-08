package com.ensibs.omeca.model.actions;

import java.io.Serializable;

import com.ensibs.omeca.model.entities.Card;

public class ReturnCardAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String src;
	private Card card;
	
	public ReturnCardAction(String source, Card card){
		this.src = source;
		this.card = card;
	}

	public String getSrc() {
		return src;
	}

	public Card getCard() {
		return card;
	}
}