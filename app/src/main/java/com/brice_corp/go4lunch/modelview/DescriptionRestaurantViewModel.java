package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.User;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.utils.AlarmUtils;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by <NIATEL Brice> on <12/07/2020>.
 */

public class DescriptionRestaurantViewModel extends AndroidViewModel {
    private FirestoreUserRepository mFirestoreUserRepository;
    private AlarmUtils alarmUtils;

    public DescriptionRestaurantViewModel(@NonNull Application application) {
        super(application);
        mFirestoreUserRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
        alarmUtils = new AlarmUtils();
    }

    public Query getQuery(String idRestaurant) {
        return mFirestoreUserRepository.getQueryDescription(idRestaurant);
    }

    public MutableLiveData<ArrayList<String>> getEatTodayWorkmates(String idREstaurant) {
        return mFirestoreUserRepository.getEatTodayWorkmates(idREstaurant);
    }

    public void addAlarm(Context context, Intent intent) {
        alarmUtils.addAlarm(context, intent, 0);
    }

    public void cancelAlarm(Context context, Intent intent) {
        alarmUtils.cancelAlarm(context, intent,0);
    }

    public User getCurrenUser(){
        return mFirestoreUserRepository.getUser();
    }
}
