package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.brice_corp.go4lunch.utils.Constants.RADDRESS;
import static com.brice_corp.go4lunch.utils.Constants.RID;
import static com.brice_corp.go4lunch.utils.Constants.RNAME;

/**
 * Created by <NIATEL Brice> on <16/09/2020>.
 */
public class WorkerManager {
    private static final String TAG = "WorkerManager";
    private static final String WORK_NAME = "work_name";
    private static final String NOTIF = "notification";

    private Long mNotifTime;

    private Data mData;
    private Context mApplicationContext;
    private ApplicationPreferences applicationPreferences;

    public WorkerManager(Context applicationContext) {
        mApplicationContext = applicationContext;
        applicationPreferences = new ApplicationPreferences(mApplicationContext);
    }

    public void setWorker() {
        final long delay;
        Calendar actualDate = Calendar.getInstance();
        Calendar notificationDate = Calendar.getInstance();
        long nowTimeInMillis = actualDate.getTimeInMillis();

        if (mNotifTime != null) {
            Log.d(TAG, "setWorker: time from picker : " + mNotifTime);
            Log.d(TAG, "setWorker: now time  : " + nowTimeInMillis);
            delay = mNotifTime - nowTimeInMillis;
            Log.d(TAG, "setWorker: notif time " + delay);
        } else {
            notificationDate.set(Calendar.HOUR_OF_DAY, 12);
            notificationDate.set(Calendar.MINUTE, 0);
            notificationDate.set(Calendar.SECOND, 0);
            notificationDate.set(Calendar.MILLISECOND, 0);

            delay = notificationDate.getTimeInMillis() - nowTimeInMillis;
            Log.d(TAG, "setWorker: actual time" + delay);
        }

        //TODO A mettre au dernier moment pour repousser la date de un jour si l'heure est passé
//        if (notificationDate.before(actualDate)) {
//            Log.d(TAG, "setWorker if date : add one day");
//            //It's 12PM past, set it for tomorrow then
//            notificationDate.add(Calendar.HOUR_OF_DAY, 24);
//        }

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest notifRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                .setInputData(mData)
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(NOTIF)
                .build();

        WorkManager.getInstance(mApplicationContext).enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, notifRequest);
    }

    public static void stopWorkRequest(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(NOTIF);
    }

    public void setData(Data data) {
        if (mData == null) {
            mData = new Data.Builder()
                    .putString(RNAME, applicationPreferences.getSharedPrefsDATA().get(0))
                    .putString(RID, applicationPreferences.getSharedPrefsDATA().get(1))
                    .putString(RADDRESS, applicationPreferences.getSharedPrefsDATA().get(2))
                    .build();
        } else {
            applicationPreferences.setSharedPrefsData(mData);
            this.mData = data;
        }
    }

    //TODO Faire en sorte que ce soit sauvegarder et non qu'à chaque fois que desc resto est ouvert que ce soit reset et mit pour midi
    public void setTime(long timeNotification) {
        mNotifTime = timeNotification;
    }
}
