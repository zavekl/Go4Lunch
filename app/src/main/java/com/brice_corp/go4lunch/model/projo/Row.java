package com.brice_corp.go4lunch.model.projo;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by <NIATEL Brice> on <29/09/2020>.
 */
public class Row {
    public Row(List<Element> elements) {
        this.elements = elements;
    }

    @SerializedName("elements")
    @Expose
    private List<Element> elements;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @NonNull
    @Override
    public String toString() {
        return "Row{" +
                "elements=" + elements +
                '}';
    }
}
