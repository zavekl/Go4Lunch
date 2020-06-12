package com.brice_corp.go4lunch.model;

/**
 * Created by <NIATEL BRICE> on <26/05/2020>.
 */
public class User {
    private String name;
    private String mEmail;
    private String mId;

    public User() {
        //Need an empty constructor
    }

    public User(String name, String email, String id) {
        this.name = name;
        this.mEmail = email;
        this.mId = id;
    }

    public String getName() {
        return name;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmId() {
        return mId;
    }
}


//public class User {
//    private String name;
//
//
//    public User(){
//        //need an empty constructor
//    }
//    public User(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }
//}
