
package com.brice_corp.go4lunch.model.projo;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Close {
    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("time")
    @Expose
    private String time;

    public Close(Integer day, String time) {
        this.day = day;
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "Close{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
