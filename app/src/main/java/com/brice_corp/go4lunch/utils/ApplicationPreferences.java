package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.work.Data;

import java.util.ArrayList;
import java.util.Arrays;

import static com.brice_corp.go4lunch.utils.Constants.RADDRESS;
import static com.brice_corp.go4lunch.utils.Constants.RID;
import static com.brice_corp.go4lunch.utils.Constants.RNAME;

/**
 * Created by <NIATEL Brice> on <05/09/2020>.
 */
public class ApplicationPreferences {
    private static final String TAG = "ApplicationPreferences";
    private static final String PREF_ID = "GO4LUNCH_ID";
    private static final String PREF_DATA = "GO4LUNCH_DATA";
    private static final String PREF_TIME = "GO4LUNCH_TIME";

    private Context mContext;

    public ApplicationPreferences(Context Context) {
        this.mContext = Context;
    }

    public void deleteSharedPrefsID() {
        Log.d(TAG, "deleteSharedPrefsID: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    public void setSharedPrefsID(String idRestaurant) {
        Log.d(TAG, "setSharedPrefsID: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("restaurant_id", idRestaurant);
        editor.apply();
    }

    public String getSharedPrefsID() {
        SharedPreferences sharedPref = mContext.getApplicationContext().getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);
        return sharedPref.getString("restaurant_id", null);
    }

    public void setSharedPrefsData(String name, String id, String address) {
        Log.d(TAG, "setSharedPrefsData: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("rname", name);
        editor.putString("raddress", id);
        editor.putString("rid", address);

        editor.apply();
    }

    public void deleteSharedPrefsData() {
        Log.d(TAG, "deleteSharedPrefsData: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    ArrayList<String> getSharedPrefsDATA() {
        SharedPreferences sharedPref = mContext.getApplicationContext().getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
        return new ArrayList<>(Arrays.asList(sharedPref.getString("rname", null), sharedPref.getString("raddress", null),
                sharedPref.getString("rid", null)));
    }

    public void deleteSharedPrefsTime() {
        Log.d(TAG, "deleteSharedPrefsTime: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    public void setSharedPrefsTime(long time) {
        Log.d(TAG, "setSharedPrefsTime: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("time", time);
        editor.apply();
    }

    public long getSharedPrefsTime() {
        SharedPreferences sharedPref = mContext.getApplicationContext().getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE);
        return sharedPref.getLong("time", 0);
    }


}
