package com.brice_corp.go4lunch.model.projo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by <NIATEL Brice> on <29/09/2020>.
 */
public class Duration {
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("value")
    @Expose
    public int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}