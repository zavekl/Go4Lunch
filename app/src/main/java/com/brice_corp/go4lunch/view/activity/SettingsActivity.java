package com.brice_corp.go4lunch.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.modelview.SettingsViewModel;

import java.util.concurrent.TimeUnit;

import static com.brice_corp.go4lunch.utils.WorkerManager.stopWorkRequest;

/**
 * Created by <NIATEL Brice> on <02/09/2020>.
 */
//TODO Changer l'heure de rappel pourquoi pas ou chercher d'autres paramètres
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private ImageView mImageView;
    private TimePicker mTimePicker;

    private long timeNotification;

    private SettingsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mImageView = findViewById(R.id.back_imageView);
        mTimePicker = findViewById(R.id.timepicker);
        mTimePicker.setIs24HourView(true);

        mViewModel = new ViewModelProvider(SettingsActivity.this).get(SettingsViewModel.class);

        backClick();

        getHourModification();
    }

    private void backClick() {
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getHourModification() {
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeNotification = TimeUnit.HOURS.toMillis(hourOfDay) + TimeUnit.MINUTES.toMillis(minute);
                Log.d(TAG, "onTimeChanged: " + timeNotification);
                stopWorkRequest(getApplicationContext());
                mViewModel.setWorker(timeNotification);
            }
        });
    }
}