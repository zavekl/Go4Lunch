package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.utils.ApplicationPreferences;
import com.brice_corp.go4lunch.utils.WorkerManager;

/**
 * Created by <NIATEL Brice> on <12/09/2020>.
 */
public class SettingsViewModel extends AndroidViewModel {
    private final ApplicationPreferences mApplicationPreferences;
    public SettingsViewModel(@NonNull Application application) {
        super(application);
        mApplicationPreferences = new ApplicationPreferences(application.getApplicationContext());
    }

    public void setWorker(long timeNotification) {
        WorkerManager workerManager = new WorkerManager(getApplication().getApplicationContext());
        workerManager.setWorker();
        mApplicationPreferences.setSharedPrefsTime(timeNotification);
    }

}
