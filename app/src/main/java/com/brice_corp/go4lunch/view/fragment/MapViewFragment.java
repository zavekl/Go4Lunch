package com.brice_corp.go4lunch.view.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.projo.NearByPlaceResults;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.MapViewModel;
import com.brice_corp.go4lunch.utils.MapStateManager;
import com.brice_corp.go4lunch.view.activity.DescriptionRestaurantActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.brice_corp.go4lunch.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class MapViewFragment extends Fragment {
    private static final String TAG = "MapViewFragment";
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private MapViewModel mMapViewModel;
    private FloatingActionButton mFab;
    private LocationCallback mLocationCallback;
    private LatLng mLatLng;
    private Boolean mIsCenter = false;
    private ArrayList<String> mIdPlaceRestaurant = new ArrayList<>();
    private Boolean iconMarker = false;

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
                setCameraPosition();
            }

            @Override
            public void onProviderDisabled(String provider) {
                buildAlertMessageNoGps();
            }
        });

        //Display the menu in fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            Log.d(TAG, "onActivityResult : 10");
            createLocationCallback();
        }
    }

    //Create callback to get the location
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                if (!mIsCenter) {
                    Log.d(TAG, "onLocationResult: center camera");
                    setCameraPosition();
                    //Set this boolean to true in the order not to center the map on the user's position a second time
                    mIsCenter = true;
                    new GetTasks(MapViewFragment.this).execute();
                    //getUserTodayRestaurant();
                    setMarkerOnCLick();
                }
            }
        };
    }

    //Convert a vector image xml into bitmap descriptor for googlemap icon
    private BitmapDescriptor bitmapDescriptorFromVector() {
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_map_marker);
        if (vectorDrawable != null) {
            if (iconMarker) {
                Drawable wrappedDrawable = DrawableCompat.wrap(vectorDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.GREEN);
            } else {
                Drawable wrappedDrawable = DrawableCompat.wrap(vectorDrawable);
                DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.colorPrimary));
            }
        }

        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Get the id of restaurant if workmates eat in today
    private void getUserTodayRestaurant() {

        Log.d(TAG, "getUserTodayRestaurant: start display");
        mMapViewModel.getUsersDocuments().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document != null) {
                        if (document.get("eatToday") != null && !Objects.requireNonNull(document.get("eatToday")).equals("")) {
                            mIdPlaceRestaurant.add(Objects.requireNonNull(document.get("eatToday")).toString());
                            Log.d(TAG, "onSuccess: element list :  " + Objects.requireNonNull(document.get("eatToday")).toString());
                        } else {
                            Log.d(TAG, "onSuccess: document.get(\"eatToday\") = null or empty");
                        }
                    } else {
                        Log.d(TAG, "onSuccess: document = null");
                    }
                }
            }
        });
    }

    //Display markers of the restaurant which are around the user position
    private void getRestaurantListAroundUser() {
        Log.d(TAG, "getRestaurantListAroundUser: start");
        mMapViewModel.getRestaurantListAroundUser(mLatLng).observe(MapViewFragment.this, new Observer<NearByPlaceResults>() {
            @Override
            public void onChanged(NearByPlaceResults nearByPlaceResults) {
                Log.d(TAG, "onChanged: start :" + nearByPlaceResults.toString());
                ArrayList<String> idPlaceRestaurant = new ArrayList<>();

                Log.d(TAG, "getRestaurantListAroundUser : onChanged: list : " + mIdPlaceRestaurant);
                for (Restaurant result : nearByPlaceResults.getResults()) {
                    for (String idPlace : mIdPlaceRestaurant) {
                        if (idPlace.equals(result.getPlaceId())) {
                            Log.d(TAG, "getRestaurantListAroundUser: onChanged restaurant green : " + idPlace);
                            iconMarker = true;
                            break;
                        }
                        break;
                    }
                    LatLng latLngRestaurant = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
                    mGoogleMap.addMarker(new MarkerOptions().position(latLngRestaurant).title(result.getName()).icon(bitmapDescriptorFromVector())).setTag(result.getPlaceId());
                    idPlaceRestaurant.add(result.getPlaceId());
                    iconMarker = false;
                }
                mMapViewModel.setRestaurantListView(idPlaceRestaurant);
            }
        });
    }

    //When the user click on icon that will start the description of the restaurant
    private void setMarkerOnCLick() {
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(requireContext(), DescriptionRestaurantActivity.class);
                intent.putExtra("id", (String) marker.getTag());
                startActivityForResult(intent, 10);
                return false;
            }
        });
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
            CameraPosition cameraPosition = new CameraPosition.Builder().target(mLatLng).zoom(16).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Log.d(TAG, "setCameraPosition: done");
        }
    }

    //If map was saved before, load it
    private void setupMapIfNeeded() {
        Log.d(TAG, "setupMapIfNeeded: start");
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
                mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

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

                //Clear the icons at each start of map
                mGoogleMap.clear();
                Log.d(TAG, "onMapReady: map saved");
                if (mPosition != null) {
                    Log.d(TAG, "onMapReady: in if : set camera position");
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(mPosition);
                    mGoogleMap.moveCamera(update);
                    mGoogleMap.setMapType(mMapStateManager.getSavedMapType());
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
        Log.d(TAG, "onPause: ");
        MapStateManager mMapStateManager = new MapStateManager(requireContext());
        mMapStateManager.saveMapState(mGoogleMap);

        mMapView.onPause();
        mMapViewModel.stopLocationUpdates(mLocationCallback);
        mIsCenter = false;

        mIdPlaceRestaurant.clear();
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

    private static class GetTasks extends AsyncTask<Void, Void, Void> {
        private WeakReference<MapViewFragment> activityReference;

        GetTasks(MapViewFragment mapViewFragment) {
            activityReference = new WeakReference<>(mapViewFragment);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MapViewFragment mapViewFragment = activityReference.get();
            if (mapViewFragment != null) {
                mapViewFragment.getUserTodayRestaurant();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MapViewFragment mapViewFragment = activityReference.get();
            if (mapViewFragment != null) {
                mapViewFragment.getRestaurantListAroundUser();
            }
        }
    }
}
