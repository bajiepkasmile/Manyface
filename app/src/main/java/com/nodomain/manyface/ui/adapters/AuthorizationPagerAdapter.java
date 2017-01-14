package com.nodomain.manyface.ui.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.fragments.SignInFragment;
import com.nodomain.manyface.ui.fragments.SignUpFragment;


public class AuthorizationPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;

    private Context context;

    public AuthorizationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return SignInFragment.newInstance();
        } else {
            return SignUpFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.sign_in);
        } else {
            return context.getString(R.string.sign_up);
        }
    }
}
