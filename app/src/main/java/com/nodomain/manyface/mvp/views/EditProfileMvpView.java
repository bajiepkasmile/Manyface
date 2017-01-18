package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Profile;


public interface EditProfileMvpView extends MvpView {

    void showEditableUser(Profile editableProfile);

    void showSaveProgress();

    void hideSaveProgress();

    void showPreviousView();
}