package com.nodomain.manyface.utils;


import com.nodomain.manyface.domain.Error;

import javax.inject.Inject;


public class AuthInfoValidator {

    private static final String[] illegalCharacters = new String[]{" ", ";", ".", ",", "?"};

    @Inject
    public AuthInfoValidator() {
    }

    public Error checkLogin(String login) {
        if (login.length() == 0) {
            return Error.EMPTY_LOGIN;
        }

        if (stringContainsIllegalCharacters(login)) {
            return Error.ILLEGAL_CHARACTERS_IN_LOGIN;
        }

        return Error.NONE;
    }

    public Error checkPassword(String password) {
        if (password.length() == 0) {
            return Error.EMPTY_PASSWORD;
        }

        if (stringContainsIllegalCharacters(password)) {
            return Error.ILLEGAL_CHARACTERS_IN_PASSWORD;
        }

        return Error.NONE;
    }

    public Error checkPasswordConfirmation(String passwordConfirmation) {
        if (passwordConfirmation.length() == 0) {
            return Error.EMPTY_PASSWORD_CONFIRMATION;
        }

        if (stringContainsIllegalCharacters(passwordConfirmation)) {
            return Error.ILLEGAL_CHARACTERS_IN_PASSWORD_CONFIRMATION;
        }

        return Error.NONE;
    }

    public Error checkProfileName(String profileName) {
        if (profileName.length() == 0) {
            return Error.EMPTY_PROFILE_NAME;
        }

        if (stringContainsIllegalCharacters(profileName)) {
            return Error.ILLEGAL_CHARACTERS_IN_PROFILE_NAME;
        }

        return Error.NONE;
    }

    private boolean stringContainsIllegalCharacters(String string) {
        for (String illegalCharacter : illegalCharacters) {
            if (string.contains(illegalCharacter)) {
                return true;
            }
        }
        return false;
    }
}
