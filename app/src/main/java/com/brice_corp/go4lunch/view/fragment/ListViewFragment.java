package com.brice_corp.go4lunch.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.IdPlaceNumber;
import com.brice_corp.go4lunch.model.projo.DistanceMatrix;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.ListViewViewModel;
import com.brice_corp.go4lunch.view.recyclerview.ListViewRestaurantRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by <NIATEL Brice> on <08/04/2020>.
 */
public class ListViewFragment extends Fragment {
    private static final String TAG = "ListViewFragment";

    private ListViewViewModel mListViewViewModel;
    private ListViewRestaurantRecyclerViewAdapter mAdapter;
    private ArrayList<Restaurant> mSavedListRestaurant = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private Button mRatingButton;
    private Button mWorkmatesButton;

    private Boolean mOnClicked1 = false;
    private Boolean mOnClicked2 = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_menu_fragment_list_view, container, false);
        mRecyclerView = view.findViewById(R.id.list_restaurant_recyclerview);
        mRatingButton = view.findViewById(R.id.buttonlistview1);
        mWorkmatesButton = view.findViewById(R.id.buttonlistview2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListViewViewModel = new ViewModelProvider(requireActivity()).get(ListViewViewModel.class);

        mAdapter = new ListViewRestaurantRecyclerViewAdapter(requireContext(), mListViewViewModel);
        mListViewViewModel.setListViewAdapter(mAdapter);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        setItem();

        onClickButtons();
    }

    private void onClickButtons() {
        mSavedListRestaurant = mAdapter.getList();
        mRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClicked1 = !mOnClicked1;
                if (mOnClicked1) {
                    mWorkmatesButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state1,null));
                    mOnClicked2 = false;
                    mRatingButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state2,null));
                    mAdapter.setList(mListViewViewModel.sortByRating(mSavedListRestaurant));

                    Log.d(TAG, "List rating: " + mListViewViewModel.sortByRating(mAdapter.getList()));
                } else {
                    mRatingButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state1,null));
                    ifNeedSetListRestaurant();
                }
                Log.d(TAG, "onClick1: " + mOnClicked1);
                Log.d(TAG, "onClick2: " + mOnClicked2);
            }
        });

        mWorkmatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSavedListRestaurant.isEmpty()) {
                    mOnClicked2 = !mOnClicked2;
                    if (mOnClicked2) {
                        mRatingButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state1,null));
                        mOnClicked1 = false;
                        mWorkmatesButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state2,null));
                        mAdapter.setList(mListViewViewModel.sortByWorkmates(mSavedListRestaurant));
                        Log.d(TAG, "List rating: " + mListViewViewModel.sortByWorkmates(mAdapter.getList()));
                    } else {
                        mWorkmatesButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.round_btn_state1,null));
                        ifNeedSetListRestaurant();
                    }
                    Log.d(TAG, "onClick1: " + mOnClicked1);
                    Log.d(TAG, "onClick2: " + mOnClicked2);
                }
            }
        });
    }


    private void setItem() {
        for (final IdPlaceNumber idRestaurant : mListViewViewModel.getIdPlaceRestaurantList()) {
            mListViewViewModel.getRestaurantDetails(Objects.requireNonNull(idRestaurant.getIdPlace())).observe(getViewLifecycleOwner(),
                    new Observer<Restaurant>() {
                        @Override
                        public void onChanged(Restaurant restaurant) {
                            final Restaurant currentRestaurant = restaurant.getResult();
                            if (currentRestaurant != null) {
                                mListViewViewModel.getDistance(currentRestaurant.getPlaceId()).observe(getViewLifecycleOwner(), new Observer<DistanceMatrix>() {
                                    @Override
                                    public void onChanged(DistanceMatrix distanceMatrix) {
                                        Log.d(TAG, "onChanged: " + distanceMatrix.toString());
                                        mAdapter.addItems(new Restaurant(currentRestaurant.getName(), currentRestaurant.getFormattedAddress(), currentRestaurant.getRating(),
                                                currentRestaurant.getOpeningHours(), currentRestaurant.getPhotos(), currentRestaurant.getPlaceId(),
                                                distanceMatrix.getRows().get(0).getElements().get(0).getDistance().getText(), idRestaurant.isEatPerson()));
                                    }
                                });
                            }
                        }
                    });
        }
    }

    private void ifNeedSetListRestaurant() {
        if (!mOnClicked1 && !mOnClicked2) {
            Log.d(TAG, "ifNeedSetListRestaurant: ");
            mAdapter.setSavedRestaurant();
        }
    }
}