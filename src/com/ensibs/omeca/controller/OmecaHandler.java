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
import android.widget.TextView;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.model.entities.DiscardPile;
import com.ensibs.omeca.model.entities.Notif;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.utils.NotifPopup;
import com.ensibs.omeca.utils.NotificationTools;
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
	public static final int CUT = 9;
	public static final int PILES_REINIT = 10;
	public static final int GAME_REINIT = 11;

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
		Bundle data = msg.getData();
		BoardView boardView = (BoardView) GameActivity.getActivity()
				.findViewById(R.id.view_board);
		Hashtable<Integer, PlayerView> players = boardView.getPlayerViews();
		Notif notif = new Notif();

		switch (msg.what) {
		case DECONNEXION: {
			notif.setEvent("Un joueur s'est d�connect�");
			boardView.getDiscardPileView().updateView();
			boardView.updatePlayers();
		}
			break;

		case AKNOWLEGMENT_CONNECTION_ACTION: {
			notif.setEvent("Acceptation d'un joueur");
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
			notif.setEvent("D�placement de joueurs");
			boardView.updatePlayers();
		}
			break;
		case AUTOMATIC_DRAW_ACTION: {
			notif.setEvent("Chaque joueur re�oit " + data.getInt("Number")
					+ " carte(s)");
			boardView.runDistrib(data.getInt("From"), data.getInt("Number"));
		}
			break;
		case RETURN_CARD: {
			if (data.getString("Source").equals("DrawPileView")) {
				notif.setEvent("Une carte de la pioche a �t� retourn�e");
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
				notif.setEvent("Une carte du tapis a �t� retourn�e");
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
				notif.setEvent("Une carte d�fauss�e a �t� retourn�e");
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
			if (data.getString("Source").equals("DrawPileView")) {
				DrawPileView pileView = boardView.getDrawPileView();
				Card tmp = pileView.getDrawpile().getCards()
						.get(pileView.getDrawpile().getNumberOfCards() - 1);
				pileView.getDrawpile().getCards()
						.remove(pileView.getDrawpile().getNumberOfCards() - 1);
				pileView.updateView();

				if (data.getString("Target").equals("BoardView")) {
					notif.setEvent("Une carte a �t� mise en jeu");
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
					notif.setEvent("D�fausse d'une carte de la pioche");
					boardView.getDiscardPileView().addView(
							new CardView(GameActivity.getActivity(), tmp));
				} else if (data.getString("Target").equals("Player")) {
					int playerId = data.getInt("IDTarget");
					if (playerId == ActionController.user.getId()) {
						notif.setEvent("J'ai re�u 1 carte de la pioche");
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
						notif.setEvent("a re�u 1 carte de la pioche");
						for (int playerPlace : players.keySet()) {
							if (players.get(playerPlace).getPlayer() != null
									&& players.get(playerPlace).getPlayer()
											.getId() == playerId) {
								Player p = players.get(playerPlace).getPlayer();
								notif.setSource(p);
								p.addCard(tmp);
								break;
							}
						}
						boardView.updatePlayers();
						HandView handView = (HandView) GameActivity
								.getActivity().findViewById(R.id.handview);
						handView.updateView(false);
					}
				}
				TextView textDiscardPile = (TextView) boardView
						.findViewById(R.id.nbDiscardPileCards);
				TextView textDrawPile = (TextView) boardView
						.findViewById(R.id.nbDrawPileCards);
				textDiscardPile.setText(""
						+ boardView.getDiscardPileView().getDiscardPile()
								.getNumberOfCards());
				textDrawPile.setText(""
						+ boardView.getDrawPileView().getDrawpile()
								.getNumberOfCards());
			} else if (data.getString("Source").equals("BoardView")) {
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
						notif.setEvent("Une carte a �t� mise dans la pioche");
					} else if (data.getString("Target").equals(
							"DiscardPileView")) {
						boardView.getDiscardPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
						notif.setEvent("D�fausse d'une carte du tapis");
					} else if (data.getString("Target").equals("Player")) {
						notif.setEvent("J'ai re�u 1 carte du tapis");
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
							notif.setEvent("a re�u 1 carte du tapis");
							for (int playerPlace : players.keySet()) {
								if (players.get(playerPlace).getPlayer() != null
										&& players.get(playerPlace).getPlayer()
												.getId() == playerId) {
									Player p = players.get(playerPlace)
											.getPlayer();
									notif.setSource(p);
									p.addCard(tmp);
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
					notif.setEvent("Une carte d�fauss�e a �t� mise en jeu");
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
					notif.setEvent("Une carte d�fauss�e a �t� mise en pioche");
				} else if (data.getString("Target").equals("DrawPileView")) {
					boardView.getDrawPileView().addView(
							new CardView(GameActivity.getActivity(), tmp));
				} else if (data.getString("Target").equals("Player")) {
					notif.setEvent("J'ai re�u 1 carte d�fauss�e");
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
						notif.setEvent("a re�u 1 carte d�fauss�e");
						for (int playerPlace : players.keySet()) {
							if (players.get(playerPlace).getPlayer() != null
									&& players.get(playerPlace).getPlayer()
											.getId() == playerId) {
								Player p = players.get(playerPlace).getPlayer();
								notif.setSource(p);
								p.addCard(tmp);
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
				for (int playerPlace : players.keySet()) {
					if (players.get(playerPlace).getPlayer() != null
							&& players.get(playerPlace).getPlayer().getId() == playerId) {
						Player player = players.get(playerPlace).getPlayer();
						notif.setSource(player);
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
						notif.setEvent("a jou� 1 carte");
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
						notif.setEvent("a plac� 1 carte en pioche");
						boardView.getDrawPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals(
							"DiscardPileView")) {
						notif.setEvent("s'est d�fauss� d'une carte");
						boardView.getDiscardPileView().addView(
								new CardView(GameActivity.getActivity(), tmp));
					} else if (data.getString("Target").equals("Player")) {
						int playerTarget = data.getInt("IDTarget");
						if (playerTarget == ActionController.user.getId()) {
							notif.setEvent("m'a donn� une carte");
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
							notif.setEvent("a donn� une carte �");
							for (int playerPlace : players.keySet()) {
								if (players.get(playerPlace).getPlayer() != null
										&& players.get(playerPlace).getPlayer()
												.getId() == playerId) {
									Player p = players.get(playerPlace)
											.getPlayer();
									notif.setSource(p);
									p.addCard(tmp);
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
			boardView.giveTo(msg.getData().getInt("playerPlace"));
		}
			break;
		case SHUFFLE: {
			notif.setEvent("Les cartes ont �t� m�lang�es");
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
		case CUT: {
			if(ActionController.isSoundToggled())
	    		NotificationTools.createSoundNotification(GameActivity.getActivity().getApplicationContext(), R.drawable.cut);
	    	if(ActionController.isVibrationToggled())
	    		NotificationTools.createVibrationNotification(GameActivity.getActivity().getApplicationContext(), 1000);
			notif.setEvent("Les cartes ont �t� coup�es");
			boardView.getDrawPileView().setDrawpile(
					ActionController.board.getDrawPile());
			boardView.getDrawPileView().updateView();
		}
			break;

		case PILES_REINIT: {
			notif.setEvent("R�initialisation des piles");
			boardView.getDrawPileView().setDrawpile(
					ActionController.board.getDrawPile());
			boardView.getDrawPileView().updateView();
			boardView.getDiscardPileView().setDiscardPile(new DiscardPile());
			boardView.getDiscardPileView().updateView();
			TextView textDiscardPile = (TextView) boardView
					.findViewById(R.id.nbDiscardPileCards);
			TextView textDrawPile = (TextView) boardView
					.findViewById(R.id.nbDrawPileCards);
			textDiscardPile.setText(""
					+ boardView.getDiscardPileView().getDiscardPile()
							.getNumberOfCards());
			textDrawPile.setText(""
					+ boardView.getDrawPileView().getDrawpile()
							.getNumberOfCards());

			CardView card = new CardView(GameActivity.getActivity(), new Card(
					1, Card.COLORS[0]));
			card.setLayoutParams(boardView.getDiscardPileView().getLayoutParams());
			boardView.addView(card);
			DisplayMetrics metrics = GameActivity.getActivity()
					.getApplicationContext().getResources().getDisplayMetrics();
			
			ObjectAnimator anim = new ObjectAnimator().ofFloat(card,
					"translationX", boardView.getDrawPileView().getX()-5);
			anim.setDuration(150);
			anim.setRepeatCount(5);
			anim.addListener(new PileReinitAnimationListener(card));
			anim.start();
			if(ActionController.isSoundToggled())
	    		NotificationTools.createSoundNotification(GameActivity.getActivity().getApplicationContext(), R.drawable.shufflecard);
	    	if(ActionController.isVibrationToggled())
	    		NotificationTools.createVibrationNotification(GameActivity.getActivity().getApplicationContext(), 1000);
		}
			break;
		case GAME_REINIT: {
			notif.setEvent("R�initialisation de la partie");
			boardView.getDrawPileView().updateView();
			boardView.getDiscardPileView().updateView();
			TextView textDiscardPile = (TextView) boardView
					.findViewById(R.id.nbDiscardPileCards);
			TextView textDrawPile = (TextView) boardView
					.findViewById(R.id.nbDrawPileCards);
			textDiscardPile.setText(""
					+ boardView.getDiscardPileView().getDiscardPile()
							.getNumberOfCards());
			textDrawPile.setText(""
					+ boardView.getDrawPileView().getDrawpile()
							.getNumberOfCards());
			boardView.updatePlayers();
			View v;
			for (int i = 0; i < boardView.getChildCount(); i++) {
				v = boardView.getChildAt(i);
				if (v instanceof CardView) {
					boardView.removeViewInLayout(v);
					i--;
				}
			}

			Gallery g = (Gallery) GameActivity.getActivity().findViewById(
					R.id.playerview_slider_board_cardgallery);
			if (g != null) {
				SliderbarCardGallery a = (SliderbarCardGallery) g.getAdapter();
				a.notifyDataSetChanged();
			}
			HandView hv = (HandView) GameActivity.getActivity().findViewById(
					R.id.handview);
			hv.updateView(true);
			if(ActionController.isSoundToggled())
	    		NotificationTools.createSoundNotification(GameActivity.getActivity().getApplicationContext(), R.drawable.startgame);
	    	if(ActionController.isVibrationToggled())
	    		NotificationTools.createVibrationNotification(GameActivity.getActivity().getApplicationContext(), 1000);
		}
		default:
			break;
		}

		NotifPopup.addNotif(notif);
	}
}
