package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;

import java.util.List;


public interface UsersMvpView extends MvpView {

    void showUsers(List<ProfileDto> users);

    void hideUser(ProfileDto user);

    void showGetUsersProgress();

    void hideGetUsersProgress();

    void showDeleteUserProgress();

    void hideDeleteUserProgress();

    void showSignOutProgress();

    void hideSignOutProgress();

    void showCreateUserView();

    void showEditUserView(ProfileDto editableUser);

    void showContactsView(ProfileDto selectedUser);

    void showAuthorizationView();
}
