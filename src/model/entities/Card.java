package model.entities;

import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import android.util.Log;

public class Card{
	//private static final String CONFIG_PATH = "/config/config.xml";
	public static int[] VALUES;
	public static String[] COLORS;
	public static String[] JOKERS;
	public static String DECK_PATH;
	
	private int value;
	private String color;
	
	public Card(int val, String col){
		this.value = val;
		this.color = col;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public static void loadConfig(InputStream in){
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(in);
			Element rootNode = document.getRootElement();
			if(rootNode == null)
				throw new Exception("Error while parsing config file. (Empty file)");
			
			Element colors = rootNode.getChild("colors");
			Element cards = rootNode.getChild("cards");
			Element jokers = rootNode.getChild("jokers");
			
			if(colors == null || cards == null || jokers == null)
				throw new Exception("Error while parsing config file. (No cards or values)");
			
			Element node = null;
			
			DECK_PATH = rootNode.getAttributeValue("path");
			VALUES = new int[cards.getChildren().size()];
			COLORS = new String[colors.getChildren().size()];
			JOKERS = new String[jokers.getChildren().size()];
			
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
	 
		  } catch (Exception e){
			  Log.w("Card", e.getMessage());
		  }
	}
	
	public static String getCardsConfig(){
		String ret = "";
		ret +="Deck path : " +DECK_PATH+"\n Colors : ";
		for(String col : COLORS)
			ret +=col+" ";
		ret +="\n Values : ";
		for(int val : VALUES)
			ret +=val+" ";
		return ret;
	}
	
	public static int getNumberOfCards(){
		return VALUES.length*COLORS.length;
	}
	
}
