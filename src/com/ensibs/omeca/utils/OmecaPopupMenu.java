package com.ensibs.omeca.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.ensibs.omeca.R;

public class OmecaPopupMenu {
	private static AlertDialog omecaPopupMenu = null;

	public static void show(Activity act) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		LayoutInflater inflater = act.getLayoutInflater();
		omecaPopupMenu = builder.create();
		omecaPopupMenu.setTitle("Options");
		omecaPopupMenu.setView(inflater.inflate(R.layout.popup_menu, null));
		omecaPopupMenu.show();		
	}
	
	public static void dismiss() {
		omecaPopupMenu.cancel();
	}
}
