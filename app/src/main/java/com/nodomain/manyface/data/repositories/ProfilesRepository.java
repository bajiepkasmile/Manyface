package com.nodomain.manyface.data.repositories;


import com.nodomain.manyface.data.datasources.cache.ProfilesCache;
import com.nodomain.manyface.data.datasources.local.ProfilesLocalStorage;
import com.nodomain.manyface.data.datasources.remote.ProfilesRemoteStorage;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.FileReadingException;
import com.nodomain.manyface.domain.exeptions.TooManyProfilesException;
import com.nodomain.manyface.domain.exeptions.ProfileAlreadyExistsException;
import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.List;


public class ProfilesRepository {

    private final ProfilesRemoteStorage remoteStorage;
    private final ProfilesLocalStorage localStorage;
    private final ProfilesCache cache;

    public ProfilesRepository(ProfilesRemoteStorage remoteStorage,
                              ProfilesLocalStorage localStorage,
                              ProfilesCache cache) {
        this.remoteStorage = remoteStorage;
        this.localStorage = localStorage;
        this.cache = cache;
    }

    public boolean hasCachedProfiles() {
        return cache.hasProfiles();
    }

    public List<Profile> getCachedProfilesCopy() {
        return cache.getProfilesCopy();
    }

    public List<Profile> getProfilesFromLocalStorage() {
        List<Profile> profiles = localStorage.getProfiles();
        cache.rewriteProfiles(profiles);
        return new ArrayList<>(profiles); //return copy to achieve immutability of cache
    }

    public List<Profile> getUsersFromRemoteStorage() throws ConnectionFailedException {
        List<Profile> profiles = remoteStorage.getProfiles();
        localStorage.rewriteProfiles(profiles);
        cache.rewriteProfiles(profiles);
        return new ArrayList<>(profiles); //return copy to achieve immutability of cache
    }

    public Profile createProfile(String name, String description)
            throws ProfileAlreadyExistsException, TooManyProfilesException, ConnectionFailedException {
        Profile createdProfile = remoteStorage.createProfile(name, description);
        localStorage.saveProfile(createdProfile);
        cache.addProfile(createdProfile);
        return createdProfile;
    }

    public void updateProfile(Profile profile) throws ProfileAlreadyExistsException, ConnectionFailedException {
        remoteStorage.updateProfile(profile);
        localStorage.updateProfile(profile);
        cache.updateProfile(profile);
    }

    public void deleteProfile(Profile profile) throws ConnectionFailedException {
        remoteStorage.deleteProfile(profile);
        localStorage.deleteProfile(profile);
        cache.deleteProfile(profile);
    }

    public void clearProfiles() {
        localStorage.clearProfiles();
        cache.clear();
    }

    public void setProfilePicture(Profile profile, byte[] pictureByteArray)
            throws FileReadingException, ConnectionFailedException {
        remoteStorage.setProfilePicture(profile, pictureByteArray);
    }
}
