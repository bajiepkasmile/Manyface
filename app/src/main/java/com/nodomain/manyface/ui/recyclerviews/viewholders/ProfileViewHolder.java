package com.nodomain.manyface.ui.recyclerviews.viewholders;


import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.listeners.OnItemClickListener;
import com.nodomain.manyface.ui.listeners.OnItemLongClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileViewHolder extends ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.iv_picture)
    public ImageView ivPicture;
    @BindView(R.id.tv_name)
    public TextView tvName;
    @BindView(R.id.tv_description)
    public TextView tvDescription;

    private final OnItemClickListener onClickListener;
    private final OnItemLongClickListener onLongClickListener;

    public ProfileViewHolder(View itemView,
                             OnItemClickListener onClickListener,
                             OnItemLongClickListener onLongClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onClickListener.onItemClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        onLongClickListener.onItemLongClick(getAdapterPosition());
        return true;
    }
}
