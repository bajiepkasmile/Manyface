package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.NetworkUtil;


public class DeleteProfileInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public DeleteProfileInteractor(Handler mainThreadHandler,
                                   ProfilesRepository profilesRepository,
                                   NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(Profile profile) {
        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnDeleteProfileFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                profilesRepository.deleteProfile(profile);
                postOnMainThread(() -> postEvent(new OnDeleteProfileSuccessEvent(profile)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnDeleteProfileFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnDeleteProfileSuccessEvent {

        private Profile deletedProfile;

        public OnDeleteProfileSuccessEvent(Profile deletedProfile) {
            this.deletedProfile = deletedProfile;
        }

        public Profile getDeletedProfile() {
            return deletedProfile;
        }
    }

    public static class OnDeleteProfileFailureEvent extends BaseFailureEvent {

        public OnDeleteProfileFailureEvent(Error error) {
            super(error);
        }
    }
}
