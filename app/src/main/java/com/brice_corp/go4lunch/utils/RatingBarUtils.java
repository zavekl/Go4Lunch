package com.brice_corp.go4lunch.utils;

import android.util.Log;

/**
 * Created by <NIATEL Brice> on <09/10/2020>.
 */
public class RatingBarUtils {
    private static final String TAG = "RatingBarUtils";

    //Convert 5 stars to 3 stars
    public static float CalculateRatingBar(float rate) {
        if (rate != 0.0f) {
            return (rate * 3) / 5;
        } else {
            Log.d(TAG, "setTheRatingBar: no rate from API Place");
            return 0.0f;
        }
    }
}
