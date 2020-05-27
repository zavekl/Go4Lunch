package com.brice_corp.go4lunch.view.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.Workmates;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <NIATEL Brice> on <20/05/2020>.
 */
public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> workmates;

    public WorkmatesRecyclerViewAdapter(ArrayList<String> workmates) {
        this.workmates = workmates;
    }

    @NonNull
    @Override
    public WorkmatesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recyclerview_workmates, parent, false);
        return new WorkmatesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mPersonName.setText(workmates.get(position));
    }

    @Override
    public int getItemCount() {
        if (workmates == null) {
            return 0;
        } else {
            return workmates.size();
        }

    }

    public void addWorkmatesList(ArrayList<String> workmates) {
        this.workmates = workmates;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mPersonName;

        ViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.imagePerson);
            mPersonName = view.findViewById(R.id.personName);
        }
    }
}
