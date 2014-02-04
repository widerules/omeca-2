package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.view.DealView;

public class DealPopup {
	private static AlertDialog.Builder dealPopup = null;
	private static DealView dv;

	public static void show(Context context) {
		
		if (ActionController.board.getDrawPile().getNumberOfCards() > 0) {
			dv = new DealView(ActionController.board
					.getDrawPile().getNumberOfCards(), ActionController.board
					.getPlayers().size(), context);
			dealPopup = new AlertDialog.Builder(context);
			dealPopup.setTitle("Distribution automatique");
			dealPopup.setView(dv);
			final AlertDialog alert = dealPopup.create();
			alert.show();
			dv.buttonSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//controller.dealCard(dv.getDealNumber());
					alert.dismiss();
				}
			});
			dv.buttonCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					alert.cancel();
				}
			});
		}

	}

}
