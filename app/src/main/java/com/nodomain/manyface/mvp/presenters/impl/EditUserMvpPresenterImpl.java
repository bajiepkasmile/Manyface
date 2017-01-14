package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.interactors.EditUserInteractor;
import com.nodomain.manyface.domain.interactors.EditUserInteractor.*;
import com.nodomain.manyface.domain.interactors.SetUserPhotoInteractor;
import com.nodomain.manyface.domain.interactors.SetUserPhotoInteractor.*;
import com.nodomain.manyface.mvp.presenters.EditUserMvpPresenter;
import com.nodomain.manyface.mvp.views.EditUserMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class EditUserMvpPresenterImpl extends BaseMvpPresenterImpl<EditUserMvpView> implements EditUserMvpPresenter {

    private final EditUserInteractor editUserInteractor;
    private final SetUserPhotoInteractor setUserPhotoInteractor;

    private ProfileDto editableUser;
    private String newDescription;

    @Inject
    public EditUserMvpPresenterImpl(EditUserInteractor editUserInteractor,
                                    SetUserPhotoInteractor setUserPhotoInteractor) {
        this.editUserInteractor = editUserInteractor;
        this.setUserPhotoInteractor = setUserPhotoInteractor;
    }

    @Override
    public void init(ProfileDto editableUser) {
        this.editableUser = editableUser;
        mvpView.showEditableUser(editableUser);
    }

    @Override
    public void editUser(String newDescription, String newPhotoFilePath) { //TODO: start both interactors together ?
        mvpView.showSaveProgress();
        this.newDescription = newDescription;
        setUserPhotoInteractor.execute(editableUser, newPhotoFilePath);
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onSetUserPhotoSuccess(OnSetUserPhotoSuccessEvent event) {
        editUserInteractor.execute(editableUser, newDescription);
    }

    @Subscribe
    public void onSetUserPhotoFailure(OnSetUserPhotoFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onEditUserSuccess(OnEditUserSuccessEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onEditUserFailure(OnEditUserFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }
}
