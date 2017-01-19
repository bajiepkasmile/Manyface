package com.nodomain.manyface.navigation;


import com.nodomain.manyface.model.Profile;


public interface ProfilesNavigator {

    void navigateToCreateProfileView();

    void navigateToEditProfileView(Profile editableProfile);

    void navigateToContactsView(Profile profile);

    void navigateToAuthorizationView();
}