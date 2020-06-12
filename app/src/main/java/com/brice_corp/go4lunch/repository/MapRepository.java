package com.brice_corp.go4lunch.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by <NIATEL BRICE> on <01/05/2020>.
 */
public class MapRepository {
    private static final int UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager mLocationManager;

    public MapRepository(Context context) {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public void startLocationUpdates(LocationCallback locationCallback) {
        fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper());
    }

    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return mLocationRequest;
    }

    @SuppressLint("MissingPermission")
    public void requestGPSUpdate(LocationListener locationListener) {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000000, 1000000, locationListener);
    }
}
