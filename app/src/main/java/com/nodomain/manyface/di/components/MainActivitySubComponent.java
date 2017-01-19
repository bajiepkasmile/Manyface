package com.nodomain.manyface.di.components;


import com.nodomain.manyface.di.modules.MainActivityModule;
import com.nodomain.manyface.di.modules.NavigationModule;
import com.nodomain.manyface.di.modules.PresentersModule;
import com.nodomain.manyface.di.scopes.PerActivity;
import com.nodomain.manyface.ui.activities.MainActivity;
import com.nodomain.manyface.ui.fragments.ChatFragment;
import com.nodomain.manyface.ui.fragments.ContactDetailsFragment;
import com.nodomain.manyface.ui.fragments.ContactsFragment;
import com.nodomain.manyface.ui.fragments.CreateProfileFragment;
import com.nodomain.manyface.ui.fragments.EditProfileFragment;
import com.nodomain.manyface.ui.fragments.ProfilesFragment;

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

    void inject(ProfilesFragment fragment);

    void inject(CreateProfileFragment fragment);

    void inject(EditProfileFragment fragment);

    void inject(ContactsFragment fragment);

    void inject(ContactDetailsFragment fragment);

    void inject(ChatFragment fragment);
}
