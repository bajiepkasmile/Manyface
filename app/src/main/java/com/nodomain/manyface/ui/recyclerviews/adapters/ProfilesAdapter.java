package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.viewholders.ProfileViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProfilesAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    private List<Profile> profiles;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public ProfilesAdapter(List<Profile> profiles,
                           OnItemClickListener clickListener,
                           OnItemLongClickListener longClickListener) {
        this.profiles = profiles;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ProfileViewHolder(itemView, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);

        Picasso.with(holder.ivPicture.getContext())
                .load(profile.getPictureUrl())
                .placeholder(R.drawable.picture_placeholder)
                .into(holder.ivPicture);
        holder.tvName.setText(profile.getName());
        holder.tvDescription.setText(profile.getDescription());
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public Profile getItem(int position) {
        return profiles.get(position);
    }

    public void removeItem(Profile profile) {
        int deletingPosition = profiles.indexOf(profile);
        profiles.remove(deletingPosition);
        notifyItemRemoved(deletingPosition);
    }
}
