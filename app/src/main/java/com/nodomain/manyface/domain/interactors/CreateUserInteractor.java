package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.AuthInfoValidator;
import com.nodomain.manyface.utils.NetworkUtil;


public class CreateUserInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;
    private final AuthInfoValidator authInfoValidator;

    public CreateUserInteractor(Handler mainThreadHandler,
                                ProfilesRepository profilesRepository,
                                NetworkUtil networkUtil,
                                AuthInfoValidator authInfoValidator) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
        this.authInfoValidator = authInfoValidator;
    }

    public void execute(String username, String description) {
        try {
            authInfoValidator.checkUsername(username);
            networkUtil.checkNetworkIsAvailable();
        } catch (Exception e) {
            postEvent(new OnCreateUserFailureEvent(e));
            return;
        }

        runInBackground(() -> {
            try {
                ProfileDto createdUser = profilesRepository.createUser(username, description);
                postOnMainThread(() -> postEvent(new OnCreateUserSuccessEvent(createdUser)));
            } catch (Exception e) {
                postOnMainThread(() -> postEvent(new OnCreateUserFailureEvent(e)));
            }
        });
    }

    public static class OnCreateUserSuccessEvent {

        private ProfileDto user;

        public OnCreateUserSuccessEvent(ProfileDto user) {
            this.user = user;
        }

        public ProfileDto getUser() {
            return user;
        }
    }

    public static class OnCreateUserFailureEvent extends BaseFailureEvent {

        public OnCreateUserFailureEvent(Exception exception) {
            super(exception);
        }
    }
}
