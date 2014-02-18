package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.MainActivity;
import com.ensibs.omeca.R;

/**
 * This class creates, display, discard and manages the exit popup
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 *
 */
public class OmecaPopupExit {
	/**
	 * The AlertDialog
	 */
	private static AlertDialog omecaPopupMenu = null;

	/**
	 * True if context activity is GameActivity
	 */
	private static Boolean isGameActivity = false;

	/**
	 * The text to display in the exit popup
	 */
	private static String finishText = "";

	/**
	 * Displays the popup
	 * @param context the context
	 */
	public static void show(Context context) {

		// Define whether we deal with exiting GameActivity or MainActivity
		if (context instanceof	GameActivity) {
			isGameActivity = true;
			finishText = GameActivity.getActivity().getResources().getString(R.string.confirm_exit_game);
		}
		else{
			isGameActivity = false;
			finishText = GameActivity.getActivity().getResources().getString(R.string.confirm_exit_application);
		}

		// Define the Lister
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

		// Creates and show the popup
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		omecaPopupMenu = builder.create();

		builder
		.setMessage(finishText)
		.setPositiveButton(GameActivity.getActivity().getResources().getString(R.string.yes), dialogClickListener)
		.setNegativeButton(GameActivity.getActivity().getResources().getString(R.string.no), dialogClickListener)
		.show();

	}

	/**
	 * Dismiss the popup
	 */
	public static void dismiss() {
		omecaPopupMenu.cancel();
	}

}
