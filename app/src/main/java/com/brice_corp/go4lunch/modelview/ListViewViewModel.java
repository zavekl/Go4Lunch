package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewViewModel extends AndroidViewModel {
    private static final String TAG = "ListViewViewModel";
    private ListViewRepository mListViewRepository;
    private RetrofitRepository mRetrofitRepository;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();

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

    public ArrayList<Restaurant> filterRestaurantList(ArrayList<Restaurant> restaurants, final String word) {
        mRestaurantList.clear();
        if (restaurants.size() != 0) {
            for (Restaurant restaurant : restaurants) {
                if (restaurant.getName().toLowerCase().contains(word.toLowerCase())) {
                    Log.d(TAG, "sortRestaurantList: " + restaurant.getName());
                    mRestaurantList.add(restaurant);
                }
            }
            Log.d(TAG, "sortRestaurantList: return modified list");
            return mRestaurantList;
        } else {
            return null;
        }
    }

    public void setListViewAdapter(ListViewRestaurantRecyclerViewAdapter adapter) {
        mListViewRepository.setListViewAdapter(adapter);
    }
}
