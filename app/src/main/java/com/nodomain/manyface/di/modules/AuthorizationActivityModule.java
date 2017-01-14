package com.nodomain.manyface.di.modules;


import android.support.v7.app.AppCompatActivity;

import com.nodomain.manyface.di.scopes.PerActivity;
import com.nodomain.manyface.navigation.AuthorizationNavigator;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.animators.AuthorizationAnimator;
import com.nodomain.manyface.ui.animators.SignInAnimator;
import com.nodomain.manyface.ui.animators.SignUpAnimator;

import dagger.Module;
import dagger.Provides;


@Module
public class AuthorizationActivityModule {

    private AuthorizationActivity authorizationActivity;

    public AuthorizationActivityModule(AuthorizationActivity authorizationActivity) {
        this.authorizationActivity = authorizationActivity;
    }

    @PerActivity
    @Provides
    AppCompatActivity provideAppCompatActivity() {
        return authorizationActivity;
    }

    @PerActivity
    @Provides
    AuthorizationNavigator provideAuthorizationNavigator() {
        return new AuthorizationNavigator(authorizationActivity);
    }
}
