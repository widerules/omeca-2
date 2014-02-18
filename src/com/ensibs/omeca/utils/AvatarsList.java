package com.ensibs.omeca.utils;

import com.ensibs.omeca.R;


/**
 * This class contains the list of every avatar images.
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 */
public abstract class AvatarsList {

	/**
	 * The list of every avatar image
	 */
	private final static Integer[] mImageIds = {
		
		R.drawable.avatar_alien,
		R.drawable.avatar_angel,
		R.drawable.avatar_basic_guy,
		R.drawable.avatar_billy,
		R.drawable.avatar_borg,
		R.drawable.avatar_bricky,
		R.drawable.avatar_camouflage,
		R.drawable.avatar_candy,
		R.drawable.avatar_chef,
		R.drawable.avatar_cowboy,
		R.drawable.avatar_dandy,
		R.drawable.avatar_devil,
		R.drawable.avatar_fox,
		R.drawable.avatar_geek,
		R.drawable.avatar_geisha,
		R.drawable.avatar_girl,
		R.drawable.avatar_ninja,
		R.drawable.avatar_nurse,
		R.drawable.avatar_pirate,
		R.drawable.avatar_policeman,
		R.drawable.avatar_princess,
		R.drawable.avatar_punker,
		R.drawable.avatar_santa,
		R.drawable.avatar_squared,
		R.drawable.avatar_stripey,
		R.drawable.avatar_sunglasser
	
	};
	
	/**
	 * Getter for an avatar image, given id
	 * @param id
	 * @return
	 */
	public static int get(int id){
		
		return mImageIds[id];
		
	}
	
	/**
	 * Returns the number of available avatars
	 * @return
	 */
	public static int getLength(){
		
		return mImageIds.length;
		
	}
	
}
