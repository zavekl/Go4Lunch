package com.brice_corp.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.Workmates;
import com.brice_corp.go4lunch.view.recyclerview.WorkmatesRecyclerViewAdapter;

/**
 * Created by <VOTRE-NOM> on <DATE-DU-JOUR>.
 */
public class WorkmatesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_workmates, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.workmates_recyclerview);

        WorkmatesRecyclerViewAdapter adapter = new WorkmatesRecyclerViewAdapter(Workmates.getListFakesWorkmates());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return view;
    }
}
