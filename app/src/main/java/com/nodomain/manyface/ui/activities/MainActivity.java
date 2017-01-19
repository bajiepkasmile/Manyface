package com.nodomain.manyface.ui.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.nodomain.manyface.App;
import com.nodomain.manyface.di.components.MainActivitySubComponent;
import com.nodomain.manyface.di.modules.MainActivityModule;
import com.nodomain.manyface.navigation.MainNavigator;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    private MainActivitySubComponent mainActivitySubComponent;

    public static MainActivitySubComponent getActivitySubComponent(Activity activity) {
        return ((MainActivity) activity).mainActivitySubComponent;
    }

    @Inject
    MainNavigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivitySubComponent();
        mainActivitySubComponent.inject(this);

        if (savedInstanceState == null) {
            navigator.navigateToProfilesView();
        }
    }

    private void initActivitySubComponent() {
        mainActivitySubComponent =
                App.getApplicationComponent(this).plusMainActivitySubComponent(new MainActivityModule(this));
    }
}
