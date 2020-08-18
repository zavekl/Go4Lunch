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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.view.activity.DescriptionRestaurantActivity;
import com.brice_corp.go4lunch.model.projo.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by <NIATEL Brice> on <06/06/2020>.
 */
public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "AutoCompleteAdapter";
    private final PlacesClient placesClient;
    private RectangularBounds mBounds;
    private ArrayList<Restaurant> mResultList = new ArrayList<>();
    private LatLng mLatLng;
    private Context mContext;

    /**
     * Constructor
     *
     * @param context Context
     * @param bounds  Used to specify the search bounds
     */
    public AutoCompleteAdapter(Context context, RectangularBounds bounds, LatLng latLng) {
        Log.i(TAG, "ListviewAdapter: ");
        this.mBounds = bounds;
        this.mLatLng = latLng;
        this.mContext = context;
        //TODO
        Places.initialize(context, "AIzaSyC_1zEF7WGkg-FRO7ATSoX1Y32VY3wzvqM");
        placesClient = com.google.android.libraries.places.api.Places.createClient(context);
    }

    //AutoCompletePrediction with findAutoCompletePrediction method
    private ArrayList<Restaurant> getPredictions(CharSequence constraint) {
        Log.i(TAG, "Enter in getPredictions method ");
        final ArrayList<Restaurant> resultList = new ArrayList<>();

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(mBounds)
                .setOrigin(mLatLng)
                .setCountry("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

        //No problem with the main thread because of the Filterable class uses
        try {
            Tasks.await(autocompletePredictions, 20, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        if (autocompletePredictions.isSuccessful()) {
            FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
            if (findAutocompletePredictionsResponse != null)
                for (com.google.android.libraries.places.api.model.AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                    for (Place.Type type : prediction.getPlaceTypes()) {
                        if (type.toString().equals("RESTAURANT") || type.toString().equals("MEAL_DELIVERY") || type.toString().equals("MEAL_TAKEAWAY")) {
                            resultList.add(new Restaurant(prediction.getPlaceId(), prediction.getPrimaryText(null).toString(),
                                    prediction.getSecondaryText(null).toString()));
                            Log.i(TAG, "getPredictions: " + prediction.getPlaceTypes().toString());
                            break;
                        }
                    }
                }
            return resultList;
        } else {
            return resultList;
        }
    }

    //Filter
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    // Query the autocomplete API for the entered constraint
                    Log.d(TAG, "Before Prediction");
                    mResultList = getPredictions(constraint);
                    Log.d(TAG, "After Prediction");
                    if (mResultList != null) {
                        // Results
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d(TAG, "Enter in publishResults method");
//                if (results != null && results.count > 0) {
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

    public void cleanAdapter() {
        mResultList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "Enter in onBindViewHolder method");
        final Restaurant restaurant = mResultList.get(position);

        holder.mName.setText(restaurant.getName());
        Log.i(TAG, "onBindViewHolder: name " + restaurant.getName());

        holder.mAddress.setText(restaurant.getAdrAddress());
        Log.i(TAG, "onBindViewHolder: address " + restaurant.getAdrAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DescriptionRestaurantActivity.class);
                Log.i(TAG, "onClick: " + restaurant.getId());
                intent.putExtra("id", restaurant.getId());
                ((Activity) mContext).startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mResultList == null) {
            Log.d(TAG, "getItemCount: null");
            return 0;
        } else {
            int size = mResultList.size();
            Log.d(TAG, "getItemCount: " + size);
            return size;
        }
    }

    //View holder of adapter
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mAddress;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.nameListView);
            mAddress = itemView.findViewById(R.id.addressListView);
        }
    }
}