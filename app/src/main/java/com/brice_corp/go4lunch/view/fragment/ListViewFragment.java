package com.brice_corp.go4lunch.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.ListViewViewModel;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import java.util.Objects;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class ListViewFragment extends Fragment {
    private RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_list_view, container, false);
        mRecyclerView = view.findViewById(R.id.list_restaurant_recyclerview);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListViewViewModel mListViewViewModel = new ViewModelProvider(requireActivity()).get(ListViewViewModel.class);

        final ListViewRestaurantRecyclerViewAdapter adapter = new ListViewRestaurantRecyclerViewAdapter(requireContext(), mListViewViewModel);
        mListViewViewModel.setListViewAdapter(adapter);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        for (String idRestaurant : mListViewViewModel.getIdPlaceRestaurantList()) {
            mListViewViewModel.getRestaurantDetails(Objects.requireNonNull(idRestaurant)).observe(getViewLifecycleOwner(),
                    new Observer<Restaurant>() {
                        @Override
                        public void onChanged(Restaurant restaurant) {
                            Restaurant currentRestaurant = restaurant.getResult();
                            if (currentRestaurant != null) {
                                adapter.addItems(new Restaurant(currentRestaurant.getName(), currentRestaurant.getFormattedAddress(), currentRestaurant.getRating(),
                                        currentRestaurant.getOpeningHours(), currentRestaurant.getPhotos(), currentRestaurant.getPlaceId()));
                            }
                        }
                    });
        }
    }
}