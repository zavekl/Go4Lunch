package com.brice_corp.go4lunch.model;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
public class User {
    private String name;


     public User(){
         //need an empty constructor
     }
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
