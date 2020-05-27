package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <27/5/2020>.
 */
public class WorkmatesViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> liveData;

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
        FirestoreUserRepository mFirestoreUserRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
        liveData = mFirestoreUserRepository.getUsersName();
    }

    public MutableLiveData<ArrayList<String>> getUsersName() {
        return liveData;
    }
}
