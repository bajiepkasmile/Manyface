package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Profile;

import java.util.List;


public interface ProfilesMvpView extends MvpView {

    void showProfiles(List<Profile> profiles);

    void hideProfile(Profile profile);

    void showGetProfilesProgress();

    void hideGetProfilesProgress();

    void showDeleteProfileProgress();

    void hideDeleteProfileProgress();

    void showSignOutProgress();

    void hideSignOutProgress();

    void showCreateProfileView();

    void showEditProfileView(Profile editableProfile);

    void showContactsView(Profile selectedProfile);

    void showAuthorizationView();
}
