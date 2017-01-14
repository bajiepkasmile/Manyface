package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.CreateUserInteractor.OnCreateUserSuccessEvent;
import com.nodomain.manyface.domain.interactors.DeleteUserInteractor;
import com.nodomain.manyface.domain.interactors.DeleteUserInteractor.*;
import com.nodomain.manyface.domain.interactors.EditUserInteractor.OnEditUserSuccessEvent;
import com.nodomain.manyface.domain.interactors.GetUsersInteractor;
import com.nodomain.manyface.domain.interactors.GetUsersInteractor.*;
import com.nodomain.manyface.domain.interactors.SignOutInteractor;
import com.nodomain.manyface.domain.interactors.SignOutInteractor.*;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.presenters.UsersMvpPresenter;
import com.nodomain.manyface.mvp.views.UsersMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class UsersMvpPresenterImpl extends BaseMvpPresenterImpl<UsersMvpView> implements UsersMvpPresenter {

    private final GetUsersInteractor getUsersInteractor;
    private final DeleteUserInteractor deleteUserInteractor;
    private final SignOutInteractor signOutInteractor;

    @Inject
    public UsersMvpPresenterImpl(GetUsersInteractor getUsersInteractor,
                                 DeleteUserInteractor deleteUserInteractor,
                                 SignOutInteractor signOutInteractor) {
        this.getUsersInteractor = getUsersInteractor;
        this.deleteUserInteractor = deleteUserInteractor;
        this.signOutInteractor = signOutInteractor;
    }

    @Override
    public void getUsers() {
        mvpView.showGetUsersProgress();
        getUsersInteractor.execute();
    }

    @Override
    public void deleteUser(ProfileDto user) {
        mvpView.showDeleteUserProgress();
        deleteUserInteractor.execute(user);
    }

    @Override
    public void signOut() {
        mvpView.showSignOutProgress();
        signOutInteractor.execute();
    }

    @Override
    public void navigateToEditUser(ProfileDto editableUser) {
        mvpView.showEditUserView(editableUser);
    }

    @Override
    public void navigateToCreateUser() {
        mvpView.showCreateUserView();
    }

    @Override
    public void navigateToContacts(ProfileDto user) {
        mvpView.showContactsView(user);
    }

    @Subscribe
    public void onGetUsersSuccess(OnGetUsersSuccessEvent event) {
        mvpView.hideGetUsersProgress();
        mvpView.showUsers(event.getUsers());
    }

    @Subscribe
    public void onGetUsersFailure(OnGetUsersFailureEvent event) {
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onEditUserSuccess(OnEditUserSuccessEvent event) {
//        mvpView.updateUser(event.getEditedUSer());

        //TODO: remove created and edited users from events
    }

    @Subscribe
    public void onDeleteUserSuccess(OnDeleteUserSuccessEvent event) {
        mvpView.hideDeleteUserProgress();
        mvpView.hideUser(event.getDeletedUser());
    }

    @Subscribe
    public void onDeleteUserFailure(OnDeleteUserFailureEvent event) {
        mvpView.hideDeleteUserProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onLogoutFinished(OnSignOutFinishEvent event) {
        mvpView.hideSignOutProgress();
        mvpView.showAuthorizationView();
    }

    @Subscribe
    public void onCreateUserSuccess(OnCreateUserSuccessEvent event) {
//        mvpView.showUser(event.getUser());
    }
}
