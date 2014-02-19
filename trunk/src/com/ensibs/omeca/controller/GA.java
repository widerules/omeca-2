package com.ensibs.omeca.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.ensibs.omeca.AvatarActivity;
import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.MainActivity;
import com.ensibs.omeca.OmecaApplication;
import com.ensibs.omeca.model.actions.AknowlegmentConnectionAction;
import com.ensibs.omeca.model.entities.Board;
import com.ensibs.omeca.model.entities.Player;
import com.ensibs.omeca.wifidirect.mod.WifiDirectMod;

/**
 * This class provides global access to the current Player and the Board GA for
 * Global Accessor
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class GA {
	/**
	 * Omeca android application
	 */
	public static OmecaApplication maActivity;

	/**
	 * Current Player (user)
	 */
	public static Player user;

	/**
	 * Current Board
	 */
	public static Board board;

	/**
	 * Current Player preferences (avatar, name, vibrations, sounds)
	 */
	private static SharedPreferences profilPreferences;

	/**
	 * Constructor
	 * 
	 * @param omecaApplication
	 *            The Omeca android application
	 */
	public GA(OmecaApplication omecaApplication) {

		GA.maActivity = omecaApplication;

		profilPreferences = maActivity.getSharedPreferences(
				AvatarActivity.SHARED_PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);

	}

	/**
	 * Provides an initialization of the current Player and the Board. The
	 * Player information will be updated after being notified by the host with
	 * an AknolegmentConnectionAction {@link AknowlegmentConnectionAction}
	 * 
	 */
	public static void init() {
		int id;
		if (GameActivity.getActivity().getWifiDirectManager().getMod() == WifiDirectMod.HOST) {
			id = 0;
		} else {
			id = -1;
		}
		user = new Player(profilPreferences.getString(
				AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, "Player"),
				profilPreferences.getInt(
						AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0),
				id);

		board = new Board();
	}

	/**
	 * Updates current Player information according to his preferences
	 */
	public static void updateUser() {
		if (user == null) {
			user = new Player(
					profilPreferences.getString(
							AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME,
							"Player"),
					profilPreferences
							.getInt(AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME,
									0), -1);
		}
		user.setAvatar(profilPreferences.getInt(
				AvatarActivity.SHARED_PREFERENCES_AVATAR_ID_NAME, 0));
		user.setName(profilPreferences.getString(
				AvatarActivity.SHARED_PREFERENCES_PLAYER_NAME, ""));
	}

	/**
	 * Returns true if vibrations have been enabled, false else
	 * 
	 * @return true if vibrations have been enabled, false else
	 */
	public static boolean isVibrationToggled() {
		return profilPreferences.getBoolean(
				MainActivity.SHARED_PREFERENCES_VIBRATION, false);
	}

	/**
	 * Returns true if sounds have been enabled, false else
	 * 
	 * @return true if sounds have been enabled, false else
	 */
	public static boolean isSoundToggled() {
		return profilPreferences.getBoolean(
				MainActivity.SHARED_PREFERENCES_SOUND, false);
	}

}
