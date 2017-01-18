package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.EditProfileInteractor;
import com.nodomain.manyface.domain.interactors.EditProfileInteractor.*;
import com.nodomain.manyface.domain.interactors.SetProfilePictureInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePictureInteractor.*;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.views.EditProfileMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class EditUserMvpPresenterImpl extends BaseMvpPresenterImpl<EditProfileMvpView> implements EditUserMvpPresenter {

    private final EditProfileInteractor editProfileInteractor;
    private final SetProfilePictureInteractor setProfilePictureInteractor;

    private Profile editableProfile;
    private String newPictureFilePath;

    @Inject
    public EditUserMvpPresenterImpl(EditProfileInteractor editProfileInteractor,
                                    SetProfilePictureInteractor setProfilePictureInteractor) {
        this.editProfileInteractor = editProfileInteractor;
        this.setProfilePictureInteractor = setProfilePictureInteractor;
    }

    @Override
    public void init(Profile editableProfile) {
        this.editableProfile = editableProfile;
        mvpView.showEditableProfile(editableProfile);
    }

    @Override
    public void editProfile(String newDescription, String newPictureFilePath) {
        mvpView.showSaveProgress();
        this.newPictureFilePath = newPictureFilePath;
        editProfileInteractor.execute(editableProfile, newDescription);
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onEditProfileSuccess(OnEditProfileSuccessEvent event) {
        setProfilePictureInteractor.execute(editableProfile, newPictureFilePath);
    }

    @Subscribe
    public void onEditProfileFailure(OnEditProfileFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onSetProfilePictureSuccess(OnSetProfilePictureSuccessEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onSetProfilePictureFailure(OnSetProfilePictureFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }
}
