package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.DeleteProfileInteractor;
import com.nodomain.manyface.domain.interactors.DeleteProfileInteractor.*;
import com.nodomain.manyface.domain.interactors.GetProfilesInteractor;
import com.nodomain.manyface.domain.interactors.GetProfilesInteractor.*;
import com.nodomain.manyface.domain.interactors.SignOutInteractor;
import com.nodomain.manyface.domain.interactors.SignOutInteractor.*;
import com.nodomain.manyface.model.Profile;
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
    public void getProfiles() {
        mvpView.showGetProfilesProgress();
        getProfilesInteractor.execute();
    }

    @Override
    public void deleteProfile(Profile profile) {
        mvpView.showDeleteProfileProgress();
        deleteProfileInteractor.execute(profile);
    }

    @Override
    public void signOut() {
        mvpView.showSignOutProgress();
        signOutInteractor.execute();
    }

    @Override
    public void navigateToEditProfile(Profile editableProfile) {
        mvpView.showEditProfileView(editableProfile);
    }

    @Override
    public void navigateToCreateProfile() {
        mvpView.showCreateProfileView();
    }

    @Override
    public void navigateToContacts(Profile profile) {
        mvpView.showContactsView(profile);
    }

    @Subscribe
    public void onGetProfilesSuccess(OnGetProfilesSuccessEvent event) {
        mvpView.hideGetProfilesProgress();
        mvpView.showProfiles(event.getProfiles());
    }

    @Subscribe
    public void onGetProfilesFailure(OnGetProfilesFailureEvent event) {
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onDeleteProfileSuccess(OnDeleteProfileSuccessEvent event) {
        mvpView.hideDeleteProfileProgress();
        mvpView.hideProfile(event.getDeletedProfile());
    }

    @Subscribe
    public void onDeleteUserFailure(OnDeleteProfileFailureEvent event) {
        mvpView.hideDeleteProfileProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onLogoutFinished(OnSignOutFinishEvent event) {
        mvpView.hideSignOutProgress();
        mvpView.showAuthorizationView();
    }
}
