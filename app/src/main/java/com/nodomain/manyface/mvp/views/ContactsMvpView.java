package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Profile;

import java.util.List;


public interface ContactsMvpView extends MvpView {

    void showCurrentProfile(Profile currentProfile);

    void showContacts(List<Profile> contacts);

    void showFoundedContacts(List<Profile> foundedContacts);

    void showGetContactsProgress();

    void hideGetContactsProgress();

    void showSearchProgress();

    void hideSearchProgress();

    void showChatView(Profile currentProfile, Profile contact);

    void showContactDetailsView(Profile contact);

    void showPreviousView();
}
