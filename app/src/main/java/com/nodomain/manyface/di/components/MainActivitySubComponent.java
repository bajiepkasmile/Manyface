package com.nodomain.manyface.di.components;


import com.nodomain.manyface.di.modules.MainActivityModule;
import com.nodomain.manyface.di.modules.NavigationModule;
import com.nodomain.manyface.di.modules.PresentersModule;
import com.nodomain.manyface.di.scopes.PerActivity;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.fragments.ChatFragment;
import com.nodomain.manyface.ui.fragments.ContactsFragment;
import com.nodomain.manyface.ui.fragments.CreateUserFragment;
import com.nodomain.manyface.ui.fragments.EditUserFragment;
import com.nodomain.manyface.ui.fragments.UsersFragment;

import dagger.Subcomponent;


@PerActivity
@Subcomponent(
        modules = {
                MainActivityModule.class,
                PresentersModule.class,
                NavigationModule.class
        }
)
public interface MainActivitySubComponent {

    void inject(MainActivity activity);

    void inject(UsersFragment fragment);

    void inject(CreateUserFragment fragment);

    void inject(EditUserFragment fragment);

    void inject(ContactsFragment fragment);

    void inject(ChatFragment fragment);
}
