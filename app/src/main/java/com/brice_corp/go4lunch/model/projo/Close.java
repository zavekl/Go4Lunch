
package com.brice_corp.go4lunch.model.projo;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Close {
    @SerializedName("time")
    @Expose
    private String time;

    @NonNull
    @Override
    public String toString() {
        return "Close{" +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }
}
