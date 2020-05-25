package com.brice_corp.go4lunch.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <19/05/2020>.
 */
public class Workmates {
    private String mName;

    public Workmates(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public static List<Workmates> getListFakesWorkmates() {
        return Arrays.asList(new Workmates("Javier"),
                new Workmates("Julien"),
                new Workmates("Thomas"));
    }
}
