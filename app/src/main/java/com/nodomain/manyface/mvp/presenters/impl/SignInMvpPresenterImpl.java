package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.SignInInteractor;
import com.nodomain.manyface.domain.interactors.SignInInteractor.*;
import com.nodomain.manyface.mvp.presenters.SignInMvpPresenter;
import com.nodomain.manyface.mvp.views.SignInMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class SignInMvpPresenterImpl extends BaseMvpPresenterImpl<SignInMvpView> implements SignInMvpPresenter {

    private final SignInInteractor signInInteractor;

    @Inject
    public SignInMvpPresenterImpl(SignInInteractor signInInteractor) {
        this.signInInteractor = signInInteractor;
    }

    @Override
    public void signIn(String login, String password) {
        mvpView.showSignInProgress();
        signInInteractor.execute(login, password);
    }

    @Subscribe
    public void onSignInSuccess(OnSignInSuccessEvent event) {
        mvpView.hideSignInProgress();
        mvpView.showUsersView();
    }

    @Subscribe
    public void onSignInFailure(OnSignInFailureEvent event) {
        mvpView.hideSignInProgress();
        mvpView.showError(event.getError());
    }
}
