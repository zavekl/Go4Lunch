package com.brice_corp.go4lunch.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.Workmates;
import com.brice_corp.go4lunch.modelview.MapViewModel;
import com.brice_corp.go4lunch.modelview.WorkmatesViewModel;
import com.brice_corp.go4lunch.view.recyclerview.WorkmatesRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class WorkmatesFragment extends Fragment {

    private WorkmatesRecyclerViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_workmates, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.workmates_recyclerview);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        WorkmatesViewModel mWorkmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        Log.i("WorkmatesFragment", "onCreateView: " + mWorkmatesViewModel.getUsersName().getValue());
        adapter = new WorkmatesRecyclerViewAdapter(mWorkmatesViewModel.getUsersName().getValue());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mWorkmatesViewModel.getUsersName().observe(requireActivity(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                adapter.addWorkmatesList(strings);
            }
        });

        return view;
    }
}