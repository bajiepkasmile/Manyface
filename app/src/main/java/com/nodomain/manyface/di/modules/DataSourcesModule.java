package com.nodomain.manyface.di.modules;


import android.content.Context;

import com.nodomain.manyface.data.datasources.local.impl.DbHelper;
import com.nodomain.manyface.data.datasources.remote.impl.AccountManager;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;

import javax.inject.Singleton;

import dagger.Provides;


public class DataSourcesModule {

    @Singleton
    @Provides
    AccountManager provideAccountManager(Context context, ManyfaceApi api) {
        return new AccountManager(context, api);
    }

    @Singleton
    @Provides
    DbHelper provideDbHelper(Context context) {
        return new DbHelper(context);
    }
}
