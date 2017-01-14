package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.viewholders.UserViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private List<ProfileDto> users;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public UsersAdapter(List<ProfileDto> users) {
        this.users = users;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        onItemLongClickListener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView, onItemClickListener, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        ProfileDto user = users.get(position);

        Picasso.with(holder.ivPhoto.getContext())
                .load(user.getPhotoLink())
                .placeholder(R.drawable.photo_placeholder)
                .into(holder.ivPhoto);
        holder.tvUsername.setText(user.getUsername());
        holder.tvDescription.setText(user.getDescription());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public ProfileDto getItem(int position) {
        return users.get(position);
    }

    public void addItem(int position, ProfileDto user) {
        users.add(position, user);
        notifyItemInserted(position);
    }

    public void updateItem(ProfileDto user) {
        int updatePosition = users.indexOf(user);
        notifyItemChanged(updatePosition);
    }

    public void removeItem(ProfileDto user) {
        int deletingPosition = users.indexOf(user);
        users.remove(deletingPosition);
        notifyItemRemoved(deletingPosition);
    }

    public void startCreatingUser() {

    }

    public void completeCreatingUser(ProfileDto user) {

    }

    public void cancelCreatingUser(ProfileDto user) {

    }

    public void startEditingUser(ProfileDto user) {

    }

    public void completeEditingUser(ProfileDto user) {

    }

    public void cancelEditingUser(ProfileDto user) {

    }
}
