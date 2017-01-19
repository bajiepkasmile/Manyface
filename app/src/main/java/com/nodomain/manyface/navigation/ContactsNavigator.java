package com.nodomain.manyface.navigation;


import com.nodomain.manyface.model.Profile;


public interface ContactsNavigator {

    void navigateToChatView(Profile currentProfile, Profile contact);

    void navigateToContactDetailsView(Profile contact);

    void navigateToPreviousView();
}
