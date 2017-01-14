package com.nodomain.manyface.data.repositories;


import com.nodomain.manyface.data.datasources.cache.ContactsCache;
import com.nodomain.manyface.data.datasources.local.ContactsLocalStorage;
import com.nodomain.manyface.data.datasources.remote.ContactsRemoteStorage;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.model.Profile;

import java.util.List;


public class ContactsRepository {

    private final ContactsRemoteStorage remoteStorage;
    private final ContactsLocalStorage localStorage;
    private final ContactsCache cache;

    public ContactsRepository(ContactsRemoteStorage remoteStorage,
                              ContactsLocalStorage localStorage,
                              ContactsCache cache) {
        this.remoteStorage = remoteStorage;
        this.localStorage = localStorage;
        this.cache = cache;
    }

    public boolean hasCachedProfileContacts(Profile profile) {
        return cache.hasProfileContacts(profile);
    }

    public List<Profile> getCachedProfileContactsCopy(Profile profile) {
        return cache.getProfileContactsCopy(profile);
    }

    public List<Profile> getProfileContactsFromLocalStorage(Profile profile) {
        List<Profile> contacts = localStorage.getProfileContacts(profile);
        cache.rewriteProfileContacts(profile, contacts);
        return contacts;
    }

    public List<Profile> getProfileContactsFromRemoteStorage(Profile profile) throws ConnectionFailedException {
        List<Profile> contacts = remoteStorage.getProfileContacts(profile);
        localStorage.rewriteProfileContacts(profile, contacts);
        cache.rewriteProfileContacts(profile, contacts);
        return contacts;
    }

    public void saveProfileContact(Profile profile, Profile contact) {
        localStorage.saveProfileContact(profile, contact);
        cache.addProfileContact(profile, contact);
    }

    public void clearAllContacts() {
        localStorage.clearAllContacts();
        cache.clear();
    }

    public List<Profile> searchForContacts(String contactUsername) throws ConnectionFailedException {
        return remoteStorage.searchForContacts(contactUsername);
    }
}
