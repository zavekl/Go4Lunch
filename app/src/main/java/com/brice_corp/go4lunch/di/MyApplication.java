package com.brice_corp.go4lunch.di;

import android.app.Application;

/**
 * Created by <NIATEL BRICE> on <16/04/2020>.
 */
public class MyApplication extends Application {

    private ContainerDependencies containerDependencies;

    public ContainerDependencies getContainerDependencies() {
        if (containerDependencies == null) {
            containerDependencies = new ContainerDependencies(this);
        }
        return containerDependencies;
    }
}
