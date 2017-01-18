package com.nodomain.manyface.di.components;


import com.nodomain.manyface.di.modules.AuthorizationActivityModule;
import com.nodomain.manyface.di.modules.ApplicationModule;
import com.nodomain.manyface.di.modules.DataSourcesModule;
import com.nodomain.manyface.di.modules.InteractorsModule;
import com.nodomain.manyface.di.modules.MainActivityModule;
import com.nodomain.manyface.di.modules.RepositoriesModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                RepositoriesModule.class,
                InteractorsModule.class,
                DataSourcesModule.class,

        }
)
public interface ApplicationComponent {

    AuthorizationActivitySubComponent plusLoginActivitySubComponent(
            AuthorizationActivityModule authorizationActivityModule);

    MainActivitySubComponent plusMainActivitySubComponent(MainActivityModule mainActivityModule);
}
