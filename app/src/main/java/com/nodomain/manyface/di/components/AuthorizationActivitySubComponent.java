package com.nodomain.manyface.di.components;


import com.nodomain.manyface.di.modules.AnimatorsModule;
import com.nodomain.manyface.di.modules.AuthorizationActivityModule;
import com.nodomain.manyface.di.modules.NavigationModule;
import com.nodomain.manyface.di.modules.PresentersModule;
import com.nodomain.manyface.di.scopes.PerActivity;
import com.nodomain.manyface.ui.activities.AuthorizationActivity;
import com.nodomain.manyface.ui.fragments.SignInFragment;
import com.nodomain.manyface.ui.fragments.SignUpFragment;

import dagger.Subcomponent;


@PerActivity
@Subcomponent(
        modules = {
                AuthorizationActivityModule.class,
                PresentersModule.class,
                NavigationModule.class,
                AnimatorsModule.class
        }
)
public interface AuthorizationActivitySubComponent {

    void inject(AuthorizationActivity activity);

    void inject(SignInFragment fragment);

    void inject(SignUpFragment fragment);
}
