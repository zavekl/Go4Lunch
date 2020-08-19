package com.brice_corp.go4lunch.repository;

import com.brice_corp.go4lunch.model.projo.Restaurant;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewRepository {
    private ArrayList<String> idPlaceRestaurant = new ArrayList<>();

    public void setRestaurant(ArrayList<String> idPlaceRestaurant) {
        this.idPlaceRestaurant = idPlaceRestaurant;
    }

    public ArrayList<String> getIdPlaceRestaurant() {
        return idPlaceRestaurant;
    }
}
