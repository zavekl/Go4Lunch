package com.brice_corp.go4lunch.modelview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

/**
 * Created by <NIATEL Brice on <19/08/2020>.
 */
public class MainActivityViewModel extends AndroidViewModel {
    private ListViewRepository mListViewRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mListViewRepository = ((MyApplication) application).getContainerDependencies().getListViewRepository();
    }

    public ListViewRestaurantRecyclerViewAdapter getListViewAdapter() {
        return mListViewRepository.getListViewAdapter();
    }
}
