package com.nodomain.manyface.mvp.views;


public interface AuthorizationMvpView extends MvpView {

    void showUsersView();

    void showAuthorizationProgress();

    void hideAuthorizationProgress();
}
