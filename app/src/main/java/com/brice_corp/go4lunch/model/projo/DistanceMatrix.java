package com.brice_corp.go4lunch.model.projo;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by <NIATEL Brice> on <29/09/2020>.
 */
public class DistanceMatrix {

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    @SerializedName("rows")
    @Expose
    private List<Row> rows;

    public List<Row> getRows() {
        return rows;
    }

    @NonNull
    @Override
    public String toString() {
        return "DistanceMatrix{" +
                ", rows=" + rows +
                '}';
    }
}
