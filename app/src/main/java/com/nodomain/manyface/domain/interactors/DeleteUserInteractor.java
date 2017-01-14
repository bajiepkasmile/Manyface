package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.NetworkUtil;


public class DeleteUserInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public DeleteUserInteractor(Handler mainThreadHandler,
                                ProfilesRepository profilesRepository,
                                NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(ProfileDto user) {
        try {
            networkUtil.checkNetworkIsAvailable();
        } catch (Exception e) {
            postEvent(new OnDeleteUserFailureEvent(e));
            return;
        }

        runInBackground(() -> {
            try {
                profilesRepository.deleteUser(user);
                postOnMainThread(() -> postEvent(new OnDeleteUserSuccessEvent(user)));
            } catch (Exception e) {
                postOnMainThread(() -> postEvent(new OnDeleteUserFailureEvent(e)));
            }
        });
    }

    public static class OnDeleteUserSuccessEvent {

        private ProfileDto deletedUser;

        public OnDeleteUserSuccessEvent(ProfileDto deletedUser) {
            this.deletedUser = deletedUser;
        }

        public ProfileDto getDeletedUser() {
            return deletedUser;
        }
    }

    public static class OnDeleteUserFailureEvent extends BaseFailureEvent {

        public OnDeleteUserFailureEvent(Exception exception) {
            super(exception);
        }
    }
}
