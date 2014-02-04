package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.MainActivity;
import com.ensibs.omeca.wifidirect.property.WifiDirectProperty;

public class OmecaPopupExit {
	private static AlertDialog omecaPopupMenu = null;
	private static Boolean isGameActivity = false;
	private static String finishText = "Voulez-vous vraiment quitter l'application?";

	public static void show(Context context) {
		if (context instanceof	GameActivity) {
			isGameActivity = true;
			finishText = "Voulez-vous vraiment quitter la partie?";
		}
		else{
			isGameActivity = false;
			finishText = "Voulez-vous vraiment quitter l'application?";
		}

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case DialogInterface.BUTTON_POSITIVE:
					if (isGameActivity) {
						GameActivity.getActivity().finishGameActivity();
					} else {
						MainActivity.getActivity().finishMainActivity();
					}
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					omecaPopupMenu.cancel();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		omecaPopupMenu = builder.create();

		builder
		.setMessage(finishText)
		.setPositiveButton("Oui", dialogClickListener)
		.setNegativeButton("Non", dialogClickListener)
		.show();

	}

	public static void dismiss() {
		omecaPopupMenu.cancel();
	}

}
