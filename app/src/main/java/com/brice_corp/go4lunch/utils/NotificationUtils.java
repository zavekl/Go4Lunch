package com.brice_corp.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.view.activity.AuthenticationActivity;

import java.util.ArrayList;

/**
 * Created by <NIATEL BRICE> on <01/06/2020>.
 */
class NotificationUtils {
    private static final String TAG = "NotificationUtils";
    private static final int NOTIFICATION_ID = 7;
    private static final String NOTIFICATION_TAG = "rappel";
    private static final CharSequence channelName = "Rappel";
    private final Context context;

    NotificationUtils(Context context) {
        this.context = context;
    }

    void sendNotification(String nameRestaurant, String address, ArrayList<String> workmates) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        StringBuilder workmateNames;
        //Build String with all the workmates
        if(workmates!=null){
            workmateNames = new StringBuilder(context.getString(R.string.stg_workmates) + " ");
            for (int i = 0; i < workmates.size(); i++) {
                workmateNames.append(workmates.get(i));
                if (i != workmates.size() - 1) {
                    workmateNames.append(" / ");
                }
            }
        } else{
            workmateNames = new StringBuilder(context.getString(R.string.stg_no_workmates));
        }


        //Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(nameRestaurant);
        inboxStyle.addLine(address);
        inboxStyle.addLine(workmateNames);

        //Create a Channel (Android 8)
        String channelId = "fcm_default_channel";

        //Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_search)
                        .setContentTitle(context.getString(R.string.notif_title))
                        .setContentText(nameRestaurant)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            } else {
                Log.e(TAG, "notification manager = null");
            }
        }

        //Show notification
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
        } else {
            Log.e(TAG, "sendNotification: notification manager = null");
        }
    }
}
