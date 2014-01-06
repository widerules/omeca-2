package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.ensibs.omeca.R;

public class OmecaPopupMenu {
	private static AlertDialog omecaPopupMenu = null;

	public static void show(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		omecaPopupMenu = builder.create();
		omecaPopupMenu.setTitle("Options");
		omecaPopupMenu.setView(inflater.inflate(R.layout.popup_menu, null));
		omecaPopupMenu.show();		
	}
	
	public static void dismiss() {
		omecaPopupMenu.cancel();
	}
}
