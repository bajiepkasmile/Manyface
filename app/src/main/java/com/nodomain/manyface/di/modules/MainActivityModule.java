package com.nodomain.manyface.di.modules;


import android.support.v7.app.AppCompatActivity;

import com.nodomain.manyface.di.scopes.PerActivity;
import com.nodomain.manyface.navigation.MainNavigator;
import com.nodomain.manyface.ui.activities.MainActivity;

import dagger.Module;
import dagger.Provides;


@Module
public class MainActivityModule {

    private MainActivity mainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @PerActivity
    @Provides
    AppCompatActivity provideAppCompatActivity() {
        return mainActivity;
    }

    @PerActivity
    @Provides
    MainNavigator provideMainNavigator() {
        return new MainNavigator(mainActivity);
    }
}
