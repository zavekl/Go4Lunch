package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.utils.ApplicationPreferences;
import com.brice_corp.go4lunch.utils.WorkerManager;
import com.google.firebase.firestore.Query;

import javax.annotation.Nonnull;

/**
 * Created by <NIATEL Brice> on <12/07/2020>.
 */

public class DescriptionRestaurantViewModel extends AndroidViewModel {
    private FirestoreUserRepository mFirestoreUserRepository;
    private ApplicationPreferences mApplicationPreferences;

    public DescriptionRestaurantViewModel(@NonNull Application application) {
        super(application);
        mFirestoreUserRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
        mApplicationPreferences = new ApplicationPreferences(application.getApplicationContext());
    }

    public Query getQuery(@Nonnull String idRestaurant) {
        return mFirestoreUserRepository.getQueryDescription(idRestaurant);
    }

    public MutableLiveData<Boolean> getTheLikeRestaurant(final String id) {
        return mFirestoreUserRepository.getTheLikeRestaurant(id);
    }

    public void setWorker(String name, String id, String address) {
        WorkerManager workerManager = new WorkerManager(getApplication().getApplicationContext());
        mApplicationPreferences.setSharedPrefsData(name, id, address);
        workerManager.setWorker();
    }
}
