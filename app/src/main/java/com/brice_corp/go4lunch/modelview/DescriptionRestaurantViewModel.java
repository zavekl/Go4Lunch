package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.google.firebase.firestore.Query;

/**
 * Created by <NIATEL Brice> on <12/07/2020>.
 */

public class DescriptionRestaurantViewModel extends AndroidViewModel {
    private FirestoreUserRepository mFirestoreUserRepository;

    public DescriptionRestaurantViewModel(@NonNull Application application) {
        super(application);
        mFirestoreUserRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
    }

    public Query getQuery(String idRestaurant) {
        return mFirestoreUserRepository.getQueryDescription(idRestaurant);
    }
}
