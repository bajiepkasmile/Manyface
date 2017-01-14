package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.SignUpMvpView;


public interface SignUpMvpPresenter extends MvpPresenter<SignUpMvpView> {

    void signUp(String login, String password, String passwordConfirmation);
}
