package com.nodomain.manyface.ui.customviews;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class AuthorizationViewPager extends ViewPager {

    private boolean enabled = true;

    public AuthorizationViewPager(Context context) {
        super(context);
    }

    public AuthorizationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return enabled && super.onTouchEvent(ev);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.enabled = enabled;
    }
}
