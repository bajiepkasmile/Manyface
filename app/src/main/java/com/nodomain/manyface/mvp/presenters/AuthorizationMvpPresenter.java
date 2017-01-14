package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.AuthorizationMvpView;


public interface AuthorizationMvpPresenter extends MvpPresenter<AuthorizationMvpView> {

    void navigateToUsers();
}
