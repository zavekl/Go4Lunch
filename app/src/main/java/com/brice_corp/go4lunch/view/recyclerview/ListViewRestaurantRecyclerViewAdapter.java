package com.brice_corp.go4lunch.view.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.brice_corp.go4lunch.modelview.ListViewViewModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by <NIATEL Brice> on <11/05/2020>.
 */
public class ListViewRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRestaurantRecyclerViewAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "ListViewRVAdapter";
    private ArrayList<Restaurant> mItemRestaurants;
    private Context mContext;
    private ListViewViewModel mListViewViewModel;
    private ArrayList<Restaurant> mSavedRestaurant = new ArrayList<>();

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
    public void onBindViewHolder(@NonNull ListViewRestaurantRecyclerViewAdapter.ViewHolder holder, int position) {
        //Name
        StringBuilder sb = new StringBuilder(mItemRestaurants.get(position).getName());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        holder.nameRestaurant.setText(sb.toString());

        //Address
        holder.addressRestaurant.setText(mItemRestaurants.get(position).getAdrAddress());

        //PHOTO
        if (mItemRestaurants.get(position).getPhotos() != null) {
            Glide.with(mContext)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference="
                            + mItemRestaurants.get(position).getPhotos().get(0).getPhotoReference() + "&key="
                            + "AIzaSyAz_L90GbDp0Hzy_GHjnmxsqPjc1sARRYA")
                    .centerCrop()
                    .into(holder.imageRestaurant);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.no_image_restaurant)
                    .centerCrop()
                    .into(holder.imageRestaurant);
        }

        //TODO Set HOUR
        holder.scheduleRestaurant.setText("A FAIRE HEURE");
    }

    public void addItems(Restaurant restaurant) {
        Log.i(TAG, "addItems: " + restaurant);
        mItemRestaurants.add(restaurant);
        notifyItemInserted(getItemCount() - 1);
        notifyDataSetChanged();

        mSavedRestaurant.add(restaurant);
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
        }

                ;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameRestaurant;
        final TextView addressRestaurant;
        final TextView scheduleRestaurant;
        final CircleImageView imageRestaurant;
        final View itemList;

        ViewHolder(View view) {
            super(view);
            nameRestaurant = view.findViewById(R.id.nameRestaurant);
            addressRestaurant = view.findViewById(R.id.addressRestaurant);
            scheduleRestaurant = view.findViewById(R.id.scheduleRestaurant);
            imageRestaurant = view.findViewById(R.id.imageRestaurant);
            itemList = view.findViewById(R.id.item_list_listview);
        }
    }
}
