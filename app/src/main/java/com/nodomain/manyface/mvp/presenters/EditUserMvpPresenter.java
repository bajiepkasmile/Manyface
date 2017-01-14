package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.views.EditUserMvpView;


public interface EditUserMvpPresenter extends MvpPresenter<EditUserMvpView> {

    void init(ProfileDto editableUser);

    void editUser(String newDescription, String newPhotoFilePath);

    void navigateToBack();
}
