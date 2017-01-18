package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.CreateProfileInteractor.OnCreateUserSuccessEvent;
import com.nodomain.manyface.domain.interactors.DeleteProfileInteractor;
import com.nodomain.manyface.domain.interactors.EditProfileInteractor.OnEditUserSuccessEvent;
import com.nodomain.manyface.domain.interactors.GetProfilesInteractor;
import com.nodomain.manyface.domain.interactors.SignOutInteractor;
import com.nodomain.manyface.domain.interactors.SignOutInteractor.*;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.presenters.ProfilesMvpPresenter;
import com.nodomain.manyface.mvp.views.ProfilesMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class ProfilesMvpPresenterImpl extends BaseMvpPresenterImpl<ProfilesMvpView> implements ProfilesMvpPresenter {

    private final GetProfilesInteractor getProfilesInteractor;
    private final DeleteProfileInteractor deleteProfileInteractor;
    private final SignOutInteractor signOutInteractor;

    @Inject
    public ProfilesMvpPresenterImpl(GetProfilesInteractor getProfilesInteractor,
                                    DeleteProfileInteractor deleteProfileInteractor,
                                    SignOutInteractor signOutInteractor) {
        this.getProfilesInteractor = getProfilesInteractor;
        this.deleteProfileInteractor = deleteProfileInteractor;
        this.signOutInteractor = signOutInteractor;
    }

    @Override
    public void getUsers() {
        mvpView.showGetUsersProgress();
        getProfilesInteractor.execute();
    }

    @Override
    public void deleteUser(ProfileDto user) {
        mvpView.showDeleteUserProgress();
        deleteProfileInteractor.execute(user);
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
