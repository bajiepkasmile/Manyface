package com.nodomain.manyface.ui.recyclerviews.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nodomain.manyface.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_text)
    public TextView tvText;

    public MessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
