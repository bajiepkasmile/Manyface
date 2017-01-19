package com.nodomain.manyface.ui.recyclerviews.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_text)
    public TextView tvText;
    @BindView(R.id.tv_time)
    public TextView tvTime;

    private OnItemClickListener listener;

    public MessageViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}
