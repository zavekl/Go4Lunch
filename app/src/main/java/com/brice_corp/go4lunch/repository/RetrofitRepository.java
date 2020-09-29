package com.brice_corp.go4lunch.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.BuildConfig;
import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.projo.DistanceMatrix;
import com.brice_corp.go4lunch.model.projo.NearByPlaceResults;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by <NIATEL Brice> on <15/06/2020>.
 */
public class RetrofitRepository {
    private static final String TAG = "RetrofitRepository";

    private Context mContext;

    private static final int RADIUS = 2500;
    private static final boolean SENSOR = true;
    private static final String TYPESEARCH = "restaurant";

    private ApiGoogleMapRetrofit mApiService;

    public RetrofitRepository(Context context) {
        mContext = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        // Build retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        mApiService = retrofit.create(ApiGoogleMapRetrofit.class);
    }

    //Get the restaurant information with Place API
    public LiveData<Restaurant> getRestaurantDetails(@NonNull String idRestaurant) {
        final MutableLiveData<Restaurant> restaurantDetailsResults = new MutableLiveData<>();
        mApiService.getRestaurantDetails(idRestaurant, mContext.getResources().getString(R.string.place_api_key)).enqueue(
                new Callback<Restaurant>() {
                    @Override
                    public void onResponse(@NotNull Call<Restaurant> call, @NotNull Response<Restaurant> response) {
                        if (response.body() != null) {
                            Log.i(TAG, "onResponse: " + response.body().getResult());
                            restaurantDetailsResults.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Restaurant> call, @NotNull Throwable t) {
                        t.printStackTrace();
                    }
                }
        );
        return restaurantDetailsResults;
    }

    public LiveData<NearByPlaceResults> getRestaurantListAroundUser(String latLng) {
        final MutableLiveData<NearByPlaceResults> liveData = new MutableLiveData<>();
        final Call<NearByPlaceResults> call = mApiService.getRestaurantListAroundUser(latLng, RADIUS, TYPESEARCH, SENSOR, mContext.getResources().getString(R.string.place_api_key));

        call.enqueue(new Callback<NearByPlaceResults>() {
            @Override
            public void onResponse(@NotNull Call<NearByPlaceResults> call, @NotNull Response<NearByPlaceResults> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<NearByPlaceResults> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
        return liveData;
    }

    public LiveData<DistanceMatrix> getDistance(LatLng latLng, String placeId) {
        final MutableLiveData<DistanceMatrix> liveData = new MutableLiveData<>();
        Log.d(TAG, "getDistance: " + latLng + placeId);
        final Call<DistanceMatrix> call = mApiService.getDistance(latLng.latitude + "," + latLng.longitude, "place_id:" + placeId, mContext.getResources().getString(R.string.map_api_key));

        call.enqueue(new Callback<DistanceMatrix>() {
            @Override
            public void onResponse(@NonNull Call<DistanceMatrix> call, @NonNull Response<DistanceMatrix> response) {
                liveData.setValue(response.body());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<DistanceMatrix> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        return liveData;
    }
}
