package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.IdPlaceNumber;
import com.brice_corp.go4lunch.model.projo.DistanceMatrix;
import com.brice_corp.go4lunch.model.projo.Period;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewViewModel extends AndroidViewModel {
    private static final String TAG = "ListViewViewModel";
    private final ListViewRepository mListViewRepository;
    private final RetrofitRepository mRetrofitRepository;
    private final ArrayList<Restaurant> mRestaurantList = new ArrayList<>();

    public ListViewViewModel(@NonNull Application application) {
        super(application);
        mListViewRepository = ((MyApplication) application).getContainerDependencies().getListViewRepository();
        mRetrofitRepository = ((MyApplication) application).getContainerDependencies().getRestrofitRepository();
    }

    public ArrayList<IdPlaceNumber> getIdPlaceRestaurantList() {
        return mListViewRepository.getIdPlaceRestaurant();
    }

    public LiveData<Restaurant> getRestaurantDetails(@NonNull String idRestaurant) {
        return mRetrofitRepository.getRestaurantDetails(idRestaurant);
    }

    public ArrayList<Restaurant> filterRestaurantList(ArrayList<Restaurant> restaurants, final String word) {
        mRestaurantList.clear();
        if (restaurants.size() != 0) {
            for (Restaurant restaurant : restaurants) {
                if (restaurant.getName().toLowerCase().contains(word.toLowerCase())) {
                    Log.d(TAG, "sortRestaurantList: " + restaurant.getName());
                    mRestaurantList.add(restaurant);
                }
            }
            Log.d(TAG, "sortRestaurantList: return modified list");
            return mRestaurantList;
        } else {
            return null;
        }
    }

    public void setListViewAdapter(ListViewRestaurantRecyclerViewAdapter adapter) {
        mListViewRepository.setListViewAdapter(adapter);
    }

    public String getOpeningHoursSorted(List<Period> periodArrayList, Boolean openNow) {
        String opening = null;

        int actualNumberDay = LocalDate.now().getDayOfWeek().getValue();
        Log.d(TAG, "getOpeningHoursSorted: actualNumberDay : " + actualNumberDay);

        LocalTime actualTime = LocalTime.now();

        int i = -1;

        for (Period period : periodArrayList) {
            Log.d(TAG, "getOpeningHoursSorted: in loop for each");
            i++;
            int day = period.getOpen().getDay();
            Log.d(TAG, "getOpeningHoursSorted: getDay restaurant : " + day);
            if (actualNumberDay == 7) {
                actualNumberDay = 0;
            }
            if (period.getOpen().getDay() == 0 && period.getOpen().getTime().equals("0000")) {
                Log.d(TAG, "getOpeningHoursSorted: 24/7");
                opening = getApplication().getApplicationContext().getResources().getString(R.string.tv_open7d24h);
                break;
            }

            if (day == actualNumberDay) {
                //Get close time
                LocalTime closeTime = buildTime(period.getClose().getTime());
                Log.d(TAG, "getOpeningHoursSorted close : " + closeTime);

                //Get open time
                LocalTime openTime = buildTime(period.getOpen().getTime());
                Log.d(TAG, "getOpeningHoursSorted open : " + openTime);

                //If already open with openNow
                if (openNow) {
                    Log.d(TAG, "getOpeningHoursSorted openNow");
                    //Open for less than hour hour left
                    if (Duration.between(actualTime, closeTime).abs().getSeconds() < 3600) {
                        Log.d(TAG, "getOpeningHoursSorted: openNow : Close soon");
                        opening = getApplication().getApplicationContext().getResources().getString(R.string.tv_close_soon);
                    }
                    //Open and it will close at closeTime
                    else {
                        Log.d(TAG, "getOpeningHoursSorted: openNow : Open until");
                        opening = getApplication().getApplicationContext().getResources().getString(R.string.tv_open_until, closeTime);
                    }
                }
                //If it is not open with openNow
                else {
                    Log.d(TAG, "getOpeningHoursSorted: no openNow ");
                    //Will open after the actual time
                    if (actualTime.isBefore(openTime)) {
                        Log.d(TAG, "getOpeningHoursSorted: no openNow : Open at");
                        opening = getApplication().getApplicationContext().getResources().getString(R.string.tv_open_at, openTime);
                    }
                    //Will open tomorrow or the second time today
                    else {
                        Log.d(TAG, "getOpeningHoursSorted: no openNow : i+ or tomorrow");
                        //If i+ is the same day break for the second iteration of the list (same day)
                        if (periodArrayList.get(i + 1).getOpen().getDay() == actualNumberDay) {
                            Log.d(TAG, "getOpeningHoursSorted: no openNow : i+ or tomorrow : i+");
                        }
                        //Open tomorrow
                        else {
                            Log.d(TAG, "getOpeningHoursSorted: no openNow : i+ or tomorrow : tomorrow");
                            opening = getApplication().getApplicationContext().getResources().getString(R.string.tv_open_tomorrow);
                        }
                    }
                }
            }
        }
        if (opening == null) {
            return getApplication().getApplicationContext().getResources().getString(R.string.tv_closed);
        } else {
            return opening;
        }
    }

    public LiveData<DistanceMatrix> getDistance(String placeId) {
        return mRetrofitRepository.getDistance(mListViewRepository.getLatlng(), placeId);
    }

    private LocalTime buildTime(String hoursAndMinutes) {
        StringBuilder stringBuilderReturn = new StringBuilder(hoursAndMinutes);
        stringBuilderReturn.insert(2, ":");
        return LocalTime.parse(stringBuilderReturn);
    }

    public ArrayList<Restaurant> sortByRating(ArrayList<Restaurant> list) {
        if (list != null && list.size() != 0) {
            Collections.sort(list, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                    if (restaurant1.getRating() == null) {
                        Log.d(TAG, "compare: " + restaurant1.getName());
                        restaurant1.setRating(0.0);
                    }
                    if (restaurant2.getRating() == null) {
                        Log.d(TAG, "compare: " + restaurant2.getName());
                        restaurant2.setRating(0.0);
                    }
                    return restaurant2.getRating().compareTo(restaurant1.getRating());
                }
            });
            return list;
        } else {
            return null;
        }
    }

    public ArrayList<Restaurant> sortByWorkmates(ArrayList<Restaurant> list) {
        if (list != null && list.size() != 0) {
            Collections.sort(list, new Comparator<Restaurant>() {
                @Override
                public int compare(Restaurant restaurant1, Restaurant restaurant2) {
                    return restaurant2.getNumberWorkamtesEating().compareTo(restaurant1.getNumberWorkamtesEating());
                }
            });
            return list;
        } else {
            return null;
        }
    }
}
