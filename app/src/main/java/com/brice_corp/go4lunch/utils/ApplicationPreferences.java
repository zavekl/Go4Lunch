package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by <NIATEL Brice> on <05/09/2020>.
 */
public class ApplicationPreferences {
    private static final String TAG = "ApplicationPreferences";
    public static final String PREF_ID = "GO4LUNCH_ID";

    private Context mContext;

    public ApplicationPreferences(Context Context) {
        this.mContext = Context;
    }

    public void deleteSharedPrefs() {
        Log.d(TAG, "deleteSharedPrefs: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    public void setSharedPrefs(String idRestaurant) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("restaurant_id", idRestaurant);
        editor.apply();
    }
}
