package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.MainActivity;
import com.ensibs.omeca.R;

public class OmecaPopupMenu {
	private static AlertDialog omecaPopupMenu = null;

	public static void show(Context context) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		omecaPopupMenu = builder.create();
		omecaPopupMenu.setTitle("Menu");
		omecaPopupMenu.setCanceledOnTouchOutside(false);
		
		if (context instanceof	MainActivity) {
			omecaPopupMenu.setView(inflater.inflate(R.layout.popup_menu_home, null));
			omecaPopupMenu.show();
		} else if (context instanceof GameActivity) {
			omecaPopupMenu.setView(inflater.inflate(R.layout.popup_menu_game, null));
			omecaPopupMenu.show();
		} else {
			omecaPopupMenu.cancel();
		}
		
	}
	
	public static void dismiss() {
		omecaPopupMenu.cancel();
	}
	
}
