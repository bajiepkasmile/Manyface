package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.List;


public class GetContactsInteractor extends BaseSingleTaskInteractor {

    private final ContactsRepository contactsRepository;
    private final NetworkUtil networkUtil;

    public GetContactsInteractor(Handler mainThreadHandler,
                                 ContactsRepository contactsRepository,
                                 NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.contactsRepository = contactsRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(long userId) {
        if (contactsRepository.hasCachedUserContacts(userId)) {
            List<ProfileDto> contacts = contactsRepository.getCachedUserContactsCopy(userId);
            postEvent(new OnGetContactsSuccessEvent(contacts));
            return;
        }

        boolean networkIsNoAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNoAvailable) {
            postEvent(new OnGetContactsFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));

            runInBackground(() -> {
                List<ProfileDto> contacts = contactsRepository.getUserContactsFromLocalStorage(userId);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            });

            return;
        }

        runInBackground(() -> {
            try {
                List<ProfileDto> contacts = contactsRepository.getUserContactsFromRemoteStorage(userId);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() ->
                        postEvent(new OnGetContactsFailureEvent(Error.CONNECTION_FAILED)));
                List<ProfileDto> contacts = contactsRepository.getUserContactsFromLocalStorage(userId);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            }
        });
    }

    public static class OnGetContactsSuccessEvent {

        private List<ProfileDto> contacts;

        public OnGetContactsSuccessEvent(List<ProfileDto> contacts) {
            this.contacts = contacts;
        }

        public List<ProfileDto> getContacts() {
            return contacts;
        }
    }

    public static class OnGetContactsFailureEvent extends BaseFailureEvent {

        public OnGetContactsFailureEvent(Error error) {
            super(error);
        }
    }
}
