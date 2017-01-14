package com.nodomain.manyface.mvp.views;


public interface SignInMvpView extends MvpView {

    void showSignInProgress();

    void hideSignInProgress();

    void showUsersView();
}
