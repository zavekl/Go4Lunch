
package com.brice_corp.go4lunch.model.projo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @NonNull
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @NonNull
    @SerializedName("periods")
    @Expose
    private List<Period> periods = null;
    @NonNull
    @SerializedName("weekday_text")
    @Expose
    private List<String> weekdayText = null;

    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                ", periods=" + periods +
                ", weekdayText=" + weekdayText +
                '}';
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
