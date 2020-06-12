package com.brice_corp.go4lunch.model;

/**
 * Created by <NIATEL BRICE> on <26/05/2020>.
 */
public class User {
    private String mName;
    private String mEmail;
    private String mId;

    public User() {
        //Need an empty constructor
    }

    public User(String name, String email, String id) {
        this.mName = name;
        this.mEmail = email;
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmId() {
        return mId;
    }
}
