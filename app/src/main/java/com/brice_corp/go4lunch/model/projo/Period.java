
package com.brice_corp.go4lunch.model.projo;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Period {

    @SerializedName("close")
    @Expose
    private Close close;
    @SerializedName("open")
    @Expose
    private Open open;

    public Period(Close close, Open open) {
        this.close = close;
        this.open = open;
    }

    @NonNull
    @Override
    public String toString() {
        return "Period{" +
                "close=" + close +
                ", open=" + open +
                '}';
    }

    public Close getClose() {
        return close;
    }

    public void setClose(Close close) {
        this.close = close;
    }

    public Open getOpen() {
        return open;
    }

    public void setOpen(Open open) {
        this.open = open;
    }

}
