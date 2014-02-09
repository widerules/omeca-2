package com.ensibs.omeca.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.ensibs.omeca.AvatarActivity;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.OmecaApplication;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;

public class ActionController {
	public static OmecaApplication maActivity;

	public static Player user;
	public static Board board;
	private static SharedPreferences profilPreferences;
	
	public ActionController(OmecaApplication omecaApplication) {
		
		ActionController.maActivity = omecaApplication;
		
		profilPreferences = maActivity.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		
	}
	
	public static void init(){
		int id;
		if(GameActivity.getActivity().getWifiDirectManager().getMod() == WifiDirectMod.HOST){
			id=0;
		}
		else{
			id=-1;
		}
		user = new Player(
				profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, "Player"),
				profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0), id);
		
		board = new Board();
	}
	
	public static void updateUser() {		
		SharedPreferences profilPreferences = maActivity.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		user.setAvatar(profilPreferences.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0));
		user.setName(profilPreferences.getString(AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, ""));
	}
	
	
}
