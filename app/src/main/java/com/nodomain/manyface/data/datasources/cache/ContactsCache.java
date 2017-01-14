package com.nodomain.manyface.data.datasources.cache;


import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


public class ContactsCache {

    private long profileIdForWhichContactsWereCached; //TODO: find better name
    private List<Profile> cachedProfileContacts;

    @Inject
    public ContactsCache() {
    }

    public boolean hasProfileContacts(Profile profile) {
        return profileIdForWhichContactsWereCached == profile.getId() && cachedProfileContacts != null;
    }

    public List<Profile> getProfileContactsCopy(Profile profile) {
        if (hasProfileContacts(profile)) {
            return copyContacts(cachedProfileContacts); //return copy to achieve immutability of cache
        } else {
            return Collections.emptyList();
        }
    }

    public void rewriteProfileContacts(Profile profile, List<Profile> contacts) {
        profileIdForWhichContactsWereCached = profile.getId();
        cachedProfileContacts = copyContacts(contacts); //save copy to achieve immutability of cache
    }

    public void addProfileContact(Profile profile, Profile contact) {
        if (!hasProfileContacts(profile)) {
            cachedProfileContacts = new ArrayList<>();
        }
        Profile contactCopy = Profile.copy(contact);
        cachedProfileContacts.add(contactCopy); //save copy to achieve immutability of cache
    }

    public void clear() {
        cachedProfileContacts = null;
    }

    private List<Profile> copyContacts(List<Profile> contacts) {
        List<Profile> profilesCopy = new ArrayList<>();
        for (Profile profile : contacts) {
            Profile profileCopy = Profile.copy(profile);
            profilesCopy.add(profileCopy);
        }
        return profilesCopy;
    }
}
