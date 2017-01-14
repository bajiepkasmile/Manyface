package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.ui.recyclerviews.viewholders.MessageViewHolder;

import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_OUT = 0;
    private static final int VIEW_TYPE_MESSAGE_IN = 1;

    private final long currentUserId;
    private List<MessageDto> messages;

    public MessagesAdapter(long currentUserId, List<MessageDto> messages) {
        this.currentUserId = currentUserId;
        this.messages = messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == VIEW_TYPE_MESSAGE_OUT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_out, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_in, parent, false);
        }

        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageDto message = messages.get(position);

        holder.tvText.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageDto message = messages.get(position);

        if (message.getSenderId() != currentUserId) {
            return VIEW_TYPE_MESSAGE_OUT;
        } else {
            return VIEW_TYPE_MESSAGE_IN;
        }
    }

    public void addItem(MessageDto message) {
        messages.add(message);
        notifyItemInserted(messages.size()-1);
    }
}
