package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.CreateUserInteractor;
import com.nodomain.manyface.domain.interactors.CreateUserInteractor.*;
import com.nodomain.manyface.domain.interactors.SetUserPhotoInteractor;
import com.nodomain.manyface.domain.interactors.SetUserPhotoInteractor.*;
import com.nodomain.manyface.mvp.presenters.CreateUserMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateUserMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class CreateUserMvpPresenterImpl extends BaseMvpPresenterImpl<CreateUserMvpView>
        implements CreateUserMvpPresenter {

    private final CreateUserInteractor createUserInteractor;
    private final SetUserPhotoInteractor setUserPhotoInteractor;

    private String photoFilePath;

    @Inject
    public CreateUserMvpPresenterImpl(CreateUserInteractor createUserInteractor,
                                      SetUserPhotoInteractor setUserPhotoInteractor) {
        this.createUserInteractor = createUserInteractor;
        this.setUserPhotoInteractor = setUserPhotoInteractor;
    }

    @Override
    public void createUser(String username, String description, String photoFilePath) {
        mvpView.showSaveProgress();
        createUserInteractor.execute(username, description);
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
        setUserPhotoInteractor.execute(event.getUser(), photoFilePath);
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
