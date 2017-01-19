package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Profile;


public interface EditProfileMvpView extends EditableProfileMvpView {

    void showEditableProfile(Profile editableProfile);
}
