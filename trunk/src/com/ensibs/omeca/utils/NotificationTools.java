package com.ensibs.omeca.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotificationTools {
	
	/**
	 * Id and counter of notification
	 */
	private static int counter = 1000;
	
	//TODO: return to the activity
	/**
	 * Display a notification in the notification bar
	 * @param context application context
	 * @param icon Drawable icon
	 * @param title Title of the notification
	 * @param content Content of the notification
	 * @return Id of the new notification
	 */
	public static int createBarNotification(Context context, int icon, String title, String content){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(icon);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(content);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(counter,mBuilder.build());
		counter ++;
		return counter-1;
	}

	public static void createVibrationNotification(Context context, int time){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setVibrate(new long[]{100,time});
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(counter,mBuilder.build());
		counter ++;
	}
	
	public static void createSoundNotification(Context context, int resource){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSound(Uri.parse("android.resource://com.ensibs.omeca/" + resource));
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(counter,mBuilder.build());
		counter ++;
	}
	
}
