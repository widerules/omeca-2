package com.ensibs.omeca.model.entities;

import java.io.InputStream;
import java.io.Serializable;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.util.Log;

/**
 * Model for the Card entity
 * 
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 * 
 */
public class Card implements Serializable {
	/**
	 * Card values
	 */
	public static int[] VALUES;
	
	/**
	 * Card colors
	 */
	public static String[] COLORS;
	
	/**
	 * Jokers
	 */
	public static String[] JOKERS;
	
	/**
	 * Card's back
	 */
	public static String CARDBACK;
	
	private static final long serialVersionUID = 1L;
	

	private int value;
	private String color;
	private boolean isFaceUp;

	/**
	 * Constructor
	 * 
	 * @param val
	 *            The value of the Card
	 * @param col
	 *            The color of the Card
	 */
	public Card(int val, String col) {
		this.value = val;
		this.color = col;
		this.isFaceUp = false;
	}

	/**
	 * Returns the Card value
	 * 
	 * @return value The Card value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the Card value
	 * 
	 * @param value
	 *            The Card value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the color of the Card
	 * 
	 * @return color The color of the Card
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color of the Card
	 * 
	 * @param color
	 *            The color of the Card
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Load Cards's config from a config file given in parameters to initialize
	 * colors, values, cards'skins... This method have to be called at the
	 * creation of the first Activity
	 * 
	 * @param in
	 *            The config file
	 */
	public static void loadConfig(InputStream in) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(in);
			Element rootNode = document.getRootElement();
			if (rootNode == null)
				throw new Exception(
						"Error while parsing config file. (Empty file)");

			Element colors = rootNode.getChild("colors");
			Element cards = rootNode.getChild("cards");
			Element jokers = rootNode.getChild("jokers");
			Element cardback = rootNode.getChild("cardback");

			if (colors == null || cards == null || jokers == null)
				throw new Exception(
						"Error while parsing config file. (No cards or values)");

			Element node = null;

			VALUES = new int[cards.getChildren().size()];
			COLORS = new String[colors.getChildren().size()];
			JOKERS = new String[jokers.getChildren().size()];
			CARDBACK = cardback.getAttributeValue("name");

			for (int i = 0; i < colors.getChildren().size(); i++) {
				node = (Element) colors.getChildren().get(i);
				COLORS[i] = node.getAttributeValue("name");

			}

			for (int i = 0; i < cards.getChildren().size(); i++) {
				node = (Element) cards.getChildren().get(i);
				VALUES[i] = Integer.parseInt(node.getAttributeValue("value"));
			}

			for (int i = 0; i < jokers.getChildren().size(); i++) {
				node = (Element) jokers.getChildren().get(i);
				JOKERS[i] = node.getAttributeValue("name");
			}

			in.close();

		} catch (Exception e) {
			Log.w("Card", e.getMessage());
		}
	}

	/**
	 * Returns a String representation of the Card config
	 * 
	 * @return A Card config String representation
	 */
	public static String getCardsConfig() {
		String ret = "";
		ret += "Colors : ";
		for (String col : COLORS)
			ret += col + " ";
		ret += "\n Values : ";
		for (int val : VALUES)
			ret += val + " ";
		return ret;
	}

	/**
	 * Returns the number of Cards possibly available
	 * 
	 * @return The number of Cards
	 */
	public static int getNumberOfCards() {
		return VALUES.length * COLORS.length;
	}

	/**
	 * Returns true if the Card is face up, false else
	 * 
	 * @return True if the Card is face up, false else
	 */
	public boolean isFaceUp() {
		return isFaceUp;
	}

	/**
	 * Set the Card faced up or not
	 * 
	 * @param isFaceUp
	 *            The boolean to set the Card faced up or not
	 */
	public void setFaceUp(boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}

}
