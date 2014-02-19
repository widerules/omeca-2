package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.GA;
import com.ensibs.omeca.model.actions.AutomaticDistributionAction;
import com.ensibs.omeca.view.BoardView;
import com.ensibs.omeca.view.DealView;
import com.ensibs.omeca.wifidirect.event.WifiDirectEvent;
import com.ensibs.omeca.wifidirect.event.WifiDirectEventImpl;

/**
 * This class shows and deals with a Deal Popup
 * 
 * @author OMECA 2.0 Team (Rapha�l GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO -
 *         Lindsay ROZIER)
 * 
 */
public class DealPopup {
	/**
	 * The AlertDialog builder
	 */
	private static AlertDialog.Builder dealPopup = null;

	/**
	 * The DealView
	 */
	private static DealView dv;

	/**
	 * Displays the popup, given context
	 * 
	 * @param context
	 *            the context
	 */
	public static void show(Context context) {

		if (GA.board.getDrawPile().getNumberOfCards() > 0) {
			dv = new DealView(GA.board.getDrawPile().getNumberOfCards(),
					GA.board.getPlayers().size(), context);
			dealPopup = new AlertDialog.Builder(context);
			dealPopup.setTitle(GameActivity.getActivity().getResources()
					.getString(R.string.list_distribution));
			dealPopup.setView(dv);
			final AlertDialog alert = dealPopup.create();
			alert.show();
			dv.buttonSave.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					BoardView boardView = (BoardView) GameActivity
							.getActivity().findViewById(R.id.view_board);
					GameActivity
							.getActivity()
							.getWifiDirectManager()
							.sendEvent(
									new WifiDirectEventImpl(
											WifiDirectEvent.EVENT,
											new AutomaticDistributionAction(
													GA.board.getPlace(GA.user),
													dv.getDealNumber())));
					boardView.runDistrib(GA.board.getPlace(GA.user),
							dv.getDealNumber());
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
