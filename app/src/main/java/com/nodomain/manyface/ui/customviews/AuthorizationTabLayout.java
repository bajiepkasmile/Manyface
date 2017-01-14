package com.nodomain.manyface.ui.customviews;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class AuthorizationTabLayout extends TabLayout {

    public AuthorizationTabLayout(Context context) {
        super(context);
    }

    public AuthorizationTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AuthorizationTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        LinearLayout tabStrip = ((LinearLayout) getChildAt(0));
        tabStrip.setEnabled(enabled);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(enabled);
        }
    }
}
