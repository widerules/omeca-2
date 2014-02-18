package com.ensibs.omeca.utils;

import com.ensibs.omeca.R;

import android.app.AlertDialog;
import android.content.Context;

public class AboutPopup {

	public static void show(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder
			.setMessage(context.getResources().getString(R.string.about_text))
			.show();

	}

}