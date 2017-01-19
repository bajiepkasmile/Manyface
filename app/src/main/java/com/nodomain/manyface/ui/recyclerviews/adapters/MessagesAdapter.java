package com.nodomain.manyface.ui.recyclerviews.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodomain.manyface.R;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.recyclerviews.viewholders.MessageViewHolder;
import com.nodomain.manyface.utils.TimeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_OUT = 0;
    private static final int VIEW_TYPE_MESSAGE_IN = 1;

    private final Profile currentProfile;

    private List<Message> messages;
    private OnItemClickListener listener;

    public MessagesAdapter(Profile currentProfile, List<Message> messages, OnItemClickListener listener) {
        this.currentProfile = currentProfile;
        this.messages = messages;
        this.listener = listener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == VIEW_TYPE_MESSAGE_OUT)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_out, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_in, parent, false);

        return new MessageViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);

        holder.tvText.setText(message.getText());

        switch (message.getStatus()) {
            case SENDING:
                Drawable icClock = getDrawable(holder.tvText.getContext(), R.drawable.ic_clock);
                holder.tvTime.setCompoundDrawables(icClock, null, null, null);
                break;
            case UNSENT:
                Drawable icError = getDrawable(holder.tvText.getContext(), R.drawable.ic_error);
                holder.tvTime.setCompoundDrawables(icError, null, null, null);
                break;
            default:
                String sentTimeStr = TimeConverter.timeToShortDateString(message.getSentTime());
                holder.tvTime.setText(sentTimeStr);
                break;
        }
    }

    private Drawable getDrawable(Context context, @DrawableRes int redId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            return context.getResources().getDrawable(redId, context.getTheme());
        else
            return context.getResources().getDrawable(redId);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if (message.getSenderId() == currentProfile.getId())
            return VIEW_TYPE_MESSAGE_OUT;
        else
            return VIEW_TYPE_MESSAGE_IN;
    }

    public Message getItem(int position) {
        return messages.get(position);
    }

    public void addItem(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size()-1);
    }

    public void removeItem(Message message) {
        int removeIndex = messages.indexOf(message);
        messages.remove(removeIndex);
        notifyItemRemoved(removeIndex);
    }

    public void updateItem(Message message) {
        int updatePosition = messages.indexOf(message);
        messages.set(updatePosition, message);
        notifyItemChanged(updatePosition);
    }
}
