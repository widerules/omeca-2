package com.ensibs.omeca.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Card;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.CardView;
import com.ensibs.omeca.view.DiscardPileView;
import com.ensibs.omeca.view.DrawPileView;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class OmecaHandler extends Handler{
	
	public static final int CONNEXION = 0;
	public static final int DECONNEXION = 1;
	public static final int AKNOWLEGMENT_CONNECTION_ACTION = 2;
	public static final int RETURN_CARD = 3;
	public static final int MOVE_CARD = 4;
	
	public OmecaHandler(Looper looper){
		super(looper);
	}
	
	/**
	 * Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", msg);
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);
	 */
	
	@Override
	public void handleMessage(Message msg) {
		if(msg.what == DECONNEXION){
			Toast.makeText(GameActivity.getActivity(), "Deconnexion", Toast.LENGTH_LONG).show();
		}
		else if(msg.what == AKNOWLEGMENT_CONNECTION_ACTION){
			Log.w("deed", "deded");
			((BoardView)GameActivity.getActivity().findViewById(R.id.view_board)).updatePlayers();
		}
		else if(msg.what == RETURN_CARD){
			Bundle data = msg.getData();
			BoardView boardView = (BoardView)GameActivity.getActivity().findViewById(R.id.view_board);
			if(data.getString("Source").equals("DrawPileView")){
				DrawPileView pileView = boardView.getDrawPileView();
				pileView.getDrawpile().getCards().get(pileView.getDrawpile().getNumberOfCards()-1).setFaceUp(!pileView.getDrawpile().getCards().get(pileView.getDrawpile().getNumberOfCards()-1).isFaceUp());
				pileView.updateView();
			}else if(data.getString("Source").equals("BoardView")){
				//TODO : chercher la carte sur le board et la retourner
			}else if(data.getString("Source").equals("DiscardPileView")){
				DiscardPileView discardView = boardView.getDiscardPileView();
				discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).setFaceUp(!discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).isFaceUp());
				discardView.updateView();
			}
		}else if(msg.what == MOVE_CARD){
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
				}
			}else if(data.getString("Source").equals("BoardView")){
				//TODO : chercher la carte sur le board et la retourner
			}else if(data.getString("Source").equals("DiscardPileView")){
				DiscardPileView discardView = boardView.getDiscardPileView();
				Card tmp = discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1);
				discardView.getDiscardPile().getCards().remove(discardView.getDiscardPile().getNumberOfCards()-1);
				discardView.updateView();
				if(data.getString("Target").equals("BoardView")){
					boardView.addView(new CardView(GameActivity.getActivity(),tmp));
				}else if(data.getString("Target").equals("DrawPileView")){
					boardView.getDrawPileView().addView(new CardView(GameActivity.getActivity(),tmp));
				}
			}
		}
	}
}
