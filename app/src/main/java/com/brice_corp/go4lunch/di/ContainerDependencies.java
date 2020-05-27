package com.brice_corp.go4lunch.di;

import android.content.Context;

import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.MapRepository;

/**
 * Created by <NIATEL Brice> on <16/04/2020>.
 */
public class ContainerDependencies {
    private Context mContext;
    private MapRepository mMapRepository;
    private FirestoreUserRepository mFirestoreUserRepository;

    ContainerDependencies(Context mContext) {
        this.mContext = mContext;
        mMapRepository = new MapRepository(mContext);
        mFirestoreUserRepository = new FirestoreUserRepository();
    }

    public Context getContext() {
        return mContext;
    }

    public MapRepository getMapRepository() {
        return mMapRepository;
    }

    public FirestoreUserRepository getFirestoreUserRepository() {
        return mFirestoreUserRepository;
    }
}
