package com.brice_corp.go4lunch.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by <BRICE NIATEL> on <07/04/2020>.
 */
public class Restaurant {
    private String mName;
    private String mFypeFood;
    private String mAddress;
    private String mSchedule;
    private String id;
    //TODO ID Resto

    public Restaurant(String id, String name, String typeFood, String address, String schedule) {
        this.id = id;
        this.mName = name;
        this.mFypeFood = typeFood;
        this.mAddress = address;
        this.mSchedule = schedule;
    }

    public String getmName() {
        return mName;
    }

    public String getmFypeFood() {
        return mFypeFood;
    }

    public String getmAddress() {
        return mAddress;
    }

    public String getmSchedule() {
        return mSchedule;
    }

    public static List<Restaurant> listGetFakesRestaurants() {
        return Arrays.asList(
                new Restaurant("","Chez Roger", "Français", "4 rue des Baobabs", "Bientôt ouvert"),
                new Restaurant("","Chez Hans", "Allemand", "8 rue de Panzer", "Bientôt fermé"));
    }
}
