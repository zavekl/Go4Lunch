package com.brice_corp.go4lunch.di;

import android.content.Context;

import com.brice_corp.go4lunch.repository.FirestoreUserRepository;
import com.brice_corp.go4lunch.repository.ListViewRepository;
import com.brice_corp.go4lunch.repository.MapRepository;
import com.brice_corp.go4lunch.repository.RetrofitRepository;

/**
 * Created by <NIATEL Brice> on <16/04/2020>.
 */
public class ContainerDependencies {
    private final MapRepository mMapRepository;
    private final FirestoreUserRepository mFirestoreUserRepository;
    private final RetrofitRepository mRetrofitRepository;
    private final ListViewRepository mListViewRepository;

    ContainerDependencies(Context mContext) {
        mMapRepository = new MapRepository(mContext);
        mFirestoreUserRepository = new FirestoreUserRepository();
        mRetrofitRepository = new RetrofitRepository(mContext);
        mListViewRepository= new ListViewRepository();
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

    public ListViewRepository getListViewRepository() {
        return mListViewRepository;
    }
}
