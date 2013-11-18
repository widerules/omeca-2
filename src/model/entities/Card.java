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
			
			List<Element> colors = rootNode.getChild("colors").getChildren();
			List<Element> cards = rootNode.getChild("cards").getChildren();
			
			if(colors == null || cards == null)
				throw new Exception("Error while parsing config file. (No cards or values)");
			
			Element node = null;
			
			DECK_PATH = rootNode.getAttributeValue("path");
			VALUES = new int[cards.size()];
			COLORS = new String[colors.size()];
			
			
			for (int i = 0; i < colors.size(); i++) {
			   node = (Element) colors.get(i);
			   COLORS[i] = node.getAttributeValue("name");
	 
			}
			
			for (int i = 0; i < cards.size(); i++) {
				node = (Element) cards.get(i);
				VALUES[i] = Integer.parseInt(node.getAttributeValue("value"));
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
