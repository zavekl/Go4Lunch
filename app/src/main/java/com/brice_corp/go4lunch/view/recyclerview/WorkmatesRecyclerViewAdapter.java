package com.brice_corp.go4lunch.view.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brice_corp.go4lunch.R;
import com.brice_corp.go4lunch.model.User;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by <NIATEL Brice> on <20/05/2020>.
 */
public class WorkmatesRecyclerViewAdapter extends FirestoreRecyclerAdapter<User, WorkmatesRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private static final String TAG = "WorkmatesRVAdapter";

    public WorkmatesRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        String personName;
        Log.i(TAG, "onBindViewHolder:'" + model.getEatTodayName() + "'");

        if (model.getEatTodayName() != null && !model.getEatTodayName().equals("")) {
            personName = mContext.getString(R.string.tv_person_name_workmate_adp, model.getName(), model.getEatTodayName());
        } else {
            personName = mContext.getString(R.string.tv_person_name_workmate_dont_know_adp, model.getName());
        }


        holder.mPersonName.setText(personName);

        Glide.with(mContext)
                .load(model.getImage())
                .centerCrop()
                .into(holder.mImageView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_workmates, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView mImageView;
        final TextView mPersonName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imagePerson);
            mPersonName = itemView.findViewById(R.id.personName);
        }
    }
}

