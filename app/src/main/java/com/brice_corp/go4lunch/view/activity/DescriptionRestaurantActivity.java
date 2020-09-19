package com.brice_corp.go4lunch.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.User;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.DescriptionRestaurantViewModel;
import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.brice_corp.go4lunch.utils.ApplicationPreferences;
import com.brice_corp.go4lunch.view.recyclerview.DescriptionRestaurantRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import static com.brice_corp.go4lunch.utils.Constants.RADDRESS;
import static com.brice_corp.go4lunch.utils.Constants.RID;
import static com.brice_corp.go4lunch.utils.Constants.RNAME;
import static com.brice_corp.go4lunch.utils.WorkerManager.stopWorkRequest;

public class DescriptionRestaurantActivity extends AppCompatActivity {
    private static final String TAG = "DescRestaurantActivity";

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

    private ApplicationPreferences applicationPreferences;

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

    }

    //Get id of restaurant from intent
    private void setInformations() {
        Intent intentValue = getIntent();
        RetrofitRepository retrofitRepo = ((MyApplication) getApplication()).getContainerDependencies().getRestrofitRepository();

        ftUserRepository = ((MyApplication) getApplication()).getContainerDependencies().getFirestoreUserRepository();

        mRestaurantId = intentValue.getStringExtra("id");
        Log.d(TAG, "setInformations: " + mRestaurantId);

        retrofitRepo.getRestaurantDetails(Objects.requireNonNull(mRestaurantId)).observe(DescriptionRestaurantActivity.this,
                new Observer<Restaurant>() {
                    @Override
                    public void onChanged(Restaurant restaurant) {
                        Restaurant currentRestaurant = restaurant.getResult();
                        Log.i(TAG, "onChanged: " + restaurant.getResult().toString());
                        if (currentRestaurant != null) {
                            //Restaurant image
                            if (currentRestaurant.getPhotos() != null) {
                                Glide.with(DescriptionRestaurantActivity.this)
                                        .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                                                + currentRestaurant.getPhotos().get(0).getPhotoReference() + "&key="
                                                + getResources().getString(R.string.place_api_key))
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
        applicationPreferences = new ApplicationPreferences(getApplicationContext());
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
                            applicationPreferences.deleteSharedPrefsData();
                            applicationPreferences.deleteSharedPrefsID();
                        } else {
                            mEatTodayButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                            mEatTodayButton.setTag(R.drawable.ic_check_circle_black_24dp);
                            mEatTodayButton.setColorFilter(getResources().getColor(R.color.colorTrue));
                            Log.i(TAG, "getEatToday : yes : " + aString);
                            buildNotification();
                            applicationPreferences.setSharedPrefsID(mRestaurantId);
                        }
                    } else {
                        Log.d(TAG, "aString = " + aString + " / mRestaurantId = " + mRestaurantId);
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
        mViewModel.setWorker(data);
    }
}