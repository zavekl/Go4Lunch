package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.utils.WorkerManager;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
public class SettingsViewModel extends AndroidViewModel {
    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public void setWorker(long timeNotification) {
        WorkerManager workerManager = new WorkerManager(getApplication().getApplicationContext());
        workerManager.setData(null);
        workerManager.setTime(timeNotification);
        workerManager.setWorker();
    }
}
