package com.nodomain.manyface.ui.recyclerviews.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContactViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.iv_contact_picture)
    public ImageView ivContactPicture;
    @BindView(R.id.tv_contact_name)
    public TextView tvContactName;
    @BindView(R.id.tv_messages_count)
    public TextView tvMessagesCount;

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public ContactViewHolder(View itemView,
                             OnItemClickListener clickListener,
                             OnItemLongClickListener longClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.clickListener = clickListener;
        this.longClickListener = longClickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        longClickListener.onItemLongClick(getAdapterPosition());
        return true;
    }
}
