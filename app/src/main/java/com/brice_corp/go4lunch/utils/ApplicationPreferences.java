package com.brice_corp.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.brice_corp.go4lunch.di.MyApplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.brice_corp.go4lunch.utils.Constants.RADDRESS;
import static com.brice_corp.go4lunch.utils.Constants.RID;
import static com.brice_corp.go4lunch.utils.Constants.RNAME;

/**
 * Created by <NIATEL Brice> on <05/09/2020>.
 */
public class ApplicationPreferences {
    private static final String TAG = "ApplicationPreferences";
    public static final String PREF_ID = "GO4LUNCH_ID";
    public static final String PREF_DATA = "GO4LUNCH_DATA";

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

    void setSharedPrefsData(Data data) {
        Log.d(TAG, "setSharedPrefsData: ");
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("rname", data.getString(RNAME));
        editor.putString("raddress", data.getString(RADDRESS));
        editor.putString("rid", data.getString(RID));

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
        return new ArrayList<>(Arrays.asList(sharedPref.getString("rname", null), sharedPref.getString("rname", null),
                sharedPref.getString("rname", null)));
    }
}
