package com.ensibs.omeca;

import android.content.Context;
import android.content.SharedPreferences;

import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;

public class ControllerView {
	public OmecaApplication maActivity;
	public Board board;
	public int myId;
	public Player user;
	public ControllerView(OmecaApplication omecaApplication) {
		
		this.maActivity= omecaApplication;
		
		board = new Board();
		
		//TODO set the userID 
		myId= 1;
		SharedPreferences profilPreferences = omecaApplication.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		user = new Player(
				profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, ""),
				profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0));
		board.addPlayer(0, user);
	}

	public void dealCard(int nbToDeal){
		board.setCardsToDeal(nbToDeal);
		board.dealCardsAutomatically(myId);
	}
	
	
}
