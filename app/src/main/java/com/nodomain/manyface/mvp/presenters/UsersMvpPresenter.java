package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.views.UsersMvpView;


public interface UsersMvpPresenter extends MvpPresenter<UsersMvpView> {

    void getUsers();

    void deleteUser(ProfileDto user);

    void signOut();

    void navigateToEditUser(ProfileDto editableUser); //editUser(user)

    void navigateToCreateUser();//createUser()

    void navigateToContacts(ProfileDto selectedUser);//getContacts
}
