package com.brice_corp.go4lunch.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.modelview.DescriptionRestaurantViewModel;
import com.brice_corp.go4lunch.modelview.MainActivityViewModel;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.utils.AuthenticationUtils;
import com.brice_corp.go4lunch.utils.Constants;
import com.brice_corp.go4lunch.view.fragment.ListViewFragment;
import com.brice_corp.go4lunch.view.fragment.MapViewFragment;
import com.brice_corp.go4lunch.view.fragment.WorkmatesFragment;
import com.brice_corp.go4lunch.view.recyclerview.AutoCompleteAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.brice_corp.go4lunch.utils.ApplicationPreferences.PREF_ID;
import static com.brice_corp.go4lunch.utils.Constants.DESCRIPTION_RESTAURANT_REQUESTCODE;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLogi";
    private LatLng mLatLng;

    //Components
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ConstraintLayout mConstraintLayout;
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private MainActivityViewModel mMainActivityViewModel;
    private TextWatcher mTextWatcher;
    private TextWatcher mListViewTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set ViewModel
        mMainActivityViewModel = new ViewModelProvider(MainActivity.this).get(MainActivityViewModel.class);

        //Toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mConstraintLayout = findViewById(R.id.toolbar_constraint);
        mEditText = findViewById(R.id.search_edit_text);

        mRecyclerView = findViewById(R.id.autocomplete_recyclerview);

        //Bottom menu
        setBottomMenu();

        //Navigation drawer
        setNavigationDrawerMenu();

        //Set the default fragment
        setDefaultFragment();

        //Set listener on search toolbar
        setCLickOnMenuToolbar();
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (requestCode == DESCRIPTION_RESTAURANT_REQUESTCODE) {
            Log.i(TAG, "onActivityResult: hide RV");
            hideRecyclerview();
        }
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
                        hideRecyclerview();
                        break;
                    case R.id.workmates__bottom_menu:
                        mSelectedFragment = new WorkmatesFragment();
                        checkIfSearchBarVisibleAndHideItYes();
                        hideRecyclerview();
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
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Set the listener for the buttons
        NavigationView navigationView = findViewById(R.id.navigation_drawer_view);

        //Complete the header of nav drawer
        completeNavDrawerHeader(navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        buildAlertMessageLogout();
                        return false;
                    case R.id.settings:
                        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(settingIntent, DESCRIPTION_RESTAURANT_REQUESTCODE);
                        return false;

                    case R.id.lunch:
                        Intent descActivityIntent = new Intent(MainActivity.this, DescriptionRestaurantActivity.class);
                        Log.i(TAG, "onNavigationItemSelected: " + getSharedPrefs());
                        descActivityIntent.putExtra("id", getSharedPrefs());
                        startActivityForResult(descActivityIntent, DESCRIPTION_RESTAURANT_REQUESTCODE);
                        return false;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        toolbarButtonNavDrawer();
    }

    private String getSharedPrefs() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        return sharedPref.getString("restaurant_id", null);
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
                        setTextEditText(0);
                        revealSearchBar();
                        mEditText.setHint(getResources().getString(R.string.enter_name_min_3_letters));
                    } else if (fragment instanceof ListViewFragment) {
                        setTextEditText(1);
                        revealSearchBar();
                        mEditText.setHint(getResources().getString(R.string.enter_name_restaurant));
                    } else if (fragment instanceof WorkmatesFragment) {
                        setTextEditText(2);
                        revealSearchBar();
                        mEditText.setHint(getResources().getString(R.string.enter_name_workmate));
                    }
                }
                return true;
            }
        });
    }

    //Hide the searchbar
    private void hideSearchBar() {
        mEditText.setText("");
        mConstraintLayout.setVisibility(View.INVISIBLE);
    }

    //Check if the search bar is visible if true hide it
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

    //Manage editText
    private void setTextEditText(int i) {
        Log.d(TAG, "setTextEditText: start");
        switch (i) {
            case 0: {
                mEditText.removeTextChangedListener(mListViewTextWatcher);
                setRecyclerviewWithPrediction();
                Log.d(TAG, "setTextEditText: 0");
                break;
            }
            case 1: {
                mEditText.removeTextChangedListener(mTextWatcher);
                sortListView();
                Log.d(TAG, "setTextEditText: 1");
                break;
            }
            case 2: {
                mEditText.removeTextChangedListener(mListViewTextWatcher);
                mEditText.removeTextChangedListener(mTextWatcher);
                Log.d(TAG, "setTextEditText: 2");
                break;
            }
        }
    }

    //Sort listView by the user text
    private void sortListView() {
        mListViewTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mMainActivityViewModel.getListViewAdapter().getFilter().filter(s.toString());
            }
        };
        mEditText.addTextChangedListener(mListViewTextWatcher);

    }

    //Manage AutoComplete
    private void setRecyclerviewWithPrediction() {
        if (mLatLng != null) {
            Log.i(TAG, "setRecyclerviewWithPrediction: mLatLng != null");
            RectangularBounds rectangularBounds = RectangularBounds.newInstance(
                    new LatLng(mLatLng.latitude - 0.01, mLatLng.longitude - 0.01),
                    new LatLng(mLatLng.latitude + 0.01, mLatLng.longitude + 0.01));
            Log.i(TAG, "setRecyclerviewWithPrediction: " + rectangularBounds.getNortheast() + "/ " + rectangularBounds.getSouthwest());
            mAutoCompleteAdapter = new AutoCompleteAdapter(MainActivity.this, rectangularBounds, mLatLng);
            mRecyclerView.setAdapter(mAutoCompleteAdapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            mTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 0) {
                        mAutoCompleteAdapter.getFilter().filter(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 2) {
                        mAutoCompleteAdapter.getFilter().filter(s.toString());
                    }
                }
            };
            mEditText.addTextChangedListener(mTextWatcher);
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
                if (location != null) {
                    Log.i(TAG, "GPS " + location.getLatitude() + " / " + location.getLongitude());
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    mLatLng = null;
                    getLocation();
                }
            }
        });
    }

    //Load user's informations from firestore on drawer header
    private void completeNavDrawerHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        CircleImageView mHeaderImage = headerView.findViewById(R.id.imageview_header);
        TextView mHeaderName = headerView.findViewById(R.id.name_header);
        TextView mHeaderEmail = headerView.findViewById(R.id.email_header);

        FirestoreUserRepository fsUserRepo = ((MyApplication) getApplication()).getContainerDependencies().getFirestoreUserRepository();

        Glide.with(MainActivity.this)
                .load(fsUserRepo.getUser().getImage())
                .centerCrop()
                .into(mHeaderImage);

        mHeaderName.setText(fsUserRepo.getUser().getName());
        mHeaderEmail.setText(fsUserRepo.getUser().getEmail());
    }

    //Hide adapter and clean the adapter
    private void hideRecyclerview() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        if (mAutoCompleteAdapter != null) {
            mAutoCompleteAdapter.cleanAdapter();
        }
        mEditText.setText("");
        mConstraintLayout.setVisibility(View.INVISIBLE);
    }
}
