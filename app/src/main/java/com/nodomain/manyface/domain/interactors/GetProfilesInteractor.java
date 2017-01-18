package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.List;


public class GetProfilesInteractor extends BaseSingleTaskInteractor { //TODO: Event bus remove sticky

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public GetProfilesInteractor(Handler mainThreadHandler,
                                 ProfilesRepository profilesRepository,
                                 NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute() {
        if (profilesRepository.hasCachedProfiles()) {
            List<Profile> profiles = profilesRepository.getCachedProfilesCopy();
            postEvent(new OnGetProfilesSuccessEvent(profiles));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnGetProfilesFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));

            runInBackground(() -> {
                List<Profile> profiles = profilesRepository.getProfilesFromLocalStorage();
                postOnMainThread(() -> postEvent(new OnGetProfilesSuccessEvent(profiles)));
            });

            return;
        }

        runInBackground(() -> {
            try {
                List<Profile> users = profilesRepository.getUsersFromRemoteStorage();
                postOnMainThread(() -> postEvent(new OnGetProfilesSuccessEvent(users)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnGetProfilesFailureEvent(Error.CONNECTION_FAILED)));
                List<Profile> users = profilesRepository.getProfilesFromLocalStorage();
                postOnMainThread(() -> postEvent(new OnGetProfilesSuccessEvent(users)));
            }
        });
    }

    public static class OnGetProfilesSuccessEvent {

        private List<Profile> profiles;

        public OnGetProfilesSuccessEvent(List<Profile> profiles) {
            this.profiles = profiles;
        }

        public List<Profile> getProfiles() {
            return profiles;
        }
    }

    public static class OnGetProfilesFailureEvent extends BaseFailureEvent {

        public OnGetProfilesFailureEvent(Error error) {
            super(error);
        }
    }
}
