package com.nodomain.manyface.ui.recyclerviews.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.iv_contact_photo)
    public ImageView ivContactPhoto;
    @BindView(R.id.tv_contact_username)
    public TextView tvContactUsername;

    private OnItemClickListener listener;

    public ContactViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);
        this.listener = listener;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onItemClick(getAdapterPosition());
    }
}
