package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.CreateProfileInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePhotoInteractor;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateProfileMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class CreateProfileMvpPresenterImpl extends BaseMvpPresenterImpl<CreateProfileMvpView>
        implements CreateProfileMvpPresenter {

    private final CreateProfileInteractor createProfileInteractor;
    private final SetProfilePhotoInteractor setProfilePhotoInteractor;

    private String photoFilePath;

    @Inject
    public CreateProfileMvpPresenterImpl(CreateProfileInteractor createProfileInteractor,
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
