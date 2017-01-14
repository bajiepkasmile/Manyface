package com.nodomain.manyface.data.datasources.cache;


import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ProfilesCache {

    private List<Profile> cachedProfiles;

    @Inject
    public ProfilesCache() {
    }

    public boolean hasProfiles() {
        return cachedProfiles != null;
    }

    public List<Profile> getProfilesCopy() {
        if (hasProfiles()) {
            return copyProfiles(cachedProfiles); //return copy to achieve immutability of cache
        } else {
            return Collections.emptyList();
        }
    }

    public void rewriteProfiles(List<Profile> profiles) {
        cachedProfiles = copyProfiles(profiles); //save copy to achieve immutability of cache
    }

    public void addProfile(Profile profile) {
        Profile profileCopy = Profile.copy(profile); //save copy to achieve immutability of cache
        cachedProfiles.add(profileCopy);
    }

    public void updateProfile(Profile profile) {
        int updatedProfileIndex = cachedProfiles.indexOf(profile);
        Profile profileCopy = Profile.copy(profile); //save copy to achieve immutability of cache
        cachedProfiles.set(updatedProfileIndex, profileCopy);
    }

    public void deleteProfile(Profile profile) {
        cachedProfiles.remove(profile);
    }

    public void clear() {
        cachedProfiles = null;
    }

    private List<Profile> copyProfiles(List<Profile> profiles) {
        List<Profile> profilesCopy = new ArrayList<>();
        for (Profile profile : profiles) {
            Profile profileCopy = Profile.copy(profile);
            profilesCopy.add(profileCopy);
        }
        return profilesCopy;
    }
}
