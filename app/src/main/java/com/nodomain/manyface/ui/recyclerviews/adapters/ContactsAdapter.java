package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.recyclerviews.viewholders.ContactViewHolder;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<ProfileDto> contacts;
    private OnItemClickListener listener;

    public ContactsAdapter(List<ProfileDto> contacts) {
        this.contacts = contacts;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        ProfileDto contact = contacts.get(position);

//        holder.ivContactPhoto
        holder.tvContactUsername.setText(contact.getUsername());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setData(List<ProfileDto> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public ProfileDto getItem(int position) {
        return contacts.get(position);
    }
}
