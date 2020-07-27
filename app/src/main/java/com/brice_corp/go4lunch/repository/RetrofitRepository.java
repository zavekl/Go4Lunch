package com.brice_corp.go4lunch.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.model.projo.Restaurant;

import org.jetbrains.annotations.NotNull;

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
    private ApiGoogleMapRetrofit mApiService;
    private static final String TAG = "RetrofitRepository";

    public RetrofitRepository() {
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

    public LiveData<Restaurant> getRestaurantDetails(@NonNull String idRestaurant) {
        final MutableLiveData<Restaurant> restaurantDetailsResults = new MutableLiveData<>();
        mApiService.getRestaurantDetails(idRestaurant, "AIzaSyAz_L90GbDp0Hzy_GHjnmxsqPjc1sARRYA").enqueue(
                new Callback<Restaurant>() {
                    @Override
                    public void onResponse(@NotNull Call<Restaurant> call, @NotNull Response<Restaurant> response) {
                        if (response.body() != null) {
                            Log.i(TAG, "onResponse: " + response.body());
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
}
