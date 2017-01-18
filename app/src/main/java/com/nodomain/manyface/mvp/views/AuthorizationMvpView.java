package com.nodomain.manyface.mvp.views;


public interface AuthorizationMvpView extends MvpView {

    void showAuthorizationProgress();

    void hideAuthorizationProgress();

    void showProfilesView();
}
