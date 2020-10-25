package com.brice_corp.go4lunch.model;

/**
 * Created by <NIATEL Brice> on <01/10/2020>.
 */
public class IdPlaceNumber {
    private final String mIdPlace;
    private final int mEatPerson;

    public IdPlaceNumber(String mIdPlace, int mEatPerson) {
        this.mIdPlace = mIdPlace;
        this.mEatPerson = mEatPerson;
    }

    public String getIdPlace() {
        return mIdPlace;
    }

    public int isEatPerson() {
        return mEatPerson;
    }
}
