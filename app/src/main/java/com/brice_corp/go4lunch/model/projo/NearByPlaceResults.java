
package com.brice_corp.go4lunch.model.projo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearByPlaceResults {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<Restaurant> results = null;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Restaurant> getResults() {
        return results;
    }

    public void setResults(List<Restaurant> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
