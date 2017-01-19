package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.AccountManager;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.IncorrectAuthorizationInfoException;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.AuthInfoValidator;
import com.nodomain.manyface.utils.NetworkUtil;


public class SignInInteractor extends BaseSingleTaskInteractor {

    private final AccountManager accountManager;
    private final NetworkUtil networkUtil;
    private final AuthInfoValidator authInfoValidator;

    public SignInInteractor(Handler mainThreadHandler,
                            AccountManager accountManager,
                            NetworkUtil networkUtil,
                            AuthInfoValidator authInfoValidator) {
        super(mainThreadHandler);
        this.accountManager = accountManager;
        this.networkUtil = networkUtil;
        this.authInfoValidator = authInfoValidator;
    }

    public void execute() {
        postEvent(new OnSignInStartEvent());

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSignInFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                accountManager.signIn();
                postOnMainThread(() -> postEvent(new OnSignInSuccessEvent()));
            } catch (IncorrectAuthorizationInfoException e) {
                postOnMainThread(() ->
                        postEvent(new OnSignInFailureEvent(Error.INCORRECT_LOGIN_AND_PASSWORD_COMBINATION)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSignInFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public void execute(String login, String password) {
        postEvent(new OnSignInStartEvent());

        Error loginCheckError = authInfoValidator.checkLogin(login);
        if (loginCheckError != null) {
            postEvent(new OnSignInFailureEvent(loginCheckError));
        }

        Error passwordCheckError = authInfoValidator.checkPassword(password);
        if (passwordCheckError != null) {
            postEvent(new OnSignInFailureEvent(passwordCheckError));
        }

        if (loginCheckError != null || passwordCheckError != null) {
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSignInFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                accountManager.signIn(login, password);
                postOnMainThread(() -> postEvent(new OnSignInSuccessEvent()));
            } catch (IncorrectAuthorizationInfoException e) {
                postOnMainThread(() ->
                        postEvent(new OnSignInFailureEvent(Error.INCORRECT_LOGIN_AND_PASSWORD_COMBINATION)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSignInFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSignInStartEvent {
    }

    public static class OnSignInSuccessEvent {
    }

    public static class OnSignInFailureEvent extends BaseFailureEvent {

        public OnSignInFailureEvent(Error error) {
            super(error);
        }
    }
}
