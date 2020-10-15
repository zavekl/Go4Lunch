package com.brice_corp.go4lunch.view.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.ListViewViewModel;
import com.brice_corp.go4lunch.utils.RatingBarUtils;
import com.brice_corp.go4lunch.view.activity.DescriptionRestaurantActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by <NIATEL Brice> on <11/05/2020>.
 */
public class ListViewRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRestaurantRecyclerViewAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "ListViewRVAdapter";
    private ArrayList<Restaurant> mItemRestaurants;
    private final Context mContext;
    private final ListViewViewModel mListViewViewModel;
    private final ArrayList<Restaurant> mSavedRestaurant = new ArrayList<>();

    public ListViewRestaurantRecyclerViewAdapter(Context context, ListViewViewModel listViewViewModel) {
        mItemRestaurants = new ArrayList<>();
        mContext = context;
        mListViewViewModel = listViewViewModel;
    }

    @NonNull
    @Override
    public ListViewRestaurantRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview_listview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewRestaurantRecyclerViewAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: all : " + mItemRestaurants.get(position).toString());
        //Name
        StringBuilder sb = new StringBuilder(mItemRestaurants.get(position).getName());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        holder.mNameRestaurant.setText(sb.toString());

        //ADDRESS
        holder.mAddressRestaurant.setText(mItemRestaurants.get(position).getAdrAddress());

        //PHOTO
        if (mItemRestaurants.get(position).getPhotos() != null) {
            Glide.with(mContext)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                            + mItemRestaurants.get(position).getPhotos().get(0).getPhotoReference() + "&key="
                            + mContext.getResources().getString(R.string.place_api_key))
                    .centerCrop()
                    .into(holder.mImageRestaurant);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.no_image_restaurant)
                    .centerCrop()
                    .into(holder.mImageRestaurant);
        }

        if (mItemRestaurants.get(position).getOpeningHours() != null) {
            holder.mScheduleRestaurant.setText(mListViewViewModel.getOpeningHoursSorted(mItemRestaurants.get(position).getOpeningHours().getPeriods(), mItemRestaurants.get(position).getOpeningHours().getOpenNow()));
        } else {
            holder.mScheduleRestaurant.setText(mContext.getResources().getString(R.string.no_hour));
            Log.d(TAG, "onBindViewHolder: periods is null, no information with the API");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DescriptionRestaurantActivity.class);
                Log.i(TAG, "onClick: " + mItemRestaurants.get(position).getPlaceId());
                intent.putExtra("id", mItemRestaurants.get(position).getPlaceId());
                ((Activity) mContext).startActivityForResult(intent, 10);
            }
        });

        //RATING BAR
        if (mItemRestaurants.get(position).getRating() != null) {
            holder.mRatingBar.setRating(RatingBarUtils.CalculateRatingBar(mItemRestaurants.get(position).getRating().floatValue()));
        } else {
            Log.e(TAG, "setTheRatingBar: no rate from API Place");
        }

        //DISTANCE
        holder.mDistanceRestaurant.setText(mItemRestaurants.get(position).getDistanceMeter());

        //NUMBER OF WORKMATES WHICH EAT AT THE RESTAURANT
        holder.mNumberWorkmates.setText(String.valueOf(mItemRestaurants.get(position).getNumberWorkamtesEating()));
    }

    public void addItems(Restaurant restaurant) {
        Log.i(TAG, "addItems: " + restaurant);
        mItemRestaurants.add(restaurant);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();

        mSavedRestaurant.add(restaurant);
    }

    public void setList(ArrayList<Restaurant> list) {
        mItemRestaurants = list;
        notifyDataSetChanged();
        Log.d(TAG, "setList: " + mItemRestaurants);
    }

    public ArrayList<Restaurant> getList() {
        return mItemRestaurants;
    }

    public void setSavedRestaurant() {
        mItemRestaurants = mSavedRestaurant;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mItemRestaurants != null) {
            if (mItemRestaurants.size() == 0) {
                return 0;
            } else {
                return mItemRestaurants.size();
            }
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    Log.d(TAG, "performFiltering: Before Prediction");
                    String wordFilter = (String) constraint;
                    if (mItemRestaurants != null && mItemRestaurants.size() != 0) {
                        if (!wordFilter.isEmpty()) {
                            mItemRestaurants = mListViewViewModel.filterRestaurantList(mSavedRestaurant, wordFilter);
                            Log.d(TAG, "performFiltering: " + mItemRestaurants.size());
                        } else {
                            if (mSavedRestaurant.size() != 0) {
                                Log.d(TAG, "performFiltering: if saved restaurants : " + mSavedRestaurant.size());
                                mItemRestaurants = mSavedRestaurant;
                            }
                        }
                        // Results
                        Log.d(TAG, "performFiltering: After Prediction filtered");
                        results.values = mItemRestaurants;
                        results.count = mItemRestaurants.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d(TAG, "Enter in publishResults method");
                if (results != null) {
                    // The API returned at least one result, update the data.
                    Log.d(TAG, "publishResults:  if condition");
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    Log.e(TAG, "publishResults: zero results from the API");
                }
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameRestaurant;
        final TextView mAddressRestaurant;
        final TextView mScheduleRestaurant;
        final CircleImageView mImageRestaurant;
        final RatingBar mRatingBar;
        final TextView mDistanceRestaurant;
        final TextView mNumberWorkmates;

        ViewHolder(View view) {
            super(view);
            mNameRestaurant = view.findViewById(R.id.nameRestaurant);
            mAddressRestaurant = view.findViewById(R.id.addressRestaurant);
            mScheduleRestaurant = view.findViewById(R.id.scheduleRestaurant);
            mImageRestaurant = view.findViewById(R.id.imageRestaurant);
            mRatingBar = view.findViewById(R.id.ratingbar_restaurant_listview);
            mDistanceRestaurant = view.findViewById(R.id.distance);
            mNumberWorkmates = view.findViewById(R.id.number_workmates);
        }
    }
}
