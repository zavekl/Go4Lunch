package com.brice_corp.go4lunch.model.projo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by <NIATEL Brice> on <29/09/2020>.
 */
public class DistanceMatrix {
    @SerializedName("destination_addresses")
    @Expose
    private List<String> destinationAddresses;
    @SerializedName("origin_addresses")
    @Expose
    private List<String> originAddresses;
    @SerializedName("rows")
    @Expose
    private List<Row> rows;
    @SerializedName("status")
    @Expose
    private String status;

    public DistanceMatrix(List<String> destinationAddresses, List<String> originAddresses, List<Row> rows, String status) {
        this.destinationAddresses = destinationAddresses;
        this.originAddresses = originAddresses;
        this.rows = rows;
        this.status = status;
    }

    public List<String> getDestinationAddresses() {
        return destinationAddresses;
    }

    public void setDestinationAddresses(List<String> destinationAddresses) {
        this.destinationAddresses = destinationAddresses;
    }

    public List<String> getOriginAddresses() {
        return originAddresses;
    }

    public void setOriginAddresses(List<String> originAddresses) {
        this.originAddresses = originAddresses;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
