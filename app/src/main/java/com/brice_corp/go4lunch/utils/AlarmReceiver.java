package com.brice_corp.go4lunch.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <19/07/2020>.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> list = intent.getStringArrayListExtra("listWorkmates");
        // show toast
        Log.d(TAG, "onReceive: start : " + list);
        NotificationUtils notificationUtils = new NotificationUtils(context);
        if (list != null) {
            if (list.size() != 1) {
                for (int i = 0; i < list.size(); i++) {

                }

            for (String name : list) {

            }
        }else {
                //TODO AFFICHER JUSTE SON PROPRE PRENOM
                Log.i(TAG, "onReceive: ");
            }
        }
        notificationUtils.sendNotification("restaurant", "4 rue de France", "Cécile");
    }
}
