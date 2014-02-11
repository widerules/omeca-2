package com.ensibs.omeca.controller;

import java.util.ArrayList;
import java.util.Hashtable;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Gallery;
import android.widget.RelativeLayout;

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
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class OmecaHandler extends Handler {

	public static final int CONNEXION = 0;
	public static final int DECONNEXION = 1;
	public static final int AKNOWLEGMENT_CONNECTION_ACTION = 2;
	public static final int SWITCH_PLAYERS_ACTION = 3;
	public static final int RETURN_CARD = 4;
	public static final int MOVE_CARD = 5;
	public static final int GIVETO = 6;
	public static final int AUTOMATIC_DRAW_ACTION = 7;
	public static final int SHUFFLE = 8;

	public OmecaHandler(Looper looper) {
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
		case DECONNEXION: {
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			boardView.getDiscardPileView().updateView();
			boardView.updatePlayers();
		}
			break;

		case AKNOWLEGMENT_CONNECTION_ACTION: {
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			boardView.getDrawPileView().setDrawpile(
					ActionController.board.getDrawPile());
			boardView.getDiscardPileView().setDiscardPile(
					ActionController.board.getDiscardPile());
			boardView.getDiscardPileView().updateView();
			boardView.getDrawPileView().updateView();
			boardView.updatePlayers();
		}
			break;
		case SWITCH_PLAYERS_ACTION: {
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			boardView.updatePlayers();
		}
			break;
		case AUTOMATIC_DRAW_ACTION: {
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			Bundle data = msg.getData();
			boardView.runDistrib(data.getInt("From"), data.getInt("Number"));
		}
			break;
		case RETURN_CARD: {
			Bundle data = msg.getData();
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			if (data.getString("Source").equals("DrawPileView")) {
				DrawPileView pileView = boardView.getDrawPileView();
				pileView.getDrawpile()
						.getCards()
						.get(pileView.getDrawpile().getNumberOfCards() - 1)
						.setFaceUp(
								!pileView
										.getDrawpile()
										.getCards()
										.get(pileView.getDrawpile()
												.getNumberOfCards() - 1)
										.isFaceUp());
				pileView.updateView();
			} else if (data.getString("Source").equals("BoardView")) {
				int value = data.getInt("Value");
				String color = data.getString("Color");
				for (int i = 0; i < boardView.getChildCount(); i++) {
					if (boardView.getChildAt(i) instanceof CardView) {
						CardView card = (CardView) boardView.getChildAt(i);
						if (card.getCard().getValue() == value
								&& card.getCard().getColor().equals(color)) {
							card.turnCard();
							break;
						}
					}
				}
			} else if (data.getString("Source").equals("DiscardPileView")) {
				DiscardPileView discardView = boardView.getDiscardPileView();
				discardView
						.getDiscardPile()
						.getCards()
						.get(discardView.getDiscardPile().getNumberOfCards() - 1)
						.setFaceUp(
								!discardView
										.getDiscardPile()
										.getCards()
										.get(discardView.getDiscardPile()
												.getNumberOfCards() - 1)
										.isFaceUp());
				discardView.updateView();
			}
		}
			break;
		case MOVE_CARD: {
			Bundle data = msg.getData();
			BoardView boardView = (BoardView) GameActivity.getActivity()
					.findViewById(R.id.view_board);
			if (data.getString("Source").equals("DrawPileView")) {
				DrawPileView pileView = boardView.getDrawPileView();
				Card tmp = pileView.getDrawpile().getCards()
						.get(pileView.getDrawpile().getNumberOfCards() - 1);
				pileView.getDrawpile().getCards()
						.remove(pileView.getDrawpile().getNumberOfCards() - 1);
				pileView.updateView();
				if (data.getString("Target").equals("BoardView")) {
					CardView card = new CardView(GameActivity.getActivity(),
							tmp);
					int pourcentageX = data.getInt("PX");
					int pourcentageY = data.getInt("PY");
					Log.i(WifiDirectProperty.TAG, "LeftP :" + pourcentageX
							+ " RightP :" + pourcentageY);
					MarginLayoutParams marginParams = new MarginLayoutParams(
							card.getLayoutParams());
					int left = (pourcentageX * boardView.getWidth()) / 100;
					int top = (pourcentageY * boardView.getHeight()) / 100;
					int right = (int) (((View) boardView.getParent())
							.getWidth() - left + boardView.getWidth());
					int bottom = (int) (((View) boardView.getParent())
							.getHeight() - top + boardView.getHeight());
					marginParams.setMargins(left, top, right, bottom);
					card.setLayoutParams(new RelativeLayout.LayoutParams(
							marginParams));
					Log.i(WifiDirectProperty.TAG, "Left :" + left + " Right :"
							+ right);
					boardView.addView(card);
				} else if (data.getString("Target").equals("DiscardPileView")) {
					boardView.getDiscardPileView().addView(
							new CardView(GameActivity.getActivity(), tmp));
				} else if (data.getString("Target").equals("Player")) {
					int playerId = data.getInt("IDTarget");
					if (playerId == ActionController.user.getId()) {
						ActionController.user.addCard(tmp);
						HandView handView = (HandView) GameActivity
								.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
						Gallery cards = (Gallery) GameActivity
								.getActivity()
								.findViewById(
										R.id.playerview_slider_board_cardgallery);
						SliderbarCardGallery l = (SliderbarCardGallery) cards
								.getAdapter();
						l.notifyDataSetChanged();
					} else {
						Hashtable<Integer, PlayerView> players = boardView
								.getPlayerViews();
						for (int playerPlace : players.keySet()) {
							if (players.get(playerPlace).getPlayer() != null
									&& players.get(playerPlace).getPlayer()
											.getId() == playerId) {
								players.get(playerPlace).getPlayer()
										.addCard(tmp);
								break;
							}
						}
						boardView.updatePlayers();
						HandView handView = (HandView) GameActivity
								.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
					}
				}
			} else if (data.getString("Source").equals("BoardView")) {
				// TODO : chercher la carte sur le board et la retourner
				int value = data.getInt("Value");
				String color = data.getString("Color");
				Card tmp = null;
				for (int i = 0; i < boardView.getChildCount(); i++) {
					if (boardView.getChildAt(i) instanceof CardView) {
						CardView card = (CardView) boardView.getChildAt(i);
						if (card.getCard().getValue() == value
								&& card.getCard().getColor().equals(color)) {
							tmp = card.getCard();
							boardView.removeView(card);
							break;
						}
					}
				}
				if (tmp != null) {
					tmp.setFaceUp(data.getBoolean("Face"));
					if (data.getString("Target").equals("DrawPileView")) {
						boardView.getDrawPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals(
							"DiscardPileView")) {
						boardView.getDiscardPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals("Player")) {
						int playerId = data.getInt("IDTarget");
						if (playerId == ActionController.user.getId()) {
							ActionController.user.addCard(tmp);
							HandView handView = (HandView) GameActivity
									.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
							Gallery cards = (Gallery) GameActivity
									.getActivity()
									.findViewById(
											R.id.playerview_slider_board_cardgallery);
							SliderbarCardGallery l = (SliderbarCardGallery) cards
									.getAdapter();
							l.notifyDataSetChanged();
						} else {
							Hashtable<Integer, PlayerView> players = boardView
									.getPlayerViews();
							for (int playerPlace : players.keySet()) {
								if (players.get(playerPlace).getPlayer() != null
										&& players.get(playerPlace).getPlayer()
												.getId() == playerId) {
									players.get(playerPlace).getPlayer()
											.addCard(tmp);
									break;
								}
							}
							boardView.updatePlayers();
							HandView handView = (HandView) GameActivity
									.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
						}
					}
				}
			} else if (data.getString("Source").equals("DiscardPileView")) {
				DiscardPileView discardView = boardView.getDiscardPileView();
				Card tmp = discardView
						.getDiscardPile()
						.getCards()
						.get(discardView.getDiscardPile().getNumberOfCards() - 1);
				discardView
						.getDiscardPile()
						.getCards()
						.remove(discardView.getDiscardPile().getNumberOfCards() - 1);
				discardView.updateView();
				if (data.getString("Target").equals("BoardView")) {
					CardView card = new CardView(GameActivity.getActivity(),
							tmp);
					int pourcentageX = data.getInt("PX");
					int pourcentageY = data.getInt("PY");
					Log.i(WifiDirectProperty.TAG, "LeftP :" + pourcentageX
							+ " RightP :" + pourcentageY);
					MarginLayoutParams marginParams = new MarginLayoutParams(
							card.getLayoutParams());
					int left = (pourcentageX * boardView.getWidth()) / 100;
					int top = (pourcentageY * boardView.getHeight()) / 100;
					int right = (int) (((View) boardView.getParent())
							.getWidth() - left + boardView.getWidth());
					int bottom = (int) (((View) boardView.getParent())
							.getHeight() - top + boardView.getHeight());
					marginParams.setMargins(left, top, right, bottom);
					card.setLayoutParams(new RelativeLayout.LayoutParams(
							marginParams));
					Log.i(WifiDirectProperty.TAG, "Left :" + left + " Right :"
							+ right);
					boardView.addView(card);
				} else if (data.getString("Target").equals("DrawPileView")) {
					boardView.getDrawPileView().addView(
							new CardView(GameActivity.getActivity(), tmp));
				} else if (data.getString("Target").equals("Player")) {
					int playerId = data.getInt("IDTarget");
					if (playerId == ActionController.user.getId()) {
						ActionController.user.addCard(tmp);
						HandView handView = (HandView) GameActivity
								.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
						Gallery cards = (Gallery) GameActivity
								.getActivity()
								.findViewById(
										R.id.playerview_slider_board_cardgallery);
						SliderbarCardGallery l = (SliderbarCardGallery) cards
								.getAdapter();
						l.notifyDataSetChanged();
					} else {
						Hashtable<Integer, PlayerView> players = boardView
								.getPlayerViews();
						for (int playerPlace : players.keySet()) {
							if (players.get(playerPlace).getPlayer() != null
									&& players.get(playerPlace).getPlayer()
											.getId() == playerId) {
								players.get(playerPlace).getPlayer()
										.addCard(tmp);
								break;
							}
						}
						boardView.updatePlayers();
						HandView handView = (HandView) GameActivity
								.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
					}
				}
			} else if (data.getString("Source").equals("Player")) {
				int playerId = data.getInt("IDSource");
				int value = data.getInt("Value");
				String color = data.getString("Color");
				Card tmp = null;
				Hashtable<Integer, PlayerView> players = boardView
						.getPlayerViews();
				for (int playerPlace : players.keySet()) {
					if (players.get(playerPlace).getPlayer() != null
							&& players.get(playerPlace).getPlayer().getId() == playerId) {
						Player player = players.get(playerPlace).getPlayer();
						ArrayList<Card> cards = player.getCards();
						for (int i = 0; i < cards.size(); i++) {
							if (cards.get(i).getColor().equals(color)
									&& cards.get(i).getValue() == value) {
								tmp = cards.get(i);
								cards.remove(i);
								break;
							}
						}
						break;
					}
				}
				if (tmp != null) {
					tmp.setFaceUp(data.getBoolean("Face"));
					Log.i(WifiDirectProperty.TAG, data.getBoolean("Face") + "");
					if (data.getString("Target").equals("BoardView")) {
						CardView card = new CardView(
								GameActivity.getActivity(), tmp);
						int pourcentageX = data.getInt("PX");
						int pourcentageY = data.getInt("PY");
						Log.i(WifiDirectProperty.TAG, "LeftP :" + pourcentageX
								+ " RightP :" + pourcentageY);
						MarginLayoutParams marginParams = new MarginLayoutParams(
								card.getLayoutParams());
						int left = (pourcentageX * boardView.getWidth()) / 100;
						int top = (pourcentageY * boardView.getHeight()) / 100;
						int right = (int) (((View) boardView.getParent())
								.getWidth() - left + boardView.getWidth());
						int bottom = (int) (((View) boardView.getParent())
								.getHeight() - top + boardView.getHeight());
						marginParams.setMargins(left, top, right, bottom);
						card.setLayoutParams(new RelativeLayout.LayoutParams(
								marginParams));
						Log.i(WifiDirectProperty.TAG, "Left :" + left
								+ " Right :" + right);
						boardView.addView(card);
					} else if (data.getString("Target").equals("DrawPileView")) {
						boardView.getDrawPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals(
							"DiscardPileView")) {
						boardView.getDiscardPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals("Player")) {
						int playerTarget = data.getInt("IDTarget");
						if (playerTarget == ActionController.user.getId()) {
							ActionController.user.addCard(tmp);
							HandView handView = (HandView) GameActivity
									.getActivity().findViewById(R.id.handview);
							handView.updateView(false);
							Gallery cards = (Gallery) GameActivity
									.getActivity()
									.findViewById(
											R.id.playerview_slider_board_cardgallery);
							SliderbarCardGallery l = (SliderbarCardGallery) cards
									.getAdapter();
							l.notifyDataSetChanged();
						} else {
							for (int playerPlace : players.keySet()) {
								if (players.get(playerPlace).getPlayer() != null
										&& players.get(playerPlace).getPlayer()
												.getId() == playerId) {
									players.get(playerPlace).getPlayer()
											.addCard(tmp);
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
		case GIVETO: {
			BoardView boardView = (BoardView) (GameActivity.getActivity()
					.findViewById(R.id.view_board));
			boardView.giveTo(msg.getData().getInt("playerPlace"));
		}
			break;
		case SHUFFLE: {
			BoardView boardView = (BoardView) (GameActivity.getActivity()
					.findViewById(R.id.view_board));
			boardView.getDrawPileView().setDrawpile(
					ActionController.board.getDrawPile());
			for (Card c : boardView.getDrawPileView().getDrawpile().getCards())
				Log.w("Card", c.getValue() + c.getColor() + "");
			boardView.getDrawPileView().updateView();
			CardView card = new CardView(GameActivity.getActivity(), new Card(
					1, Card.COLORS[0]));
			card.setLayoutParams(boardView.getDrawPileView().getLayoutParams());
			boardView.addView(card);
			DisplayMetrics metrics = GameActivity.getActivity()
					.getApplicationContext().getResources().getDisplayMetrics();
			int weight = (int) ((metrics.heightPixels / CardView.SIZE) / CardView.RATIO);
			ObjectAnimator anim = new ObjectAnimator().ofFloat(card,
					"translationX", card.getX(), card.getX() - (weight / 2),
					card.getX());
			anim.setDuration(100);
			anim.setRepeatCount(5);
			anim.addListener(new ShuffleAnimationListener(card));
			anim.start();

		}
			break;
		default:
			break;
		}
	}
}
