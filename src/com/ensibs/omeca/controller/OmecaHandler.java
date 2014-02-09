package com.ensibs.omeca.controller;

import java.util.ArrayList;
import java.util.Hashtable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Gallery;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.SliderbarCardGallery;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.CardView;
import com.ensibs.omeca.view.DiscardPileView;
import com.ensibs.omeca.view.DrawPileView;
import com.ensibs.omeca.view.HandView;
import com.ensibs.omeca.view.PlayerView;

public class OmecaHandler extends Handler {

	public static final int CONNEXION = 0;
	public static final int DECONNEXION = 1;
	public static final int AKNOWLEGMENT_CONNECTION_ACTION = 2;
	public static final int SWITCH_PLAYERS_ACTION = 3;
	public static final int RETURN_CARD = 4;

	public static final int MOVE_CARD = 5;
	
	public OmecaHandler(Looper looper){
		super(looper);
	}

	/**
	 * Message msgObj = handler.obtainMessage(); Bundle b = new Bundle();
	 * b.putString("message", msg); msgObj.setData(b);
	 * handler.sendMessage(msgObj);
	 */

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case DECONNEXION:{
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			boardView.getDiscardPileView().updateView();
			boardView.updatePlayers();
		}
			break;
		case AKNOWLEGMENT_CONNECTION_ACTION:
		{
			BoardView boardView = (BoardView) GameActivity.getActivity()
			.findViewById(R.id.view_board);
			boardView.getDrawPileView().setDrawpile(ActionController.board.getDrawPile());
			boardView.getDiscardPileView().setDiscardPile(ActionController.board.getDiscardPile());
			boardView.getDiscardPileView().updateView();
			boardView.getDrawPileView().updateView();
			boardView.updatePlayers();
		}
			break;
		case SWITCH_PLAYERS_ACTION:
		{
			BoardView boardView = (BoardView) GameActivity.getActivity()
			.findViewById(R.id.view_board);
			boardView.updatePlayers();
		}
			break;
		case RETURN_CARD:
		{
			Bundle data = msg.getData();
			BoardView boardView = (BoardView)GameActivity.getActivity().findViewById(R.id.view_board);
			if(data.getString("Source").equals("DrawPileView")){
				DrawPileView pileView = boardView.getDrawPileView();
				pileView.getDrawpile().getCards().get(pileView.getDrawpile().getNumberOfCards()-1).setFaceUp(!pileView.getDrawpile().getCards().get(pileView.getDrawpile().getNumberOfCards()-1).isFaceUp());
				pileView.updateView();
			}else if(data.getString("Source").equals("BoardView")){
				int value = data.getInt("Value");
				String color = data.getString("Color");
				for(int i=0;i<boardView.getChildCount();i++){
				       if(boardView.getChildAt(i) instanceof CardView){
				    	   CardView card = (CardView) boardView.getChildAt(i);
				    	   if(card.getCard().getValue() == value && card.getCard().getColor().equals(color)){
				    		   card.turnCard();
				    		   break;
				    	   }
				       }
				  }
			}else if(data.getString("Source").equals("DiscardPileView")){
				DiscardPileView discardView = boardView.getDiscardPileView();
				discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).setFaceUp(!discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).isFaceUp());
				discardView.updateView();
			}
		}
			break;
		case MOVE_CARD:
		{
			Bundle data = msg.getData();
			BoardView boardView = (BoardView)GameActivity.getActivity().findViewById(R.id.view_board);
			if(data.getString("Source").equals("DrawPileView")){
				DrawPileView pileView = boardView.getDrawPileView();
				Card tmp = pileView.getDrawpile().getCards().get(pileView.getDrawpile().getNumberOfCards()-1);
				pileView.getDrawpile().getCards().remove(pileView.getDrawpile().getNumberOfCards()-1);
				pileView.updateView();
				if(data.getString("Target").equals("BoardView")){
					boardView.addView(new CardView(GameActivity.getActivity(),tmp));
				}else if(data.getString("Target").equals("DiscardPileView")){
					boardView.getDiscardPileView().addView(new CardView(GameActivity.getActivity(),tmp));
				}else if(data.getString("Target").equals("Player")){
					int playerId = data.getInt("IDTarget");
					if(playerId == ActionController.user.getId()){
						ActionController.user.addCard(tmp);
						HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
						Gallery cards = (Gallery) GameActivity.getActivity().findViewById(R.id.playerview_slider_board_cardgallery);
						SliderbarCardGallery l = (SliderbarCardGallery)cards.getAdapter();
				        l.notifyDataSetChanged();
					}else{
						Hashtable<Integer, PlayerView> players = boardView.getPlayerViews();
						for(int playerPlace : players.keySet()){
							if(players.get(playerPlace).getPlayer() != null && players.get(playerPlace).getPlayer().getId() == playerId){
								players.get(playerPlace).getPlayer().addCard(tmp);
								break;
							}
						}
						boardView.updatePlayers();
						HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
					}
				}
			}else if(data.getString("Source").equals("BoardView")){
				//TODO : chercher la carte sur le board et la retourner
				int value = data.getInt("Value");
				String color = data.getString("Color");
				Card tmp = null;
				for(int i=0;i<boardView.getChildCount();i++){
				       if(boardView.getChildAt(i) instanceof CardView){
				    	   CardView card = (CardView) boardView.getChildAt(i);
				    	   if(card.getCard().getValue() == value && card.getCard().getColor().equals(color)){
				    		  tmp = card.getCard();
				    		  boardView.removeView(card);
				    		  break;
				    	   }
				       }
				  }
				if(tmp != null){
					if(data.getString("Target").equals("DrawPileView")){
						boardView.getDrawPileView().addView(new CardView(GameActivity.getActivity(),tmp));
					}else if(data.getString("Target").equals("DiscardPileView")){
						boardView.getDiscardPileView().addView(new CardView(GameActivity.getActivity(),tmp));
					}else if(data.getString("Target").equals("Player")){
						int playerId = data.getInt("IDTarget");
						if(playerId == ActionController.user.getId()){
							ActionController.user.addCard(tmp);
							HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
							Gallery cards = (Gallery) GameActivity.getActivity().findViewById(R.id.playerview_slider_board_cardgallery);
							SliderbarCardGallery l = (SliderbarCardGallery)cards.getAdapter();
					        l.notifyDataSetChanged();
						}else{
							Hashtable<Integer, PlayerView> players = boardView.getPlayerViews();
							for(int playerPlace : players.keySet()){
								if(players.get(playerPlace).getPlayer() != null && players.get(playerPlace).getPlayer().getId() == playerId){
									players.get(playerPlace).getPlayer().addCard(tmp);
									break;
								}
							}
							boardView.updatePlayers();
							HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
						}
					}
				}
			}else if(data.getString("Source").equals("DiscardPileView")){
				DiscardPileView discardView = boardView.getDiscardPileView();
				Card tmp = discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1);
				discardView.getDiscardPile().getCards().remove(discardView.getDiscardPile().getNumberOfCards()-1);
				discardView.updateView();
				if(data.getString("Target").equals("BoardView")){
					boardView.addView(new CardView(GameActivity.getActivity(),tmp));
				}else if(data.getString("Target").equals("DrawPileView")){
					boardView.getDrawPileView().addView(new CardView(GameActivity.getActivity(),tmp));
				}else if(data.getString("Target").equals("Player")){
					int playerId = data.getInt("IDTarget");
					if(playerId == ActionController.user.getId()){
						ActionController.user.addCard(tmp);
						HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
						Gallery cards = (Gallery) GameActivity.getActivity().findViewById(R.id.playerview_slider_board_cardgallery);
						SliderbarCardGallery l = (SliderbarCardGallery)cards.getAdapter();
				        l.notifyDataSetChanged();
					}else{
						Hashtable<Integer, PlayerView> players = boardView.getPlayerViews();
						for(int playerPlace : players.keySet()){
							if(players.get(playerPlace).getPlayer() != null && players.get(playerPlace).getPlayer().getId() == playerId){
								players.get(playerPlace).getPlayer().addCard(tmp);
								break;
							}
						}
						boardView.updatePlayers();
						HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
					}
				}
			}else if(data.getString("Source").equals("Player")){
				int playerId = data.getInt("IDSource");
				int value = data.getInt("Value");
				String color = data.getString("Color");
				Card tmp = null;
				Hashtable<Integer, PlayerView> players = boardView.getPlayerViews();
				for(int playerPlace : players.keySet()){
					if(players.get(playerPlace).getPlayer() != null && players.get(playerPlace).getPlayer().getId() == playerId){
						Player player = players.get(playerPlace).getPlayer();
						ArrayList<Card> cards = player.getCards();
						for(int i = 0; i<cards.size();i++){
							if(cards.get(i).getColor().equals(color) && cards.get(i).getValue() == value){
								tmp = cards.get(i);
								cards.remove(i);
								break;
							}
						}
						break;
					}
				}
				if(tmp != null){
					if(data.getString("Target").equals("BoardView")){
						boardView.addView(new CardView(GameActivity.getActivity(),tmp));
					}else if(data.getString("Target").equals("DrawPileView")){
						boardView.getDrawPileView().addView(new CardView(GameActivity.getActivity(),tmp));
					}else if(data.getString("Target").equals("DiscardPileView")){
						boardView.getDiscardPileView().addView(new CardView(GameActivity.getActivity(),tmp));
					}else if(data.getString("Target").equals("Player")){
						int playerTarget = data.getInt("IDTarget");
						if(playerTarget == ActionController.user.getId()){
							ActionController.user.addCard(tmp);
							HandView handView = (HandView)GameActivity.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
							Gallery cards = (Gallery) GameActivity.getActivity().findViewById(R.id.playerview_slider_board_cardgallery);
							SliderbarCardGallery l = (SliderbarCardGallery)cards.getAdapter();
					        l.notifyDataSetChanged();
						}else{
							for(int playerPlace : players.keySet()){
								if(players.get(playerPlace).getPlayer() != null && players.get(playerPlace).getPlayer().getId() == playerId){
									players.get(playerPlace).getPlayer().addCard(tmp);
									break;
								}
							}
						}
					}
					boardView.updatePlayers();
				}
			}
		}
			break;
		default:
			break;
		}
	}
}