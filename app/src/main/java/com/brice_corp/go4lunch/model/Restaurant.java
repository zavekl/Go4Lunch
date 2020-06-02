package com.brice_corp.go4lunch.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by <BRICE NIATEL> on <07/04/2020>.
 */
public class Restaurant {
    private String name;
    private String typeFood;
    private String address;
    private String schedule;

    public Restaurant(String name, String typeFood, String address, String schedule) {
        this.name = name;
        this.typeFood = typeFood;
        this.address = address;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public String getTypeFood() {
        return typeFood;
    }

    public String getAddress() {
        return address;
    }

    public String getSchedule() {
        return schedule;
    }

    public static List<Restaurant> listGetFakesRestaurants() {
        return Arrays.asList(
                new Restaurant("Chez Roger", "Français", "4 rue des Baobabs", "Bientôt ouvert"),
                new Restaurant("Chez Hans", "Allemand", "8 rue de Panzer", "Bientôt fermé"));
    }
}
