package com.brice_corp.go4lunch.modelview;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.brice_corp.go4lunch.di.MyApplication;
import com.brice_corp.go4lunch.model.projo.Period;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewViewModel extends AndroidViewModel {
    private static final String TAG = "ListViewViewModel";
    private ListViewRepository mListViewRepository;
    private RetrofitRepository mRetrofitRepository;
    private ArrayList<Restaurant> mRestaurantList = new ArrayList<>();

    public ListViewViewModel(@NonNull Application application) {
        super(application);
        mListViewRepository = ((MyApplication) application).getContainerDependencies().getListViewRepository();
        mRetrofitRepository = ((MyApplication) application).getContainerDependencies().getRestrofitRepository();
    }

    public ArrayList<String> getIdPlaceRestaurantList() {
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
        String returnString = null;
        int actualNumberDay = LocalDate.now().getDayOfWeek().getValue();
        LocalTime actualTime = LocalTime.now();
        int i = -1;
        for (Period period : periodArrayList) {
            i++;
            if (period.getClose().getDay().equals(actualNumberDay)) {
                Log.d(TAG, "getOpeningHoursSorted: in if acutal date");
                //Get close time
                LocalTime close = buildTime(period.getClose().getTime());
                Log.d(TAG, "getOpeningHoursSorted: in if acutal date : close : " + close);

                //Get open time
                LocalTime open = buildTime(period.getOpen().getTime());
                Log.d(TAG, "getOpeningHoursSorted: in if acutal date : open : " + open);

                //TODO GERER LES DEUX OUVERTURES PAR JOUR
                //If close
                if (!openNow) {
                    //If time is before open hour
                    if (actualTime.isBefore(open)) {
                        returnString = "Open at " + open;
                    }
                    //If time is after close hour
                    else {
                        //TODO i + 1? / gerer le passage de dimanche à lundi
                        LocalTime tomorrowOpenTime = buildTime(periodArrayList.get(i).getOpen().getTime());
                        returnString = "Open tomorrow at " + tomorrowOpenTime;
                    }
                    //If Open
                } else {
                    //
                    if (Duration.between(actualTime, close).abs().getSeconds() < 3600) {
                        returnString = "Close soon";
                    } else {
                        returnString = "Close at " + close;
                    }
                }
            }
        }
        return returnString;
    }

    //TODO METTRE DANS UNE CLASSE AVEC LE RATING
    private LocalTime buildTime(String hoursAndMinutes) {
        StringBuilder stringBuilderReturn = new StringBuilder(hoursAndMinutes);
        stringBuilderReturn.insert(2, ":");
        return LocalTime.parse(stringBuilderReturn);
    }
}
