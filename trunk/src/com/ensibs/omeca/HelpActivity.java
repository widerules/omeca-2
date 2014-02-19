package com.ensibs.omeca;

import com.ensibs.omeca.utils.AboutPopup;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class is the activity when the Player is reading the help
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class HelpActivity extends Activity {

	/**
	 * Creates the activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	/**
	 * Click on Android options menu button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	/**
	 * Click on a Android options menu item button
	 * 
	 * @param item
	 *            Selected item
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_help_about:
			AboutPopup.show(this);
			return true;
		case R.id.action_help_exit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
