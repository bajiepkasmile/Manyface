package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.mvp.views.SignInMvpView;


public interface SignInMvpPresenter extends MvpPresenter<SignInMvpView> {

    void signIn(String login, String password);
}
