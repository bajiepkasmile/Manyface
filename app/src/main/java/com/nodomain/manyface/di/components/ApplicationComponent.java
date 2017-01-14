package com.nodomain.manyface.di.components;


import com.nodomain.manyface.di.modules.AuthorizationActivityModule;
import com.nodomain.manyface.di.modules.LocalDataSourcesModule;
import com.nodomain.manyface.di.modules.ApplicationModule;
import com.nodomain.manyface.di.modules.InteractorsModule;
import com.nodomain.manyface.di.modules.MainActivityModule;
import com.nodomain.manyface.di.modules.RemoteDataSourceModule;
import com.nodomain.manyface.di.modules.RepositoriesModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                RemoteDataSourceModule.class,
                LocalDataSourcesModule.class,
                RepositoriesModule.class,
                InteractorsModule.class
        }
)
public interface ApplicationComponent {

    AuthorizationActivitySubComponent plusLoginActivitySubComponent(
            AuthorizationActivityModule authorizationActivityModule);

    MainActivitySubComponent plusMainActivitySubComponent(MainActivityModule mainActivityModule);
}
