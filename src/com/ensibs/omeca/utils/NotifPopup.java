package com.ensibs.omeca.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ensibs.omeca.R;
import com.ensibs.omeca.controller.ActionController;
import com.ensibs.omeca.model.entities.Notif;

public class NotifPopup {
	private static AlertDialog.Builder notifPopup = null;

	public static void show(Context context) {

		Stack<Notif> notifs = generateNotifs();

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
		notifPopup.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface popup, int arg1) {
				popup.dismiss();
			}
		});

		notifPopup.show();

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
	 * TEMP!!!
	 * A remplacer par les vraies gestions de notifs ASAP
	 * @return
	 */
	private static Stack<Notif> generateNotifs() {
		Stack<Notif> notifs = new Stack<Notif>();
		Notif notif = new Notif();
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss", Locale.FRANCE);
		String date = formatter.format(new Date());
		
		notif.setDate(date);
		notif.setSource(ActionController.user);
		notif.setTarget(null);
		notif.setEvent("pioche");
		notifs.push(notif);
		
		notif = new Notif();
		notif.setDate(date);
		notif.setSource(ActionController.user);
		notif.setTarget(ActionController.user);
		notif.setEvent("donne une carte à");
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);
		notifs.push(notif);

		return notifs;
	}

}
