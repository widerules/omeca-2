package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.actions.AutomaticDistributionAction;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.DealView;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

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
					BoardView boardView = (BoardView) GameActivity.getActivity()
							.findViewById(R.id.view_board);
					GameActivity.getActivity().getWifiDirectManager().sendEvent(new WifiDirectEventImpl(WifiDirectEvent.EVENT,
							new AutomaticDistributionAction(ActionController.board.getPlace(ActionController.user), dv.getDealNumber())));
					boardView.runDistrib(ActionController.board.getPlace(ActionController.user), dv.getDealNumber());
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
