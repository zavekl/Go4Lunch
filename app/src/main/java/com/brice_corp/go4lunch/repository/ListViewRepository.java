package com.brice_corp.go4lunch.repository;

import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewRepository {
    private ArrayList<String> idPlaceRestaurant = new ArrayList<>();
    private ListViewRestaurantRecyclerViewAdapter mAdapter;

    public void setIdPlaceRestaurant(ArrayList<String> idPlaceRestaurant) {
        this.idPlaceRestaurant = idPlaceRestaurant;
    }

    public ArrayList<String> getIdPlaceRestaurant() {
        return idPlaceRestaurant;
    }

    public ListViewRestaurantRecyclerViewAdapter getListViewAdapter() {
        return mAdapter;
    }

    public void setListViewAdapter(ListViewRestaurantRecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
