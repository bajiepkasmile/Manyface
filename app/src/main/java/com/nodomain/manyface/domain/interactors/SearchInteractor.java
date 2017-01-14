package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.List;


public class SearchInteractor extends BaseSingleTaskInteractor {

    private final ContactsRepository contactsRepository;
    private final NetworkUtil networkUtil;

    public SearchInteractor(Handler mainThreadHandler,
                            ContactsRepository contactsRepository,
                            NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.contactsRepository = contactsRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(String contactUsername) {
        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSearchFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                List<ProfileDto> foundedContacts = contactsRepository.searchForContacts(contactUsername);
                postOnMainThread(() -> postEvent(new OnSearchSuccessEvent(foundedContacts)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSearchFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSearchSuccessEvent {

        private List<ProfileDto> foundedContacts;

        public OnSearchSuccessEvent(List<ProfileDto> foundedContacts) {
            this.foundedContacts = foundedContacts;
        }

        public List<ProfileDto> getFoundedContacts() {
            return foundedContacts;
        }
    }

    public static class OnSearchFailureEvent extends BaseFailureEvent{

        public OnSearchFailureEvent(Error error) {
            super(error);
        }
    }
}
