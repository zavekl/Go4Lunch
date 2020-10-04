
package com.brice_corp.go4lunch.model.projo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Close {

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("time")
    @Expose
    private String time;

    @Override
    public String toString() {
        return "Close{" +
                "day=" + day +
                ", time='" + time + '\'' +
                '}';
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
