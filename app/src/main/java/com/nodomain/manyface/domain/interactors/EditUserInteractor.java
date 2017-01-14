package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.NetworkUtil;


public class EditUserInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final NetworkUtil networkUtil;

    public EditUserInteractor(Handler mainThreadHandler,
                              ProfilesRepository profilesRepository,
                              NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(ProfileDto editableUser, String newDescription) {
        String currentDescription = editableUser.getDescription();
        if (currentDescription.equals(newDescription)) {
            postEvent(new OnEditUserSuccessEvent(editableUser));
            return;
        }

        try {
            networkUtil.checkNetworkIsAvailable();
        } catch (Exception e) {
            postEvent(new OnEditUserFailureEvent(e));
            return;
        }

        runInBackground(() -> {
            try {
                editableUser.setDescription(newDescription);
                profilesRepository.updateUser(editableUser);
                postOnMainThread(() -> postEvent(new OnEditUserSuccessEvent(editableUser)));
            } catch (Exception e) {
                postOnMainThread(() -> postEvent(new OnEditUserFailureEvent(e)));
            }
        });
    }

    public static class OnEditUserSuccessEvent {

        private ProfileDto editedUSer;

        public OnEditUserSuccessEvent(ProfileDto editedUSer) {
            this.editedUSer = editedUSer;
        }

        public ProfileDto getEditedUSer() {
            return editedUSer;
        }
    }

    public static class OnEditUserFailureEvent extends BaseFailureEvent {

        public OnEditUserFailureEvent(Exception exception) {
            super(exception);
        }
    }
}
