package com.brice_corp.go4lunch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.brice_corp.go4lunch.model.projo.Close;
import com.brice_corp.go4lunch.model.projo.Distance;
import com.brice_corp.go4lunch.model.projo.DistanceMatrix;
import com.brice_corp.go4lunch.model.projo.Duration;
import com.brice_corp.go4lunch.model.projo.Element;
import com.brice_corp.go4lunch.model.projo.NearByPlaceResults;
import com.brice_corp.go4lunch.model.projo.Open;
import com.brice_corp.go4lunch.model.projo.OpeningHours;
import com.brice_corp.go4lunch.model.projo.Period;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.model.projo.Row;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <14/10/2020>.
 */

public class RetrofitRepoTest {
    private final String ID_RESTAURANT = "xxx";
    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    private final RetrofitRepository mRetrofitRepository = Mockito.mock(RetrofitRepository.class);

    private MutableLiveData<Restaurant> mGetDetailRestaurantLiveData;

    private MutableLiveData<DistanceMatrix> mGetDistanceLiveData;

    private MutableLiveData<NearByPlaceResults> mGetRestaurantListAroundUserLiveData;

    @Before
    public void setUp() {
        //getDetailRestaurant()
        mGetDetailRestaurantLiveData = new MutableLiveData<>();
        Mockito.doReturn(mGetDetailRestaurantLiveData).when(mRetrofitRepository).getRestaurantDetails(ID_RESTAURANT);

        //getDistance()
        mGetDistanceLiveData = new MutableLiveData<>();
        Mockito.doReturn(mGetDistanceLiveData).when(mRetrofitRepository).getDistance(new LatLng(0.6521454, -10.589841), ID_RESTAURANT);

        //getRestaurantListAroundUser()
        mGetRestaurantListAroundUserLiveData = new MutableLiveData<>();
        Mockito.doReturn(mGetRestaurantListAroundUserLiveData).when(mRetrofitRepository).getRestaurantListAroundUser(ID_RESTAURANT);
    }


    @Test
    public void getDetailRestaurant() throws InterruptedException {
        OpeningHours openingHours = new OpeningHours();
        openingHours.setOpenNow(false);
        List<Period> periodList = Arrays.asList(
                new Period(new Close(0, ""), new Open(0, "")),
                new Period(new Close(1, "23h"), new Open(1, "18h")),
                new Period(new Close(2, "23h"), new Open(2, "18h")),
                new Period(new Close(3, "23h"), new Open(3, "18h")),
                new Period(new Close(4, "23h"), new Open(4, "18h")),
                new Period(new Close(5, "23h"), new Open(5, "18h")),
                new Period(new Close(6, "23h"), new Open(6, "18h"))
        );

        openingHours.setPeriods(periodList);
        Restaurant restaurantResult = new Restaurant("Pizza", "4 cour des lilas, Paris", 2.5, openingHours, null, ID_RESTAURANT, "0.6km", 4);

        mGetDetailRestaurantLiveData.setValue(restaurantResult);

        Restaurant result = LiveDataUtils.getOrAwaitValue(mRetrofitRepository.getRestaurantDetails(ID_RESTAURANT));

        Assert.assertNotNull(result);
        Assert.assertEquals(restaurantResult, result);
        Assert.assertEquals("Pizza", result.getName());
        Assert.assertEquals("4 cour des lilas, Paris", result.getAdrAddress());
        Assert.assertEquals("2.5", result.getRating().toString());
        Assert.assertEquals(false, result.getOpeningHours().getOpenNow());

        Assert.assertEquals("", result.getOpeningHours().getPeriods().get(0).getClose().getTime());
        Assert.assertEquals("", result.getOpeningHours().getPeriods().get(0).getOpen().getTime());
        Assert.assertEquals("23h", result.getOpeningHours().getPeriods().get(1).getClose().getTime());
        Assert.assertEquals("18h", result.getOpeningHours().getPeriods().get(1).getOpen().getTime());

        Assert.assertEquals("0", result.getOpeningHours().getPeriods().get(0).getOpen().getDay().toString());
        Assert.assertEquals("1", result.getOpeningHours().getPeriods().get(1).getOpen().getDay().toString());
        Assert.assertEquals("2", result.getOpeningHours().getPeriods().get(2).getOpen().getDay().toString());
        Assert.assertEquals("3", result.getOpeningHours().getPeriods().get(3).getOpen().getDay().toString());
        Assert.assertEquals("4", result.getOpeningHours().getPeriods().get(4).getOpen().getDay().toString());
        Assert.assertEquals("5", result.getOpeningHours().getPeriods().get(5).getOpen().getDay().toString());
        Assert.assertEquals("6", result.getOpeningHours().getPeriods().get(6).getOpen().getDay().toString());

        Assert.assertNull(result.getPhotos());
        Assert.assertEquals("0.6km", result.getDistanceMeter());
        Assert.assertEquals("4", result.getNumberWorkamtesEating().toString());
    }

