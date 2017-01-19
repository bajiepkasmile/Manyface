package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.data.datasources.remote.AccountManager;
import com.nodomain.manyface.domain.interactors.SignInInteractor.*;
import com.nodomain.manyface.domain.interactors.SignUpInteractor.*;
import com.nodomain.manyface.mvp.presenters.AuthorizationMvpPresenter;
import com.nodomain.manyface.mvp.views.AuthorizationMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class AuthorizationMvpPresenterImpl extends BaseMvpPresenterImpl<AuthorizationMvpView>
        implements AuthorizationMvpPresenter {

    private AccountManager accountManager;

    @Inject
    public AuthorizationMvpPresenterImpl(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public void navigateToProfiles() {
        if (accountManager.hasAuthInfo())
            mvpView.showProfilesView();
    }

    @Subscribe
    public void onSignInStart(OnSignInStartEvent event) {
        mvpView.showAuthorizationProgress();
    }

    @Subscribe
    public void onSignInFailure(OnSignInFailureEvent event) {
        mvpView.hideAuthorizationProgress();
    }

    @Subscribe
    public void onSignUpStart(OnSignUpStartEvent event) {
        mvpView.showAuthorizationProgress();
    }

    @Subscribe
    public void onSignUpFailure(OnSignUpFailureEvent event) {
        mvpView.hideAuthorizationProgress();
    }
}
