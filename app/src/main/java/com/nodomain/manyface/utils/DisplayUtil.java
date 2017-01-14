package com.nodomain.manyface.utils;


import android.content.Context;

import javax.inject.Inject;


public class DisplayUtil {

    private Context context;

    @Inject
    public DisplayUtil(Context context) {
        this.context = context;
    }

    public int getDisplayWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getDisplayHeight() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
