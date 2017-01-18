package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Profile;
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

    public void execute(Profile profile) {
        if (contactsRepository.hasCachedProfileContacts(profile)) {
            List<Profile> contacts = contactsRepository.getCachedProfileContactsCopy(profile);
            postEvent(new OnGetContactsSuccessEvent(contacts));
            return;
        }

        boolean networkIsNoAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNoAvailable) {
            postEvent(new OnGetContactsFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));

            runInBackground(() -> {
                List<Profile> contacts = contactsRepository.getProfileContactsFromLocalStorage(profile);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            });

            return;
        }

        runInBackground(() -> {
            try {
                List<Profile> contacts = contactsRepository.getProfileContactsFromRemoteStorage(profile);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnGetContactsFailureEvent(Error.CONNECTION_FAILED)));
                List<Profile> contacts = contactsRepository.getProfileContactsFromLocalStorage(profile);
                postOnMainThread(() -> postEvent(new OnGetContactsSuccessEvent(contacts)));
            }
        });
    }

    public static class OnGetContactsSuccessEvent {

        private List<Profile> contacts;

        public OnGetContactsSuccessEvent(List<Profile> contacts) {
            this.contacts = contacts;
        }

        public List<Profile> getContacts() {
            return contacts;
        }
    }

    public static class OnGetContactsFailureEvent extends BaseFailureEvent {

        public OnGetContactsFailureEvent(Error error) {
            super(error);
        }
    }
}
