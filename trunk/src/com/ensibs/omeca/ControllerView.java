package com.ensibs.omeca;

import android.content.Context;
import android.content.SharedPreferences;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;

public class ControllerView {
	public OmecaApplication maActivity;
	
	public int myId;
	public static Player user;
	public static Board board;
	public ControllerView(OmecaApplication omecaApplication) {
		
		this.maActivity= omecaApplication;
		
		board = new Board();
		
		SharedPreferences profilPreferences = omecaApplication.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		user = new Player(
				profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, ""),
				profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0), 0);
	}

	public void dealCard(int nbToDeal){
		board.setCardsToDeal(nbToDeal);
		board.dealCardsAutomatically(myId);
	}
	
	
	
	
}
