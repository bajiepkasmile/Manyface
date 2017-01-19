package com.nodomain.manyface.di.modules;


import android.content.Context;

import com.nodomain.manyface.data.datasources.local.impl.DbHelper;
import com.nodomain.manyface.data.datasources.remote.AccountManager;
import com.nodomain.manyface.data.datasources.remote.impl.ApiConstants;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class DataSourcesModule {

    @Singleton
    @Provides
    ManyfaceApi provideManyfaceApi() {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ManyfaceApi.class);
    }

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
