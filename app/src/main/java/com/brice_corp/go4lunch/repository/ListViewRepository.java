package com.brice_corp.go4lunch.repository;

import com.brice_corp.go4lunch.model.IdPlaceNumber;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by <NIATEL Brice> on <18/08/2020>.
 */
public class ListViewRepository {
    private ArrayList<IdPlaceNumber> idPlaceRestaurant = new ArrayList<>();
    private ListViewRestaurantRecyclerViewAdapter mAdapter;
    private LatLng mLatlng;

    public void setIdPlaceRestaurant(ArrayList<IdPlaceNumber> idPlaceRestaurant) {
        this.idPlaceRestaurant = idPlaceRestaurant;
    }

    public ArrayList<IdPlaceNumber> getIdPlaceRestaurant() {
        return idPlaceRestaurant;
    }

    public ListViewRestaurantRecyclerViewAdapter getListViewAdapter() {
        return mAdapter;
    }

    public void setListViewAdapter(ListViewRestaurantRecyclerViewAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setLatlng(LatLng latlng) {
        this.mLatlng = latlng;
    }

    public LatLng getLatlng() {
        return mLatlng;
    }
}
