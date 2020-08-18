package com.brice_corp.go4lunch.repository;

import com.brice_corp.go4lunch.model.projo.NearByPlaceResults;
import com.brice_corp.go4lunch.model.projo.Restaurant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by <NIATEL Brice> on <15/05/2020>.
 */
public interface ApiGoogleMapRetrofit {

    //Get the details of restaurant by the ID
    @GET("maps/api/place/details/json?")
    Call<Restaurant> getRestaurantDetails(
            @Query("placeid") String placeId,
            @Query("key") String apiKey);

    //Get the list of restaurant near the user position
    @GET("maps/api/place/nearbysearch/json?")
    Call<NearByPlaceResults> getRestaurantListAroundUser(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type_search,
            @Query("sensor") boolean isSensor,
            @Query("key") String keyAPI
    );
}
