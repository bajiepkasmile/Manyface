package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.views.ProfilesMvpView;


public interface ProfilesMvpPresenter extends MvpPresenter<ProfilesMvpView> {

    void getProfiles();

    void deleteProfile(Profile profile);

    void signOut();

    void navigateToCreateProfile();

    void navigateToEditProfile(Profile editableProfile);

    void navigateToContacts(Profile selectedProfile);
}
