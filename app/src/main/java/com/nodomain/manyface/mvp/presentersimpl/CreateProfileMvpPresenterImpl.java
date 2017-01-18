package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.CreateProfileInteractor;
import com.nodomain.manyface.domain.interactors.CreateProfileInteractor.*;
import com.nodomain.manyface.domain.interactors.SetProfilePictureInteractor;
import com.nodomain.manyface.domain.interactors.SetProfilePictureInteractor.*;
import com.nodomain.manyface.mvp.presenters.CreateProfileMvpPresenter;
import com.nodomain.manyface.mvp.views.CreateProfileMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class CreateProfileMvpPresenterImpl extends BaseMvpPresenterImpl<CreateProfileMvpView>
        implements CreateProfileMvpPresenter {

    private final CreateProfileInteractor createProfileInteractor;
    private final SetProfilePictureInteractor setProfilePictureInteractor;

    private String pictureFilePath;

    @Inject
    public CreateProfileMvpPresenterImpl(CreateProfileInteractor createProfileInteractor,
                                         SetProfilePictureInteractor setProfilePictureInteractor) {
        this.createProfileInteractor = createProfileInteractor;
        this.setProfilePictureInteractor = setProfilePictureInteractor;
    }

    @Override
    public void createProfile(String name, String description, String pictureFilePath) {
        mvpView.showSaveProgress();
        createProfileInteractor.execute(name, description);
        this.pictureFilePath = pictureFilePath;
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onCreateProfileSuccess(OnCreateProfileSuccessEvent event) {
        setProfilePictureInteractor.execute(event.getProfile(), pictureFilePath);
    }

    @Subscribe
    public void onCreateProfileFailure(OnCreateProfileFailureEvent event) {
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
