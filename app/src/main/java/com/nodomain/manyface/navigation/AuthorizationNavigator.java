package com.nodomain.manyface.navigation;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.nodomain.manyface.R;
import com.nodomain.manyface.ui.activities.MainActivity;

import javax.inject.Inject;


public class AuthorizationNavigator extends BaseNavigator implements SignInNavigator, SignUpNavigator {

    @Inject
    public AuthorizationNavigator(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void navigateToProfilesView() {
        Intent intent = createIntent(MainActivity.class);
        startNewActivityAndFinishCurrent(intent);
    }

}
