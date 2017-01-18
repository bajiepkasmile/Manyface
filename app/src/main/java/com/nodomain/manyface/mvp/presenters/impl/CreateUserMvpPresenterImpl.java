package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.CreateProfileInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePhotoInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePhotoInteractor.*;
import com.nodomain.manyface.mvp.presenters.CreateUserMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateUserMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class CreateUserMvpPresenterImpl extends BaseMvpPresenterImpl<CreateUserMvpView>
        implements CreateUserMvpPresenter {

    private final CreateProfileInteractor createProfileInteractor;
    private final SetProfilePhotoInteractor setProfilePhotoInteractor;

    private String photoFilePath;

    @Inject
    public CreateUserMvpPresenterImpl(CreateProfileInteractor createProfileInteractor,
                                      SetProfilePhotoInteractor setProfilePhotoInteractor) {
        this.createProfileInteractor = createProfileInteractor;
        this.setProfilePhotoInteractor = setProfilePhotoInteractor;
    }

    @Override
    public void createUser(String username, String description, String photoFilePath) {
        mvpView.showSaveProgress();
        createProfileInteractor.execute(username, description);
        this.photoFilePath = photoFilePath;
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onCreateUserSuccess(OnCreateUserSuccessEvent event) {
//        mvpView.hideSavingProgress();
//        mvpView.navigateToBack();
        setProfilePhotoInteractor.execute(event.getUser(), photoFilePath);
    }

    @Subscribe
    public void onCreateUserFailure(OnCreateUserFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onSetUserPhotoSuccess(OnSetUserPhotoSuccessEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onSetUserPhotoFailure(OnSetUserPhotoFailureEvent event) {
        mvpView.hideSaveProgress();
        mvpView.showError(event.getError());
    }
}
