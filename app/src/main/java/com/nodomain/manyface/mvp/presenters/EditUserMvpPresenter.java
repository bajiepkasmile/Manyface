package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.views.EditProfileMvpView;


public interface EditUserMvpPresenter extends MvpPresenter<EditProfileMvpView> {

    void init(Profile editableProfile);

    void editProfile(String newDescription, String newPictureFilePath);

    void navigateToBack();
}
