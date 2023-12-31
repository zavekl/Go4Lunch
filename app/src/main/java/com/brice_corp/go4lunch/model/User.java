package com.brice_corp.go4lunch.model;

/**
 * Created by <NIATEL BRICE> on <26/05/2020>.
 */
public class User {
    private String name;
    private String mEmail;
    private String mId;
    private String image;
    private String eatToday;
    private String eatTodayName;

    public User() {
        //Need an empty constructor
    }

    public User(String name, String email, String image) {
        this.name = name;
        this.mEmail = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEatToday() {
        return eatToday;
    }

    public void setEatToday(String eatToday) {
        this.eatToday = eatToday;
    }

    public String getEatTodayName() {
        return eatTodayName;
    }

    public void setEatTodayName(String eatTodayName) {
        this.eatTodayName = eatTodayName;
    }
}