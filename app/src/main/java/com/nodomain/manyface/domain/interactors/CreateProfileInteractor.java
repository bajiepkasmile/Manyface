package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.ProfileAlreadyExistsException;
import com.nodomain.manyface.domain.exeptions.TooManyProfilesException;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.AuthInfoValidator;
import com.nodomain.manyface.utils.NetworkUtil;


public class CreateProfileInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;
    private final AuthInfoValidator authInfoValidator;

    public CreateProfileInteractor(Handler mainThreadHandler,
                                   ProfilesRepository profilesRepository,
                                   NetworkUtil networkUtil,
                                   AuthInfoValidator authInfoValidator) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
        this.authInfoValidator = authInfoValidator;
    }

    public void execute(String name, String description) {
        Error checkProfileNameError = authInfoValidator.checkProfileName(name);
        if (checkProfileNameError != Error.NONE) {
            postEvent(new OnCreateProfileFailureEvent(checkProfileNameError));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnCreateProfileFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                Profile createdProfile = profilesRepository.createProfile(name, description);
                postOnMainThread(() -> postEvent(new OnCreateProfileSuccessEvent(createdProfile)));
            } catch (ProfileAlreadyExistsException e) {
                postOnMainThread(() -> postEvent(new OnCreateProfileFailureEvent(Error.PROFILE_ALREADY_EXISTS)));
            } catch (TooManyProfilesException e) {
                postOnMainThread(() -> postEvent(new OnCreateProfileFailureEvent(Error.TOO_MANY_PROFILES)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnCreateProfileFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnCreateProfileSuccessEvent {

        private Profile profile;

        public OnCreateProfileSuccessEvent(Profile profile) {
            this.profile = profile;
        }

        public Profile getProfile() {
            return profile;
        }
    }

    public static class OnCreateProfileFailureEvent extends BaseFailureEvent {

        public OnCreateProfileFailureEvent(Error error) {
            super(error);
        }
    }
}
