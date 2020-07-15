package com.brice_corp.go4lunch.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.modelview.MapViewModel;
import com.brice_corp.go4lunch.utils.MapStateManager;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.brice_corp.go4lunch.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class MapViewFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MapViewModel mMapViewModel;
    private FloatingActionButton mFab;
    private LocationCallback mLocationCallback;
    private LatLng mLatLng;
    private Boolean mIsCenter = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_map_view, container, false);

        //Set the component
        mFab = view.findViewById(R.id.fab);
        mMapView = view.findViewById(R.id.mapView);

        //Create the map
        mMapView.onCreate(savedInstanceState);

        //Display the map immediately
        mMapView.onResume();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set the view model
        mMapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        //Set the FAB listener
        mFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                setCameraPosition();
            }
        });

        //Detect if the GPS is disable
        mMapViewModel.requestGPSUpdate(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                buildAlertMessageNoGps();
            }
        });

        //Display the menu in fragment
        setHasOptionsMenu(true);
    }

    //Create callback to get the location
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                if (!mIsCenter) {
                    setCameraPosition();

                    //Set this boolean to true in the order not to center the map on the user's position a second time
                    mIsCenter = true;
                }
            }
        };
    }

    //Message to activate the GPS
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Set the camera to the user's position
    private void setCameraPosition() {
        if (mLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(18).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    //If map was saved before, load it
    private void setupMapIfNeeded() {
        try {
            MapsInitializer.initialize(requireContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mGoogleMap = mMap;
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = mGoogleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.style_json));

                    if (!success) {
                        Log.e("TAG", "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("TAG", "Can't find style. Error: ", e);
                }


                MapStateManager mMapStateManager = new MapStateManager(requireContext());
                CameraPosition mPosition = mMapStateManager.getSavedCameraPosition();
                if (mPosition != null) {

                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(mPosition);
                    mGoogleMap.moveCamera(update);
                    mGoogleMap.setMapType(mMapStateManager.getSavedMapType());

                    //Set this boolean to true in the order not to center the map on the user's position
                    mIsCenter = true;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        //Check if a map is saved or not to load it
        setupMapIfNeeded();

        //Start location update
        createLocationCallback();
        mMapViewModel.startLocationUpdates(mLocationCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        MapStateManager mMapStateManager = new MapStateManager(requireContext());
        mMapStateManager.saveMapState(mGoogleMap);

        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
