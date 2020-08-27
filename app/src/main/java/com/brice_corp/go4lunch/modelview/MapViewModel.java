package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.projo.NearByPlaceResults;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.MapRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class MapViewModel extends AndroidViewModel {
    private MapRepository mMapRepository;
    private RetrofitRepository mRetrofitRepository;
    private ListViewRepository mListViewRepository;
    private FirestoreUserRepository mFirestoreRepository;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mMapRepository = ((MyApplication) application).getContainerDependencies().getMapRepository();
        mRetrofitRepository = ((MyApplication) application).getContainerDependencies().getRestrofitRepository();
        mListViewRepository = ((MyApplication) application).getContainerDependencies().getListViewRepository();
        mFirestoreRepository = ((MyApplication) application).getContainerDependencies().getFirestoreUserRepository();
    }

    public void startLocationUpdates(LocationCallback locationCallback) {
        mMapRepository.startLocationUpdates(locationCallback);
    }

    public void stopLocationUpdates(LocationCallback locationCallback) {
        mMapRepository.stopLocationUpdates(locationCallback);
    }


    public void requestGPSUpdate(LocationListener locationListener) {
        mMapRepository.requestGPSUpdate(locationListener);
    }

    public LiveData<NearByPlaceResults> getRestaurantListAroundUser(LatLng latLng) {
        return mRetrofitRepository.getRestaurantListAroundUser(latLng.latitude + "," + latLng.longitude);
    }

    public void setRestaurantListView(ArrayList<String> idPlaceRestaurant) {
        mListViewRepository.setIdPlaceRestaurant(idPlaceRestaurant);
    }

    public Task<QuerySnapshot> getUsersDocuments() {
        return mFirestoreRepository.getUsersDocuments();
    }
}
