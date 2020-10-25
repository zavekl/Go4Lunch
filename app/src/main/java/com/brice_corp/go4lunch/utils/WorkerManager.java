package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by <NIATEL Brice> on <16/09/2020>.
 */
public class WorkerManager {
    private static final String TAG = "WorkerManager";
    private static final String WORK_NAME = "work_name";
    private static final String NOTIF = "notification";

    private final Context mApplicationContext;
    private final ApplicationPreferences mApplicationPreferences;

    public WorkerManager(Context applicationContext) {
        mApplicationContext = applicationContext;
        mApplicationPreferences = new ApplicationPreferences(mApplicationContext);
    }

    public void setWorker() {
        stopWorkRequest(mApplicationContext);
        long delay;
        Calendar actualDate = Calendar.getInstance();
        Calendar notificationDate = Calendar.getInstance();
        long nowTimeInMillis = actualDate.getTimeInMillis();

        long actualTime = TimeUnit.HOURS.toMillis(actualDate.get(Calendar.HOUR_OF_DAY)) + TimeUnit.MINUTES.toMillis(actualDate.get(Calendar.MINUTE)) +
                TimeUnit.SECONDS.toMillis(actualDate.get(Calendar.SECOND));

        if (mApplicationPreferences.getSharedPrefsTime() != 0) {
            delay = mApplicationPreferences.getSharedPrefsTime() - actualTime;
            Log.d(TAG, "setWorker: delay of  shared pref : " + delay);
        } else {
            notificationDate.set(Calendar.HOUR_OF_DAY, 12);
            notificationDate.set(Calendar.MINUTE, 0);
            notificationDate.set(Calendar.SECOND, 0);
            notificationDate.set(Calendar.MILLISECOND, 0);

            delay = notificationDate.getTimeInMillis() - nowTimeInMillis;
            Log.d(TAG, "setWorker: actual time" + delay);
        }

        if (delay < 0) {
            Log.d(TAG, "setWorker: add one day");
            long mOneDay = 86400000;
            delay = delay + mOneDay;
        }

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest notifRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(NOTIF)
                .build();

        WorkManager.getInstance(mApplicationContext).enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, notifRequest);
    }

    public static void stopWorkRequest(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(NOTIF);
    }
}