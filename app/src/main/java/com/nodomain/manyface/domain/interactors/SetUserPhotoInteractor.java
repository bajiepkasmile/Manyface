package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.FileReadingException;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.utils.NetworkUtil;


public class SetUserPhotoInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public SetUserPhotoInteractor(Handler mainThreadHandler,
                                  ProfilesRepository profilesRepository,
                                  NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(ProfileDto user, String photoFilePath) {
        if (photoFilePath == null) {
            postEvent(new OnSetUserPhotoSuccessEvent());
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSetUserPhotoFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                profilesRepository.setUserPhoto(user.getId(), photoFilePath);
                postOnMainThread(() -> postEvent(new OnSetUserPhotoSuccessEvent()));
            } catch (FileReadingException e) {
                postOnMainThread(() -> postEvent(new OnSetUserPhotoFailureEvent(Error.FILE_READING_FAILURE)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSetUserPhotoFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSetUserPhotoSuccessEvent {

    }

    public static class OnSetUserPhotoFailureEvent extends BaseFailureEvent {

        public OnSetUserPhotoFailureEvent(Error error) {
            super(error);
        }
    }
}
