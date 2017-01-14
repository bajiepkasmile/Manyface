package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.CreateUserMvpView;


public interface CreateUserMvpPresenter extends MvpPresenter<CreateUserMvpView> {

    void createUser(String username, String description, String photoFilePath);

    void navigateToBack();
}
