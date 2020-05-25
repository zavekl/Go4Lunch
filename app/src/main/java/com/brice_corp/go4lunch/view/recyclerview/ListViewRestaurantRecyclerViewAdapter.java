package com.brice_corp.go4lunch.view.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.Restaurant;

import java.util.List;

/**
 * Created by <NIATEL Brice> on <11/05/2020>.
 */
public class ListViewRestaurantRecyclerViewAdapter extends RecyclerView.Adapter<ListViewRestaurantRecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> restaurants;


    public ListViewRestaurantRecyclerViewAdapter(List<Restaurant> items) {
        restaurants = items;
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
        holder.nameRestaurant.setText(restaurants.get(position).getName());
        holder.typeRestaurant.setText(restaurants.get(position).getTypeFood() + " - " + restaurants.get(position).getAddress());
        holder.scheduleRestaurant.setText(restaurants.get(position).getSchedule());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameRestaurant;
        TextView typeRestaurant;
        TextView scheduleRestaurant;
        ImageView imageRestaurant;
        View itemList;

        ViewHolder(View view) {
            super(view);
            nameRestaurant = view.findViewById(R.id.nameRestaurant);
            typeRestaurant = view.findViewById(R.id.typeRestaurant);
            scheduleRestaurant = view.findViewById(R.id.scheduleRestaurant);
            imageRestaurant = view.findViewById(R.id.imageRestaurant);
            itemList = view.findViewById(R.id.item_list_listview);
        }
    }
}
