package com.brice_corp.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.brice_corp.go4lunch.R;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
public class WorkmatesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_menu_fragment_workmates, container, false);
    }
}