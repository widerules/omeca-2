package com.ensibs.omeca.utils;

import java.util.Stack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ensibs.omeca.GameActivity;
import com.ensibs.omeca.R;
import com.ensibs.omeca.model.entities.Notif;
import com.ensibs.omeca.view.PlayerView;

/**
 * This class displays, dismiss and manage a Notification popup
 * @author OMECA 2.0 Team (Raphaël GICQUIAUX - Nicolas HALLOUIN - Sylvain RIO - Lindsay ROZIER)
 *
 */
public class NotifPopup {
	/**
	 * The AlertDialog builder
	 */
	private static AlertDialog.Builder notifPopup = null;
	
	/**
	 * The notifs stack
	 */
	private static Stack<Notif> notifs = new Stack<Notif>();

	/**
	 * Shows the popup, given context
	 * @param context the context
	 */
	public static void show(Context context) {

		if (notifs.size() > 0) {
			notifPopup = new AlertDialog.Builder(context);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			View notifView = inflater.inflate(R.layout.popup_notifs, null);

			for (Notif n : notifs) {
				View v = inflater.inflate(R.layout.view_notif, null);
				TextView tvDate = (TextView) v.findViewById(R.id.notifs_date);
				ImageView ivSrc = (ImageView) v.findViewById(R.id.notifs_src);
				TextView tvEvent = (TextView) v.findViewById(R.id.notifs_event);
				ImageView ivTarget = (ImageView) v.findViewById(R.id.notifs_target);

				notifGenerator(n, tvDate, ivSrc, tvEvent, ivTarget);

				LinearLayout ll = (LinearLayout) notifView.findViewById(R.id.notif_wrapper);
				((ViewGroup) ll).addView(v);
			}

			notifPopup.setView(notifView);
			notifPopup.setNegativeButton(GameActivity.getActivity().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface popup, int arg1) {
					popup.dismiss();
					flushNotifs();
				}
			});

			notifPopup.show();
		}

	}


	/**
	 * Generates the notification entry
	 * @param n the notifiction to generate
	 * @param tv the date textview
	 * @param ivSrc the src imageview
	 * @param tvEvent the event textview
	 * @param ivTarget the target imageview 
	 * @return
	 */
	private static void notifGenerator(Notif n, TextView tvDate, ImageView ivSrc, TextView tvEvent, ImageView ivTarget) {
		if (n != null) {
			if (n.getDate() != null) {
				tvDate.setText(n.getDate() + " - ");
			}

			if (n.getSource() != null) {
				ivSrc.setBackgroundResource(AvatarsList.get(n.getSource().getAvatar()));
			}

			if (n.getEvent() != null) {
				tvEvent.setText(n.getEvent());
			}

			if (n.getTarget() != null) {
				ivTarget.setBackgroundResource(AvatarsList.get(n.getTarget().getAvatar()));
			}
		}
	}


	/**
	 * Adds a notif
	 * @param src
	 * @param tgt
	 * @param event
	 */
	public static void addNotif(Notif notif) {	
		if (((SlidingUpPanelLayout)GameActivity.getActivity().findViewById(R.id.sliding_layout)).isExpanded()) {
			notifs.push(notif);			
			PlayerView.updateNotifs(notifs.size());
		}
	}


	/**
	 * Removes all notifs
	 */
	public static void flushNotifs() {
		notifs.removeAllElements();

		PlayerView.updateNotifs(notifs.size());
	}

	/**
	 * Returns the number of notifs
	 * @return the number of notifs
	 */
	public static int getNumberOfNofifs(){
		return notifs.size();
	}

}
