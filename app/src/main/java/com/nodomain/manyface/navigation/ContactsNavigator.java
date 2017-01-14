package com.nodomain.manyface.navigation;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;


public interface ContactsNavigator {

    void navigateToChatView(ProfileDto currentUser, ProfileDto contact);

    void navigateToContactDetailsView(ProfileDto contact);

    void navigateToPreviousView();
}
