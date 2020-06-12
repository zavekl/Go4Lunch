package com.brice_corp.go4lunch.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.modelview.MapViewModel;
import com.brice_corp.go4lunch.utils.AuthenticationUtils;
import com.brice_corp.go4lunch.utils.NotificationUtils;
import com.brice_corp.go4lunch.view.recyclerview.AutoCompleteAdapter;
import com.brice_corp.go4lunch.view.fragment.ListViewFragment;
import com.brice_corp.go4lunch.view.fragment.MapViewFragment;
import com.brice_corp.go4lunch.view.fragment.WorkmatesFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLogi";
    //Components
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private EditText mEditText;
    private RecyclerView mRecyclerView;

    private LocationCallback mLocationCallback;

    private AutoCompleteAdapter autoCompleteAdapter;

    private MapViewModel mMapViewModel;

    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        //Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mConstraintLayout = findViewById(R.id.toolbar_constraint);
        mEditText = findViewById(R.id.search_edit_text);

        //Bottom menu
        setBottomMenu();

        //Navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        setNavigationDrawerMenu();

        //Set the default fragment
        setDefaultFragment();

        //Set listener on search toolbar
        setCLickOnMenuToolbar();

        //Set the autocomplete recyclerview
        mRecyclerView = findViewById(R.id.autocomplete_recyclerview);

        //TODO Notification
        //buildNotification();
    }

    //Manage the back for the navigation drawer
    @Override
    public void onBackPressed() {
        //Handle back click to close menu
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (this.mConstraintLayout.getVisibility() == View.VISIBLE) {
            hideSearchBar();
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    //Create search menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Set the default fragment
    private void setDefaultFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapViewFragment()).commit();
    }

    //Set the bottom menus
    private void setBottomMenu() {
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navigation);
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment mSelectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.map_view_bottom_menu:
                        mSelectedFragment = new MapViewFragment();
                        checkIfSearchBarVisibleAndHideItYes();
                        break;
                    case R.id.list_view__bottom_menu:
                        mSelectedFragment = new ListViewFragment();
                        checkIfSearchBarVisibleAndHideItYes();
                        break;
                    case R.id.workmates__bottom_menu:
                        mSelectedFragment = new WorkmatesFragment();
                        checkIfSearchBarVisibleAndHideItYes();
                        break;
                }
                if (mSelectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mSelectedFragment).commit();
                }
                return true;
            }
        });
    }

    //Set navigation drawer
    private void setNavigationDrawerMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Set the listener for the buttons
        NavigationView navigationView = findViewById(R.id.navigation_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        buildAlertMessageLogout();
                        return false;
                    case R.id.settings:
                        return false;
                    //TODO lancer l'activité settings
                    case R.id.lunch:
                        return false;
                    //TODO lancer l'activité description restaurant
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toolbarButtonNavDrawer();
    }

    //Set the listener for the button
    private void toolbarButtonNavDrawer() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });
    }

    //Alert dialog which use to disconnect the user
    private void buildAlertMessageLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to signout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        AuthenticationUtils.signOut(MainActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.setTitle("Sign Out");
        alert.show();
    }

    //Set listener on search toolbar to reveal the edit text if clicked
    private void setCLickOnMenuToolbar() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_restaurant) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (fragment instanceof MapViewFragment) {
                        revealSearchBar();
                    } else if (fragment instanceof ListViewFragment) {
                        Log.i(TAG, "onMenuItemClick: 2");
                    } else if (fragment instanceof WorkmatesFragment) {
                        Log.i(TAG, "onMenuItemClick: 3");
                    }
                }
                return true;
            }
        });
    }

    //Hide the searchbar
    private void hideSearchBar() {
        mConstraintLayout.setVisibility(View.INVISIBLE);
    }

    //Check if the search bar is visible if true  hide it
    private void checkIfSearchBarVisibleAndHideItYes() {
        if (mConstraintLayout.getVisibility() == View.VISIBLE) {
            hideSearchBar();
        }
    }

    //Reveal the searchbar
    private void revealSearchBar() {
        mConstraintLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    //AutoComplete
    private void setRecyclerviewWithPrediction() {
        if (mLatLng != null) {
            Log.i(TAG, "setRecyclerviewWithPrediction: mLatLng != null");
            RectangularBounds rectangularBounds = RectangularBounds.newInstance(
                    new LatLng(mLatLng.latitude - 0.01, mLatLng.longitude - 0.01),
                    new LatLng(mLatLng.latitude + 0.01, mLatLng.longitude + 0.01));
            Log.i(TAG, "setRecyclerviewWithPrediction: " + rectangularBounds.getNortheast() + "/ " + rectangularBounds.getSouthwest());
            autoCompleteAdapter = new AutoCompleteAdapter(MainActivity.this, rectangularBounds, mLatLng);
            mRecyclerView.setAdapter(autoCompleteAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 4) {
                        autoCompleteAdapter.getFilter().filter(s.toString());
                    }
                }
            });
        } else {
            Log.i(TAG, "setRecyclerviewWithPrediction: mLatLng == null");
        }
    }

    //Get geolocation
    private void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    Log.i(TAG, "GPS "+location.getLatitude());
                    Log.i(TAG, "GPS "+location.getLongitude());
                    mLatLng =new LatLng(location.getLatitude(),location.getLongitude());
                    setRecyclerviewWithPrediction();
                }else{
                    mLatLng= null;
                    getLocation();
                }
            }
        });
    }


    //TODO Notification
    //Notification builder
    private void buildNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification("restaurant", "4 rue de France", "Cécile");
    }

}
