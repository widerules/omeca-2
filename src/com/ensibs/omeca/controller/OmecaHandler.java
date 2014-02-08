package com.ensibs.omeca.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.DiscardPileView;
import com.ensibs.omeca.view.DrawPileView;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class OmecaHandler extends Handler{
	
	public static final int CONNEXION = 0;
	public static final int DECONNEXION = 1;
	public static final int AKNOWLEGMENT_CONNECTION_ACTION = 2;
	public static final int RETURN_CARD = 3;
	
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
				
			}else if(data.getString("Source").equals("DiscardPileView")){
				DiscardPileView discardView = boardView.getDiscardPileView();
				discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).setFaceUp(!discardView.getDiscardPile().getCards().get(discardView.getDiscardPile().getNumberOfCards()-1).isFaceUp());
				discardView.updateView();
			}
		}
	}
}
