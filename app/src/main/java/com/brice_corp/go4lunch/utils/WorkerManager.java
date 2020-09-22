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

    private long mOneDay = 86400000;
    private Data mData;

    private Context mApplicationContext;
    private ApplicationPreferences mApplicationPreferences;

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
            delay = delay + mOneDay;
        }

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
                    .putString(RNAME, mApplicationPreferences.getSharedPrefsDATA().get(0))
                    .putString(RID, mApplicationPreferences.getSharedPrefsDATA().get(1))
                    .putString(RADDRESS, mApplicationPreferences.getSharedPrefsDATA().get(2))
                    .build();
        } else {
            mApplicationPreferences.setSharedPrefsData(mData);
            this.mData = data;
        }
    }

    //TODO Faire en sorte que ce soit sauvegarder et non qu'à chaque fois que desc resto est ouvert que ce soit reset et mit pour midi
    public void setTime(long timeNotification) {
    }
}