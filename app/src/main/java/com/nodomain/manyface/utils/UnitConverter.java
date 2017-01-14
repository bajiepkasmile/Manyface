package com.nodomain.manyface.utils;


import android.content.Context;
import android.util.TypedValue;

import javax.inject.Inject;


public class UnitConverter {

    private Context context;

    @Inject
    public UnitConverter(Context context) {
        this.context = context;
    }

    public float dpToFloat(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
