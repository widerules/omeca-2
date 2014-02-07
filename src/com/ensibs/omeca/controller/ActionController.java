package com.ensibs.omeca.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.ensibs.omeca.AvatarActivity;
import com.ensibs.omeca.OmecaApplication;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;

public class ActionController {
	public static OmecaApplication maActivity;

	public static Player user;
	public static Board board;
	private static SharedPreferences profilPreferences;
	
	public ActionController(OmecaApplication omecaApplication) {
		
		ActionController.maActivity = omecaApplication;
		
		profilPreferences = maActivity.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		
		init();
	}
	
	public static void init(){
		user = new Player(
				profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, "Player"),
				profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0), 0);
		
		board = new Board();
	}
	
	public static void updateUser() {		
		SharedPreferences profilPreferences = maActivity.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		user.setAvatar(profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0));
		user.setName(profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, ""));
	}
	
	
}
