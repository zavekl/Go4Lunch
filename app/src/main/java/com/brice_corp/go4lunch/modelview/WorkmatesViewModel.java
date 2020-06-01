package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.google.firebase.firestore.Query;

/**
 * Created by <NIATEL Brice> on <27/05/2020>.
 */
public class WorkmatesViewModel extends AndroidViewModel {

    private Query query;

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
        FirestoreUserRepository mFirestoreUserRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
        query = mFirestoreUserRepository.getQuery();
    }

    public Query getQuery() {
        return query;
    }
}
