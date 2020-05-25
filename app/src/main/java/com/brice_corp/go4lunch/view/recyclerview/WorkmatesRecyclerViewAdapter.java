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

import java.util.List;

/**
 * Created by <NIATEL Brice> on <20/05/2020>.
 */
public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesRecyclerViewAdapter.ViewHolder> {
    private List<Workmates> workmates;

    public WorkmatesRecyclerViewAdapter(List<Workmates> workmates) {
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
        holder.mPersonName.setText(workmates.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return workmates.size();
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
