package com.brice_corp.go4lunch.model;

/**
 * Created by <NIATEL BRICE> on <26/05/2020>.
 */
public class User {
    private String mName;
    private String mHourEat;
    private String mEmail;
    private Restaurant mRestaurant;

    //Need an empty constructor
    public User() {
    }

    public User(String name, String email, String hourEat, Restaurant restaurant) {
        this.mName = name;
        this.mHourEat = hourEat;
        this.mEmail = email;
        this.mRestaurant = restaurant;
    }

    public String getName() {
        return mName;
    }

    public String getmHourEat() {
        return mHourEat;
    }

    public void setmHourEat(String mHourEat) {
        this.mHourEat = mHourEat;
    }

    public String getmEmail() {
        return mEmail;
    }

}
