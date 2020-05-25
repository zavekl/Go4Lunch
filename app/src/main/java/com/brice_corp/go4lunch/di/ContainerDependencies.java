package com.brice_corp.go4lunch.di;

import android.content.Context;

import com.brice_corp.go4lunch.repository.MapRepository;

/**
 * Created by <NIATEL Brice> on <16/04/2020>.
 */
public class ContainerDependencies {
    private Context context;
    private MapRepository repositoryMap;

    ContainerDependencies(Context context) {
        this.context = context;
        repositoryMap = new MapRepository(context);
    }

    public Context getContext() {
        return context;
    }

    public MapRepository getRepositoryMap() {
        return repositoryMap;
    }
}
