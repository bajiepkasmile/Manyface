package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;


public interface EditUserMvpView extends MvpView {

    void showEditableUser(ProfileDto editableUser);

    void showSaveProgress();

    void hideSaveProgress();

    void showPreviousView();
}
