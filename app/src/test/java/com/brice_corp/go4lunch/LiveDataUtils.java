package com.brice_corp.go4lunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
class LiveDataUtils {
    static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T t) {
                data[0] = t;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        //Don't await indefinitely if the liveData is not set
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set");
        }
        //noinspection unchecked
        return (T) data[0];

    }
}
