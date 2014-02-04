package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.MainActivity;
import com.ensibs.omeca.R;

public class OmecaPopupMenu {
	private static AlertDialog omecaPopupMenu = null;

	public static void show(Context context) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		omecaPopupMenu = builder.create();
		
		if (context instanceof	MainActivity) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			omecaPopupMenu.setView(inflater.inflate(R.layout.popup_menu_home, null));
			omecaPopupMenu.setTitle("Menu");
			omecaPopupMenu.setCanceledOnTouchOutside(false);
			omecaPopupMenu.show();
		} else if (context instanceof GameActivity) {			
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            GameActivity.getActivity().finishGameActivity();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
						omecaPopupMenu.cancel();
			            break;
			        }
			    }
			};
			
			builder
				.setMessage("Voulez-vous vraiment quitter la partie?")
				.setPositiveButton("Oui", dialogClickListener)
				.setNegativeButton("Non", dialogClickListener)
				.show();
		} else {
			omecaPopupMenu.cancel();
		}
		
	}
	
	public static void dismiss() {
		omecaPopupMenu.cancel();
	}
	
}
