package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;
import com.nodomain.manyface.ui.recyclerviews.viewholders.ContactViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;


public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<Profile> contacts;
    private Map<Profile, List<Message>> unreadMessagesForContacts;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public ContactsAdapter(List<Profile> contacts,
                           OnItemClickListener clickListener,
                           OnItemLongClickListener longClickListener) {
        this.contacts = contacts;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Profile contact = contacts.get(position);
        int messagesCount = getUnreadMessagesCountForContact(contact);

        Picasso.with(holder.ivContactPicture.getContext())
                .load(contact.getPictureUrl())
                .placeholder(R.drawable.picture_placeholder)
                .into(holder.ivContactPicture);
        holder.tvContactName.setText(contact.getName());
        holder.tvMessagesCount.setText(messagesCount);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Profile> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public void setUnreadMessagesForContacts(Map<Profile, List<Message>> unreadMessagesForContacts) {
        this.unreadMessagesForContacts = unreadMessagesForContacts;
        notifyDataSetChanged();
    }

    public Profile getContact(int position) {
        return contacts.get(position);
    }

    private int getUnreadMessagesCountForContact(Profile contact) {
        int messagesCount = 0;
        List<Message> unreadMessagesForContact = unreadMessagesForContacts.get(contact);

        if (unreadMessagesForContact != null)
            messagesCount = unreadMessagesForContact.size();

        return messagesCount;
    }
}
