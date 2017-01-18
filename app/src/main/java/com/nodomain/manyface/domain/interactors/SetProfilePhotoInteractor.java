package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.ApiConstants;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.FileReadingException;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.FileReader;
import com.nodomain.manyface.utils.NetworkUtil;


public class SetProfilePhotoInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;
    private final FileReader fileReader;

    public SetProfilePhotoInteractor(Handler mainThreadHandler,
                                     ProfilesRepository profilesRepository,
                                     NetworkUtil networkUtil,
                                     FileReader fileReader) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
        this.fileReader = fileReader;
    }

    public void execute(Profile profile, String pictureFilePath) {
        if (pictureFilePath == null) {
            postEvent(new OnSetProfilePictureSuccessEvent());
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSetProfilePictureFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                byte[] pictureByteArray = fileReader.readFileToByteArray(pictureFilePath);
                profilesRepository.setProfilePicture(profile, pictureByteArray);
                profile.setPictureUrl(ApiConstants.BASE_URL + "api/v1/user/" + profile.getId() + "/photo");     //TODO: rewrite this
                postOnMainThread(() -> postEvent(new OnSetProfilePictureSuccessEvent()));
            } catch (FileReadingException e) {
                postOnMainThread(() -> postEvent(new OnSetProfilePictureFailureEvent(Error.FILE_READING_FAILURE)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSetProfilePictureFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSetProfilePictureSuccessEvent {
    }

    public static class OnSetProfilePictureFailureEvent extends BaseFailureEvent {

        public OnSetProfilePictureFailureEvent(Error error) {
            super(error);
        }
    }
}
