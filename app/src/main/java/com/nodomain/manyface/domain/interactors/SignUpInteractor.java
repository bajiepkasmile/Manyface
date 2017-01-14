package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.AccountManager;
import com.nodomain.manyface.domain.exeptions.AccountAlreadyExistsException;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.AuthInfoValidator;
import com.nodomain.manyface.utils.NetworkUtil;


public class SignUpInteractor extends BaseSingleTaskInteractor {

    private final AccountManager accountManager;
    private final NetworkUtil networkUtil;
    private final AuthInfoValidator authInfoValidator;

    public SignUpInteractor(Handler mainThreadHandler,
                            AccountManager accountManager,
                            NetworkUtil networkUtil,
                            AuthInfoValidator authInfoValidator) {
        super(mainThreadHandler);
        this.accountManager = accountManager;
        this.networkUtil = networkUtil;
        this.authInfoValidator = authInfoValidator;
    }

    public void execute(String login, String password, String passwordConfirmation) {
        postEvent(new OnSignUpStartEvent());

        Error loginCheckError = authInfoValidator.checkLogin(login);
        if (loginCheckError != null) {
            postEvent(new OnSignUpFailureEvent(loginCheckError));
        }

        Error passwordCheckError = authInfoValidator.checkPassword(password);
        if (passwordCheckError != null) {
            postEvent(new OnSignUpFailureEvent(passwordCheckError));
        }

        Error passwordConfirmationCheckError =
                authInfoValidator.checkPasswordConfirmation(password, passwordConfirmation);
        if (passwordConfirmationCheckError != null) {
            postEvent(new OnSignUpFailureEvent(passwordConfirmationCheckError));
        }

        if (loginCheckError != null || passwordCheckError != null || passwordConfirmationCheckError != null) {
            return;
        }

        if (!password.equals(passwordConfirmation)) {
            postEvent(new OnSignUpFailureEvent(Error.INCORRECT_PASSWORD_CONFIRMATION));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSignUpFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
        }

        runInBackground(() -> {
            try {
                accountManager.register(login, password);
                postOnMainThread(() -> postEvent(new OnSignUpSuccessEvent()));
            } catch (AccountAlreadyExistsException e) {
                postOnMainThread(() -> postEvent(new OnSignUpFailureEvent(Error.ACCOUNT_ALREADY_EXISTS)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSignUpFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSignUpStartEvent {

    }

    public static class OnSignUpSuccessEvent {

    }

    public static class OnSignUpFailureEvent extends BaseFailureEvent {

        public OnSignUpFailureEvent(Error error) {
            super(error);
        }
    }
}
