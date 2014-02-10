package com.ensibs.omeca;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Toast;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.utils.AvatarGallery;

/**
 * @author S�bastien Bat�zat <sebastien.batezat@gmail.com>
 */
public class AvatarActivity extends Activity{
	
	private Gallery avatarGallery;
	public static final String SHARED_PREFERENCES_FILE_NAME = "OMECA Profile";
	public static final String SHARED_PREFERENCES_AVATAR_ID_NAME = "avatarId";
	public static final String SHARED_PREFERENCES_PLAYER_NAME = "pseudo";
	private SharedPreferences profilPreferences;
	private EditText pseudoEditText;
	
	@Override
    public void onCreate(Bundle savedInstanceState){
    	
		super.onCreate(savedInstanceState);

		// Hides titlebar and actionbar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.view_avatar);
        
        this.avatarGallery = (Gallery) findViewById(R.id.avatar_gallery);
        this.avatarGallery.setAdapter(new AvatarGallery(this));
        
        this.pseudoEditText = (EditText)findViewById(R.id.name);
        
        this.profilPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        loadUserProfile();
        
	}
	
	public void loadUserProfile() {		
        String pseudo = this.profilPreferences.getString(SHARED_PREFERENCES_PLAYER_NAME, "");
        int avatarId = this.profilPreferences.getInt(SHARED_PREFERENCES_AVATAR_ID_NAME, (int)(this.avatarGallery.getAdapter().getCount() / 2));
        if (pseudo != "") {
        	this.pseudoEditText.setText(pseudo);
        }
       
        this.avatarGallery.setSelection(avatarId);
        
        // Add the yellow background on the selected view
        this.avatarGallery.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
			public void onGlobalLayout() {
				if (AvatarActivity.this.avatarGallery.getSelectedView() != null) {
					//EditProfileActivity.this.avatarGallery.getSelectedView().setBackgroundResource(R.drawable.avatar_background_on_select);	
				}
				
			}
			
		});
        
	}
	
	public void saveUserProfile(View v) {		
		String pseudo = this.pseudoEditText.getText().toString();
		
		if (pseudo.equals("")) {
			Toast.makeText(this, R.string.error_username_null, Toast.LENGTH_LONG).show();
			
		} else if (pseudo.length() > 10) {
			Toast.makeText(this, R.string.error_username_length, Toast.LENGTH_LONG).show();
			
		} else if (pseudo.contains("\n") || pseudo.contains("\\n")) {
			Toast.makeText(this, R.string.error_username_back, Toast.LENGTH_LONG).show();
			
		} else {
			SharedPreferences.Editor editor = this.profilPreferences.edit();
			editor.putString(SHARED_PREFERENCES_PLAYER_NAME, pseudo);
			editor.putInt(SHARED_PREFERENCES_AVATAR_ID_NAME, this.avatarGallery.getSelectedItemPosition());
			editor.commit();
			ActionController.updateUser();
			finish();
			
		}		
		
	}
	
	/**
	 * Saves the new avatar
	 * @param view
	 */
	public void saveAvatar(View view) {
		Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
		String pseudo = this.pseudoEditText.getText().toString();
		
		if (pseudo.equals("")) {
			Toast.makeText(this, R.string.error_username_null, Toast.LENGTH_LONG).show();
			
		} else if (pseudo.length() > 10) {
			Toast.makeText(this, R.string.error_username_length, Toast.LENGTH_LONG).show();
			
		} else if (pseudo.contains("\n") || pseudo.contains("\\n")) {
			Toast.makeText(this, R.string.error_username_back, Toast.LENGTH_LONG).show();
			
		} else {
			SharedPreferences.Editor editor = this.profilPreferences.edit();
			editor.putString(SHARED_PREFERENCES_PLAYER_NAME, pseudo);
			editor.putInt(SHARED_PREFERENCES_AVATAR_ID_NAME, this.avatarGallery.getSelectedItemPosition());
			editor.commit();
			ActionController.updateUser();
			finish();
		}	
	}

	/**
	 * Exits without saving any changes
	 * @param view
	 */
	public void cancelAvatar(View view) {
		finish();
	}
	
}