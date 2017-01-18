package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.ProfileAlreadyExistsException;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.NetworkUtil;


public class EditProfileInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public EditProfileInteractor(Handler mainThreadHandler,
                                 ProfilesRepository profilesRepository,
                                 NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(Profile editableProfile, String newDescription) {
        String currentDescription = editableProfile.getDescription();
        if (currentDescription.equals(newDescription)) {
            postEvent(new OnEditProfileSuccessEvent(editableProfile));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnEditProfileFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                editableProfile.setDescription(newDescription);
                profilesRepository.updateProfile(editableProfile);
                postOnMainThread(() -> postEvent(new OnEditProfileSuccessEvent(editableProfile)));
            } catch (ProfileAlreadyExistsException e) {
                postOnMainThread(() -> postEvent(new OnEditProfileFailureEvent(Error.PROFILE_ALREADY_EXISTS)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnEditProfileFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnEditProfileSuccessEvent {

        private Profile editableProfile;

        public OnEditProfileSuccessEvent(Profile editableProfile) {
            this.editableProfile = editableProfile;
        }

        public Profile getEditableProfile() {
            return editableProfile;
        }
    }

    public static class OnEditProfileFailureEvent extends BaseFailureEvent {

        public OnEditProfileFailureEvent(Error error) {
            super(error);
        }
    }
}
