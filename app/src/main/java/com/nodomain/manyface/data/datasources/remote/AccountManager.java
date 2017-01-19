package com.nodomain.manyface.data.datasources.remote;


import android.content.Context;
import android.content.SharedPreferences;

import com.nodomain.manyface.data.datasources.remote.impl.ApiConstants.HttpCodes;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;
import com.nodomain.manyface.domain.exeptions.AccountAlreadyExistsException;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.IncorrectAuthorizationInfoException;

import java.io.IOException;

import retrofit2.Response;


public class AccountManager {   //TODO: move this class to another folder?

    private static final String HEADER_ACCESS_TOKEN = "AccessToken";
    private static final String SH_PREF_NAME = "Manyface";
    private static final String SH_PREF_ACCESS_TOKEN = "access_token";
    private static final String SH_PREF_LOGIN = "login";
    private static final String SH_PREF_PASSWORD = "password";

    private final ManyfaceApi api;
    private final SharedPreferences sharedPreferences;

    public AccountManager(Context context, ManyfaceApi api) {
        sharedPreferences = context.getSharedPreferences(SH_PREF_NAME, Context.MODE_PRIVATE);
        this.api = api;
    }

    public void signUp(String login, String password)
            throws AccountAlreadyExistsException, ConnectionFailedException {
        try {
            Response<Void> response = api.signUp(login, password).execute();
            if (response.code() == HttpCodes.CONFLICT) {
                throw new AccountAlreadyExistsException();
            } else {
                String accessToken = response.headers().get(HEADER_ACCESS_TOKEN);
                setLogin(login);
                setPassword(password);
                setAccessToken(accessToken);
            }
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    public void signIn() throws IncorrectAuthorizationInfoException, ConnectionFailedException {
        try {
            Response<Void> response = api.signIn(getLogin(), getPassword()).execute();
            if (response.code() == HttpCodes.NOT_ACCEPTABLE) {
                throw new IncorrectAuthorizationInfoException();
            } else {
                String accessToken = response.headers().get(HEADER_ACCESS_TOKEN);
                setAccessToken(accessToken);
            }
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    public void signIn(String login, String password)
            throws IncorrectAuthorizationInfoException, ConnectionFailedException{
        try {
            Response<Void> response = api.signIn(login, password).execute();
            if (response.code() == HttpCodes.NOT_ACCEPTABLE) {
                throw new IncorrectAuthorizationInfoException();
            } else {
                String accessToken = response.headers().get(HEADER_ACCESS_TOKEN);
                setLogin(login);
                setPassword(password);
                setAccessToken(accessToken);
            }
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    public void signOut() {
        setAccessToken(null);
        setLogin(null);
        setPassword(null);
    }

    public boolean hasAccessToken() {
        return getAccessToken() != null;
    }

    public boolean hasAuthInfo() {
        return (getLogin() != null) && (getPassword() != null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString(SH_PREF_ACCESS_TOKEN, null);
    }

    private String getLogin() {
        return sharedPreferences.getString(SH_PREF_LOGIN, null);
    }

    private String getPassword() {
        return sharedPreferences.getString(SH_PREF_PASSWORD, null);
    }

    private void setLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SH_PREF_LOGIN, login);
        editor.apply();
    }

    private void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SH_PREF_PASSWORD, password);
        editor.apply();
    }

    private void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SH_PREF_ACCESS_TOKEN, accessToken);
        editor.apply();
    }
}
