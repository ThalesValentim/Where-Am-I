package com.thalesvalentim.phonegap.plugin.whereAmI.sample;


import org.apache.cordova.*;
import android.app.Activity;
import android.os.Bundle;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import android.R;

public class MyServiceActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		showNotification("MeuApp1", "Hello1");
    }

	public void showNotification( String contentTitle, String contentText ) {
		int icon = android.R.drawable.btn_star_big_on;
        long when = System.currentTimeMillis();
        
        String message = "Mensagem";
		String title = "Titulo";
		Notification notif = new Notification(android.R.drawable.btn_star_big_on, message, System.currentTimeMillis() );
		notif.flags = Notification.FLAG_AUTO_CANCEL;
		notif.defaults |= Notification.DEFAULT_SOUND;
		notif.defaults |= Notification.DEFAULT_VIBRATE;
		 
		Intent notificationIntent = new Intent(this, MyServiceActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		 
		notif.setLatestEventInfo(this, title, message, contentIntent);
		String ns = this.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(ns);
		mNotificationManager.notify(1, notif);
	}
}