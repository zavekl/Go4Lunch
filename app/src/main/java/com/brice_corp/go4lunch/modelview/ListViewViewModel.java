package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewViewModel extends AndroidViewModel {
    private ListViewRepository mListViewRepository;
    private RetrofitRepository mRetrofitRepository;

    public ListViewViewModel(@NonNull Application application) {
        super(application);
        mListViewRepository = ((MyApplication) application).getContainerDependencies().getListViewRepository();
        mRetrofitRepository = ((MyApplication) application).getContainerDependencies().getRestrofitRepository();

    }

    public ArrayList<String> getIdPlaceRestaurantList() {
        return mListViewRepository.getIdPlaceRestaurant();
    }

    public LiveData<Restaurant> getRestaurantDetails(@NonNull String idRestaurant) {
        return mRetrofitRepository.getRestaurantDetails(idRestaurant);
    }
}
