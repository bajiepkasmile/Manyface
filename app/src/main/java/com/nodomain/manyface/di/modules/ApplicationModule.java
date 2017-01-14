package com.nodomain.manyface.di.modules;


import android.content.Context;
import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.AccountManager;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    Handler provideMainThreadHandler() {
        return new Handler();
    }

    @Singleton
    @Provides
    ExecutorService provideExecutorService() {
        return Executors.newSingleThreadExecutor();
    }
}
