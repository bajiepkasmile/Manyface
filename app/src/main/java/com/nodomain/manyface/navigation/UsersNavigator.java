package com.nodomain.manyface.navigation;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;


public interface UsersNavigator {

    void navigateToContactsView(ProfileDto selectedUser);

    void navigateToAuthorizationView();
}