    @Test
    public void getDistance() throws InterruptedException {
        DistanceMatrix distanceMatrix = new DistanceMatrix();
        List<Row> distance = Collections.singletonList(
                new Row(Collections.singletonList(new Element(new Distance("1.3km", 1300), new Duration("15m", 900), "true"))));
        distanceMatrix.setRows(distance);

        mGetDistanceLiveData.setValue(distanceMatrix);

        DistanceMatrix result = LiveDataUtils.getOrAwaitValue(mRetrofitRepository.getDistance(new LatLng(0.6521454, -10.589841), ID_RESTAURANT));

        Assert.assertEquals("1.3km", result.getRows().get(0).getElements().get(0).getDistance().getText());
        Assert.assertEquals(1300, result.getRows().get(0).getElements().get(0).getDistance().getValue());
        Assert.assertEquals("15m", result.getRows().get(0).getElements().get(0).getDuration().getText());
        Assert.assertEquals(900, result.getRows().get(0).getElements().get(0).getDuration().getValue());
    }

    @Test
    public void getRestaurantListAroundUser() throws InterruptedException {
        OpeningHours openingHours = new OpeningHours();
        openingHours.setOpenNow(false);
        List<Period> periodList = Arrays.asList(
                new Period(new Close(0, ""), new Open(0, "")),
                new Period(new Close(1, "23h"), new Open(1, "18h")),
                new Period(new Close(2, "23h"), new Open(2, "18h")),
                new Period(new Close(3, "23h"), new Open(3, "18h")),
                new Period(new Close(4, "23h"), new Open(4, "18h")),
                new Period(new Close(5, "23h"), new Open(5, "18h")),
                new Period(new Close(6, "23h"), new Open(6, "18h"))
        );
        NearByPlaceResults nearByPlaceResults = new NearByPlaceResults();
        List<Restaurant> restaurantList = Arrays.asList(
                new Restaurant("Restaurant 1", "1 cour des fleurs, Paris", 5.0, openingHours, null, ID_RESTAURANT, "0.9km", 1),
                new Restaurant("Restaurant 2", "2 cour des arbres, Nantes", 4.5, openingHours, null, ID_RESTAURANT, "1.5km", 0),
                new Restaurant("Restaurant 3", "3 cour des maisons, Rennes", 3.4, openingHours, null, ID_RESTAURANT, "2.6km", 2),
                new Restaurant("Restaurant 4", "4 cour des photos, Grenoble", 2.8, openingHours, null, ID_RESTAURANT, "1.2km", 0),
                new Restaurant("Restaurant 5", "5 cour des tables, Lyon", 1.2, openingHours, null, ID_RESTAURANT, "0.5km", 4));

        nearByPlaceResults.setResults(restaurantList);
        mGetRestaurantListAroundUserLiveData.setValue(nearByPlaceResults);

        NearByPlaceResults result = LiveDataUtils.getOrAwaitValue(mGetRestaurantListAroundUserLiveData);
        Assert.assertNotNull(result);
        Assert.assertEquals(restaurantList.size(), result.getResults().size());
        Assert.assertEquals("Restaurant 1", result.getResults().get(0).getName());
        Assert.assertEquals("Restaurant 2", result.getResults().get(1).getName());
        Assert.assertEquals("Restaurant 3", result.getResults().get(2).getName());
        Assert.assertEquals("Restaurant 4", result.getResults().get(3).getName());
        Assert.assertEquals("Restaurant 5", result.getResults().get(4).getName());

        Assert.assertEquals("1 cour des fleurs, Paris", result.getResults().get(0).getAdrAddress());
        Assert.assertEquals("2 cour des arbres, Nantes", result.getResults().get(1).getAdrAddress());
        Assert.assertEquals("3 cour des maisons, Rennes", result.getResults().get(2).getAdrAddress());
        Assert.assertEquals("4 cour des photos, Grenoble", result.getResults().get(3).getAdrAddress());
        Assert.assertEquals("5 cour des tables, Lyon", result.getResults().get(4).getAdrAddress());

        Assert.assertEquals("5.0", result.getResults().get(0).getRating().toString());
        Assert.assertEquals("4.5", result.getResults().get(1).getRating().toString());
        Assert.assertEquals("3.4", result.getResults().get(2).getRating().toString());
        Assert.assertEquals("2.8", result.getResults().get(3).getRating().toString());
        Assert.assertEquals("1.2", result.getResults().get(4).getRating().toString());

        Assert.assertEquals(ID_RESTAURANT, result.getResults().get(0).getPlaceId());

        Assert.assertNull(result.getResults().get(0).getPhotos());

        Assert.assertEquals("1", result.getResults().get(0).getNumberWorkamtesEating().toString());
        Assert.assertEquals("0", result.getResults().get(1).getNumberWorkamtesEating().toString());
        Assert.assertEquals("2", result.getResults().get(2).getNumberWorkamtesEating().toString());
        Assert.assertEquals("0", result.getResults().get(3).getNumberWorkamtesEating().toString());
        Assert.assertEquals("4", result.getResults().get(4).getNumberWorkamtesEating().toString());
    }
}
