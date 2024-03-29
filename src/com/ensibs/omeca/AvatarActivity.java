package com.ensibs.omeca;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.utils.AvatarGallery;

/**
 * This activity manages the Player profil set up
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 */
public class AvatarActivity extends Activity {

	/**
	 * String key for shared preferences file name
	 */
	public static final String SHARED_PREFERENCES_FILE_NAME = "OMECA Profile";

	/**
	 * String key for shared preferences avatar id
	 */
	public static final String SHARED_PREFERENCES_AVATAR_ID_NAME = "avatarId";

	/**
	 * String key for shared preferences player name
	 */
	public static final String SHARED_PREFERENCES_PLAYER_NAME = "pseudo";

	private Gallery avatarGallery;
	private final String SHARED_PREFERENCES_VIBRATION = "vibration";
	private final String SHARED_PREFERENCES_SOUND = "sound";
	private ToggleButton soundToggle = null;
	private ToggleButton vibrationToggle = null;
	private SharedPreferences profilPreferences;
	private EditText pseudoEditText;

	/**
	 * Creates the activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.view_avatar);

		this.avatarGallery = (Gallery) findViewById(R.id.avatar_gallery);
		this.avatarGallery.setAdapter(new AvatarGallery(this));

		this.pseudoEditText = (EditText) findViewById(R.id.name);

		this.profilPreferences = getSharedPreferences(
				SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		loadUserProfile();

		// Retrieve options
		retrieveOptions();

	}

	/**
	 * Load the saved user profile
	 */
	public void loadUserProfile() {
		String pseudo = this.profilPreferences.getString(
				SHARED_PREFERENCES_PLAYER_NAME, "");
		int avatarId = this.profilPreferences.getInt(
				SHARED_PREFERENCES_AVATAR_ID_NAME, (int) (this.avatarGallery
						.getAdapter().getCount() / 2));
		if (pseudo != "") {
			this.pseudoEditText.setText(pseudo);
		}

		this.avatarGallery.setSelection(avatarId);

		// Add the yellow background on the selected view
		this.avatarGallery.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						if (AvatarActivity.this.avatarGallery.getSelectedView() != null) {
							// EditProfileActivity.this.avatarGallery.getSelectedView().setBackgroundResource(R.drawable.avatar_background_on_select);
						}

					}

				});

	}

	/**
	 * Saves the user Profile
	 * 
	 * @param v
	 */
	public void saveUserProfile(View v) {
		String pseudo = this.pseudoEditText.getText().toString();

		if (pseudo.equals("")) {
			Toast.makeText(this, R.string.error_username_null,
					Toast.LENGTH_LONG).show();

		} else if (pseudo.length() > 10) {
			Toast.makeText(this, R.string.error_username_length,
					Toast.LENGTH_LONG).show();

		} else if (pseudo.contains("\n") || pseudo.contains("\\n")) {
			Toast.makeText(this, R.string.error_username_back,
					Toast.LENGTH_LONG).show();

		} else {
			SharedPreferences.Editor editor = this.profilPreferences.edit();
			editor.putString(SHARED_PREFERENCES_PLAYER_NAME, pseudo);
			editor.putInt(SHARED_PREFERENCES_AVATAR_ID_NAME,
					this.avatarGallery.getSelectedItemPosition());
			editor.commit();
			GA.updateUser();
			finish();

		}

	}

	/**
	 * Saves the new avatar
	 * 
	 * @param view
	 */
	public void saveAvatar(View view) {
		Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
		String pseudo = this.pseudoEditText.getText().toString();

		if (pseudo.equals("")) {
			Toast.makeText(this, R.string.error_username_null,
					Toast.LENGTH_LONG).show();

		} else if (pseudo.length() > 10) {
			Toast.makeText(this, R.string.error_username_length,
					Toast.LENGTH_LONG).show();

		} else if (pseudo.contains("\n") || pseudo.contains("\\n")) {
			Toast.makeText(this, R.string.error_username_back,
					Toast.LENGTH_LONG).show();

		} else {
			SharedPreferences.Editor editor = this.profilPreferences.edit();
			editor.putString(SHARED_PREFERENCES_PLAYER_NAME, pseudo);
			editor.putInt(SHARED_PREFERENCES_AVATAR_ID_NAME,
					this.avatarGallery.getSelectedItemPosition());
			editor.commit();
			GA.updateUser();
			finish();
		}
	}

	/**
	 * Exits without saving any changes
	 * 
	 * @param view
	 */
	public void cancelAvatar(View view) {
		finish();
	}

	/**
	 * Retrieves options for music and vibration preferences
	 */
	private void retrieveOptions() {
		OnClickListener list = new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = profilPreferences.edit();
				editor.putBoolean(SHARED_PREFERENCES_SOUND,
						soundToggle.isChecked());
				editor.putBoolean(SHARED_PREFERENCES_VIBRATION,
						vibrationToggle.isChecked());
				editor.commit();
			}
		};

		profilPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		soundToggle = (ToggleButton) this
				.findViewById(R.id.homescreen_options_sound_toggle);
		soundToggle.setOnClickListener(list);
		soundToggle.setChecked(this.profilPreferences.getBoolean(
				SHARED_PREFERENCES_SOUND, false));
		vibrationToggle = (ToggleButton) this
				.findViewById(R.id.homescreen_options_vibration_toggle);
		vibrationToggle.setOnClickListener(list);
		vibrationToggle.setChecked(this.profilPreferences.getBoolean(
				SHARED_PREFERENCES_VIBRATION, false));
	}

}