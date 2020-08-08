package com.brice_corp.go4lunch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.User;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.DescriptionRestaurantViewModel;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.brice_corp.go4lunch.utils.NotificationWorker;
import com.brice_corp.go4lunch.view.recyclerview.DescriptionRestaurantRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DescriptionRestaurantActivity extends AppCompatActivity {
    private static final String TAG = "DescRestaurantActivity";
    private static final String NOTIF = "notification";
    private static final String WORK_NAME = "work_name";
    private static final String RNAME = "restaurant_name";
    private static final String RID = "restaurant_id";
    private static final String RADDRESS = "restaurant_address";

    private String mRestaurantId;
    private String mRestaurantName;
    private String mRestaurantAddress;

    private ImageView mImage;
    private TextView mName;
    private TextView mFormattedAdr;
    private ImageView mPhone;
    private ImageView mWebsite;
    private RatingBar mRatingBar;
    private ImageView mLikeButton;
    private ImageView mEatTodayButton;
    private FirestoreUserRepository ftUserRepository;
    private RecyclerView mRecyclerView;
    private DescriptionRestaurantRecyclerViewAdapter adapter;
    private DescriptionRestaurantViewModel mViewModel;
    private Query query;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_restaurant);

        mImage = findViewById(R.id.image_description_restaurant);
        mName = findViewById(R.id.textview_name_restaurant);
        mFormattedAdr = findViewById(R.id.textview_type_address_restaurant);
        mPhone = findViewById(R.id.image_call_description);
        mWebsite = findViewById(R.id.image_website_description);
        mRatingBar = findViewById(R.id.ratingbar_restaurant);
        mLikeButton = findViewById(R.id.image_fav_description);
        mEatTodayButton = findViewById(R.id.eat_today_button);
        mRecyclerView = findViewById(R.id.description_recyclerview);

        mViewModel = new ViewModelProvider(DescriptionRestaurantActivity.this).get(DescriptionRestaurantViewModel.class);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle mySavedInstanceState) {
        super.onSaveInstanceState(mySavedInstanceState);
        mySavedInstanceState.putString("restaurant_id", mRestaurantId);
        Log.i(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRestaurantId = savedInstanceState.getString("restaurant_id");
        Log.i(TAG, "onRestoreInstanceState: " + mRestaurantId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        setInformations();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d(TAG, "onStop: ");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("restaurant_id", mRestaurantId);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    //Get id of restaurant from intent
    private void setInformations() {
        Intent intentValue = getIntent();
        RetrofitRepository retrofitRepo = ((MyApplication) getApplication()).getContainerDependencies().getRestrofitRepository();

        ftUserRepository = ((MyApplication) getApplication()).getContainerDependencies().getFirestoreUserRepository();

        retrofitRepo.getRestaurantDetails(Objects.requireNonNull(intentValue.getStringExtra("id"))).observe(DescriptionRestaurantActivity.this,
                new Observer<Restaurant>() {
                    @Override
                    public void onChanged(Restaurant restaurant) {

                        Restaurant currentRestaurant = restaurant.getResult();
                        Log.i(TAG, "onChanged: " + restaurant.getResult());
                        if (currentRestaurant != null) {
                            //Set the id of restaurant
                            if (currentRestaurant.getId() == null) {
                                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                mRestaurantId = sharedPref.getString("restaurant_id", null);
                                Log.i(TAG, "onChanged: setInformations shared pref : id :" + mRestaurantId);
                                if (mRestaurantId == null) {
                                    mRestaurantId = currentRestaurant.getId();
                                    Log.i(TAG, "onChanged: setInformations  getId() 1 : id :" + mRestaurantId);
                                    if (mRestaurantId == null) {
                                        Log.i(TAG, "onChanged: Call setInformations again ");
                                        setInformations();
                                    }
                                }
                            } else {
                                mRestaurantId = currentRestaurant.getId();
                                Log.i(TAG, "onChanged: setInformations  getId() 2 : id :" + mRestaurantId);
                                if (mRestaurantId == null) {
                                    Log.i(TAG, "onChanged: Call setInformations again ");
                                    setInformations();
                                }
                            }

                            //Restaurant image
                            if (currentRestaurant.getPhotos() != null) {
                                Glide.with(DescriptionRestaurantActivity.this)
                                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                                                + currentRestaurant.getPhotos().get(0).getPhotoReference() + "&key="
                                                + "AIzaSyAz_L90GbDp0Hzy_GHjnmxsqPjc1sARRYA")
                                        .centerCrop()
                                        .into(mImage);
                            } else {
                                Glide.with(DescriptionRestaurantActivity.this)
                                        .load(R.drawable.no_image_restaurant)
                                        .centerCrop()
                                        .into(mImage);
                            }

                            //Restaurant name
                            mName.setText(currentRestaurant.getName());
                            mRestaurantName = currentRestaurant.getName();

                            //Restaurant address
                            StringBuilder sb = new StringBuilder(currentRestaurant.getFormattedAddress());
                            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                            mRestaurantAddress = sb.toString();
                            mFormattedAdr.setText(mRestaurantAddress);

                            //Phone call
                            setOnClickCallPhone(currentRestaurant.getFormattedPhoneNumber());

                            //Website
                            setOnClickWebsite(currentRestaurant.getWebsite());

                            //Rating bar
                            setTheRatingBar(currentRestaurant.getRating().floatValue());

                            //Set the like true or false from the firestore
                            getLike();

                            //Get the like button and change it if the user click on
                            onClickLikeButton();

                            //Set the eat today button
                            getEatToday();

                            //Get the restaurant which the person eat today and change the icon if the user click on
                            onClickEatTodayButton();


                            //Set the query
                            query = mViewModel.getQuery(mRestaurantId);

                            //Set the recyclerview
                            setUpRecyclerView();
                        }
                    }
                });
    }

    //Set the OnClickListener on call image and open the call manager of the phone
    private void setOnClickCallPhone(final String phone) {
        //TODO ajouter couleur différente si indisponible
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(DescriptionRestaurantActivity.this, "The restaurant have no phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Set the OnClickListener on website image and open the search engine of the phone
    private void setOnClickWebsite(final String website) {
        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (website == null) {
                    Toast.makeText(DescriptionRestaurantActivity.this, "This restaurant have no website referenced in google map", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = Uri.parse(website);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
    }

    //Set the rating of the restaurant
    private void setTheRatingBar(float rate) {
        if (rate != 0.0f) {
            //Convert 5 stars to 3 stars
            rate = (rate * 3) / 5;
            mRatingBar.setRating(rate);
        } else {
            Log.e(TAG, "setTheRatingBar: no rate from API Place");
        }
    }

    //Check if the user click on like button
    private void onClickLikeButton() {
        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;

                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;
                switch (integer) {
                    case R.drawable.ic_star_black_24dp:
                        ftUserRepository.setUserLikeRestaurantFalse(mRestaurantId);
                        break;

                    case R.drawable.ic_star_border_black_24dp:
                        ftUserRepository.setUserLikeRestaurantTrue(mRestaurantId);
                        break;
                }
            }
        });
    }

    //Get the like in firestore
    private void getLike() {
        Log.i(TAG, "getLike: id restaurant :" + mRestaurantId);
        mViewModel.getTheLikeRestaurant(mRestaurantId).observe(DescriptionRestaurantActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i(TAG, "getLike : " + aBoolean);
                if (aBoolean) {
                    mLikeButton.setImageResource(R.drawable.ic_star_black_24dp);
                    mLikeButton.setTag(R.drawable.ic_star_black_24dp);
                } else {
                    mLikeButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                    mLikeButton.setTag(R.drawable.ic_star_border_black_24dp);
                }
            }
        });
    }

    //Set the eat today button
    private void getEatToday() {
        ftUserRepository.getTheEatToday().observe(DescriptionRestaurantActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String aString) {
                try {
                    if (aString != null && mRestaurantId != null) {
                        if (aString.equals("") || !mRestaurantId.equals(aString)) {
                            mEatTodayButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                            mEatTodayButton.setTag(R.drawable.ic_cancel_black_24dp);
                            mEatTodayButton.setColorFilter(getResources().getColor(R.color.colorFalse));
                            Log.i(TAG, "getEatToday : no : " + aString);
                            stopWorkRequest(DescriptionRestaurantActivity.this);
                        } else {
                            mEatTodayButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            mEatTodayButton.setTag(R.drawable.ic_check_circle_black_24dp);
                            mEatTodayButton.setColorFilter(getResources().getColor(R.color.colorTrue));
                            Log.i(TAG, "getEatToday : yes : " + aString);
                            buildNotification();
                        }
                    } else {
                        Log.i(TAG, "aString = " + aString + " / mRestaurantId = " + mRestaurantId);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onChanged:getEatToday :  ", e);
                }
            }
        });
    }

    //Check if the user click on eat today button
    private void onClickEatTodayButton() {
        mEatTodayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;
                switch (integer) {
                    case R.drawable.ic_cancel_black_24dp:
                        ftUserRepository.setUserEatTodayRestaurantTrue(mRestaurantId, mRestaurantName);
                        break;

                    case R.drawable.ic_check_circle_black_24dp:
                        ftUserRepository.setEatTodayRestaurantFalse();
                        break;
                }
            }
        });
    }

    //Set the recyclerview
    private void setUpRecyclerView() {
        Log.i(TAG, "Enter in setRecyclerview");

        mRecyclerView.addItemDecoration(new DividerItemDecoration(DescriptionRestaurantActivity.this, DividerItemDecoration.VERTICAL));

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class)
                .build();

        adapter = new DescriptionRestaurantRecyclerViewAdapter(options, DescriptionRestaurantActivity.this);
        adapter.startListening();
        mRecyclerView.setAdapter(adapter);
    }

    //Notification builder
    private void buildNotification() {
        Data data = new Data.Builder()
                .putString(RNAME, mRestaurantName)
                .putString(RID, mRestaurantId)
                .putString(RADDRESS, mRestaurantAddress)
                .build();

        Calendar actualDate = Calendar.getInstance();
        Calendar notificationDate = Calendar.getInstance();

        final long nowTimeInMillis = actualDate.getTimeInMillis();

        notificationDate.set(Calendar.HOUR_OF_DAY, 12);
        notificationDate.set(Calendar.MINUTE, 0);
        notificationDate.set(Calendar.SECOND, 0);
        notificationDate.set(Calendar.MILLISECOND, 0);

        final long delay = notificationDate.getTimeInMillis() - nowTimeInMillis;

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

        PeriodicWorkRequest notifRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .addTag(NOTIF)
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, notifRequest);
    }

    public static void stopWorkRequest(Context context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(NOTIF);
    }
}