package com.ensibs.omeca.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.ensibs.omeca.R;

public class OmecaPopupMenu {
	private static AlertDialog.Builder dDialog = null;
	private static LayoutInflater inflater = null;

	public static void show(Activity act) {
		dDialog = new AlertDialog.Builder(act);
		inflater = act.getLayoutInflater();
	    dDialog.setTitle("Options");
	    dDialog.setView(inflater.inflate(R.layout.popup_menu, null));
	    dDialog.show();		
	}
}
