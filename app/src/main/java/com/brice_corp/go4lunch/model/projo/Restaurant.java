package com.brice_corp.go4lunch.model.projo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by <BRICE NIATEL> on <07/04/2020>.
 */
public class Restaurant {

    @SerializedName("adr_address")
    @Expose
    private final String adrAddress;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("result")
    @Expose
    private Restaurant result;

    private String mDistanceMeter;

    private Integer mNumberWorkamtesEating;

    public Restaurant(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.adrAddress = address;
    }

    public Restaurant(String name, String address, Double rating, OpeningHours openingHours, List<Photo> photos,String id, String distance, int numberWorkamtesEating) {
        this.name = name;
        this.adrAddress = address;
        this.rating = rating;
        this.openingHours = openingHours;
        this.photos=photos;
        this.placeId=id;
        this.mDistanceMeter = distance;
        this.mNumberWorkamtesEating = numberWorkamtesEating;
    }


    public String getAdrAddress() {
        return adrAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating=rating;
    }

    public String getWebsite() {
        return website;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Restaurant getResult() {
        return result;
    }

    public String getDistanceMeter() {
        return mDistanceMeter;
    }

    public Integer getNumberWorkamtesEating() {
        return mNumberWorkamtesEating;
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return "Restaurant{" +
                ", adrAddress='" + adrAddress + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", formattedPhoneNumber='" + formattedPhoneNumber + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", openingHours=" + openingHours +
                ", photos=" + photos +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", website='" + website + '\'' +
                ", result=" + result +
                '}';
    }
}