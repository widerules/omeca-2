package com.ensibs.omeca.utils;

import com.ensibs.omeca.R;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Shows the About popup inside the HelpActivity
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class AboutPopup {

	/**
	 * Shows the popup, given context
	 * 
	 * @param context
	 *            the context
	 */
	public static void show(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(
				context.getResources().getString(R.string.about_text)).show();

	}

}
