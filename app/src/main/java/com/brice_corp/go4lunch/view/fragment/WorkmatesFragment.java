package com.brice_corp.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.User;
import com.brice_corp.go4lunch.modelview.WorkmatesViewModel;
import com.brice_corp.go4lunch.view.recyclerview.WorkmatesRecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class WorkmatesFragment extends Fragment {

    private WorkmatesViewModel mWorkmatesViewModel;
    private RecyclerView mRecyclerView;
    private WorkmatesRecyclerViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_workmates, container, false);
        mRecyclerView = view.findViewById(R.id.workmates_recyclerview);

        mWorkmatesViewModel = new ViewModelProvider(requireActivity()).get(WorkmatesViewModel.class);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {

        Query query = mWorkmatesViewModel.getQuery();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class)
                .build();
        adapter = new WorkmatesRecyclerViewAdapter(options);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}