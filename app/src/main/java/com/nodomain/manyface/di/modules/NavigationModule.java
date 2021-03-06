package com.nodomain.manyface.di.modules;


import com.nodomain.manyface.navigation.AuthorizationNavigator;
import com.nodomain.manyface.navigation.ChatNavigator;
import com.nodomain.manyface.navigation.ContactsNavigator;
import com.nodomain.manyface.navigation.EditableProfileNavigator;
import com.nodomain.manyface.navigation.MainNavigator;
import com.nodomain.manyface.navigation.ProfilesNavigator;
import com.nodomain.manyface.navigation.SignInNavigator;
import com.nodomain.manyface.navigation.SignUpNavigator;

import dagger.Module;
import dagger.Provides;


@Module
public class NavigationModule {

    @Provides
    SignInNavigator provideSignInNavigator(AuthorizationNavigator authorizationNavigator) {
        return authorizationNavigator;
    }

    @Provides
    SignUpNavigator provideSignUpNavigator(AuthorizationNavigator authorizationNavigator) {
        return authorizationNavigator;
    }

    @Provides
    ProfilesNavigator provideProfilesNavigator(MainNavigator mainNavigator) {
        return mainNavigator;
    }

    @Provides
    ContactsNavigator provideContactsNavigator(MainNavigator mainNavigator) {
        return mainNavigator;
    }

    @Provides
    ChatNavigator provideChatNavigator(MainNavigator mainNavigator) {
        return mainNavigator;
    }

    @Provides
    EditableProfileNavigator provideEditableProfileNavigator(MainNavigator mainNavigator) {
        return mainNavigator;
    }
}
