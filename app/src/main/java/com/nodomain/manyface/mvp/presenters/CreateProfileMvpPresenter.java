package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.CreateProfileMvpView;


public interface CreateProfileMvpPresenter extends MvpPresenter<CreateProfileMvpView> {

    void createProfile(String name, String description, String pictureFilePath);

    void navigateToBack();
}
