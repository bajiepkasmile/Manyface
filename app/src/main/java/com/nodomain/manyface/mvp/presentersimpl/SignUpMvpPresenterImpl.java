package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.SignUpInteractor;
import com.nodomain.manyface.domain.interactors.SignUpInteractor.*;
import com.nodomain.manyface.mvp.presenters.SignUpMvpPresenter;
import com.nodomain.manyface.mvp.views.SignUpMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class SignUpMvpPresenterImpl extends BaseMvpPresenterImpl<SignUpMvpView> implements SignUpMvpPresenter{

    private final SignUpInteractor signUpInteractor;

    @Inject
    public SignUpMvpPresenterImpl(SignUpInteractor signUpInteractor) {
        this.signUpInteractor = signUpInteractor;
    }

    @Override
    public void signUp(String login, String password, String passwordConfirmation) {
        mvpView.showSignUpProgress();
        signUpInteractor.execute(login, password, passwordConfirmation);
    }

    @Subscribe
    public void onSignUpSuccess(OnSignUpSuccessEvent event) {
        mvpView.hideSignUpProgress();
        mvpView.showProfilesView();
    }

    @Subscribe
    public void onSignUpFailure(OnSignUpFailureEvent event) {
        mvpView.hideSignUpProgress();
        mvpView.showError(event.getError());
    }
}
