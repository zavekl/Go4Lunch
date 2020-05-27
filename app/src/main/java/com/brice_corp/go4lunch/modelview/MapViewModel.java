package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.location.LocationListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.repository.MapRepository;
import com.google.android.gms.location.LocationCallback;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class MapViewModel extends AndroidViewModel {
    private MapRepository mapRepository;

    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRepository = ((MyApplication) application).getContainerDependencies().getMapRepository();
    }

    public void startLocationUpdates(LocationCallback locationCallback) {
        mapRepository.startLocationUpdates(locationCallback);
    }

    public void requestGPSUpdate(LocationListener locationListener) {
        mapRepository.requestGPSUpdate(locationListener);
    }
}
