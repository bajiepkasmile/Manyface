package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.data.datasources.remote.AccountManager;
import com.nodomain.manyface.data.repositories.ContactsRepository;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.data.repositories.ProfilesRepository;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;


public class SignOutInteractor extends BaseSingleTaskInteractor {

    private final ProfilesRepository profilesRepository;
    private final ContactsRepository contactsRepository;
    private final MessagesRepository messagesRepository;
    private final AccountManager accountManager;

    public SignOutInteractor(Handler mainThreadHandler,
                             ProfilesRepository profilesRepository,
                             ContactsRepository contactsRepository,
                             MessagesRepository messagesRepository,
                             AccountManager accountManager) {
        super(mainThreadHandler);
        this.profilesRepository = profilesRepository;
        this.contactsRepository = contactsRepository;
        this.messagesRepository = messagesRepository;
        this.accountManager = accountManager;
    }

    public void execute() {
        runInBackground(() -> {
            profilesRepository.clearProfiles();
            contactsRepository.clearAllContacts();
            messagesRepository.clearMessages();
            accountManager.signOut();

            postOnMainThread(() -> postEvent(new OnSignOutFinishEvent()));
        });
    }

    public static class OnSignOutFinishEvent {

    }
}
