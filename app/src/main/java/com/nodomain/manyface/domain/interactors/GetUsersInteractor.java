package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.List;


public class GetUsersInteractor extends BaseSingleTaskInteractor { //TODO: Event bus remove sticky

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public GetUsersInteractor(Handler mainThreadHandler,
                              ProfilesRepository profilesRepository,
                              NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute() {
        if (profilesRepository.hasCachedUsers()) {
            List<ProfileDto> users = profilesRepository.getCachedUsersCopy();
            postEvent(new OnGetUsersSuccessEvent(users));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnGetUsersFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));

            runInBackground(() -> {
                List<ProfileDto> users = profilesRepository.getUsersFromLocalStorage();
                postOnMainThread(() -> postEvent(new OnGetUsersSuccessEvent(users)));
            });

            return;
        }

        runInBackground(() -> {
            try {
                List<ProfileDto> users = profilesRepository.getUsersFromRemoteStorage();
                postOnMainThread(() -> postEvent(new OnGetUsersSuccessEvent(users)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() ->
                        postEvent(new OnGetUsersFailureEvent(Error.CONNECTION_FAILED)));

                List<ProfileDto> users = profilesRepository.getUsersFromLocalStorage();
                postOnMainThread(() -> postEvent(new OnGetUsersSuccessEvent(users)));
            }
        });
    }

    public static class OnGetUsersSuccessEvent {

        private List<ProfileDto> users;

        public OnGetUsersSuccessEvent(List<ProfileDto> users) {
            this.users = users;
        }

        public List<ProfileDto> getUsers() {
            return users;
        }
    }

    public static class OnGetUsersFailureEvent extends BaseFailureEvent {

        public OnGetUsersFailureEvent(Error error) {
            super(error);
        }
    }
}
