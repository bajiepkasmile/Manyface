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
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
public class DataSourcesModule {

    @Singleton
    @Provides
    ManyfaceApi provideManyfaceApi() {
        return new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                //order of factories is important
                //if GCF will be first, it will add quotes to message text
                .addConverterFactory(ScalarsConverterFactory.create())
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
