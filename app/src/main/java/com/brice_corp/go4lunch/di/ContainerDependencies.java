package com.brice_corp.go4lunch.di;

import android.content.Context;

import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.MapRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;

/**
 * Created by <NIATEL Brice> on <16/04/2020>.
 */
public class ContainerDependencies {
    private Context mContext;
    private MapRepository mMapRepository;
    private FirestoreUserRepository mFirestoreUserRepository;
    private RetrofitRepository mRetrofitRepository;

    ContainerDependencies(Context mContext) {
        this.mContext = mContext;
        mMapRepository = new MapRepository(mContext);
        mFirestoreUserRepository = new FirestoreUserRepository();
        mRetrofitRepository = new RetrofitRepository();
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

    public RetrofitRepository getRestrofitRepository() {
        return mRetrofitRepository;
    }
}
