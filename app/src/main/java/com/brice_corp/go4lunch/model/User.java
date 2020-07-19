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

    //TODO Créer un nouveau champ avec le nom du resto pour l'afficher dans les workamtes

    public User() {
        //Need an empty constructor
    }

    public User(String name, String email, String image, String id) {
        this.name = name;
        this.mEmail = email;
        this.image = image;
        this.mId = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getImage() {
        return image;
    }

    public String getmId() {
        return mId;
    }

    public String getEatToday() {
        return eatToday;
    }

    public String getEatTodayName() {
        return eatTodayName;
    }

    public void setTodayEat(String todayEat) {
        this.eatToday = todayEat;
    }
}