package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.util.List;
import java.util.Map;


public interface ContactsMvpView extends MvpView {

    void showCurrentProfile(Profile currentProfile);

    void showContacts(List<Profile> contacts);

    void showUnreadMessagesForContacts(Map<Profile, List<Message>> unreadMessagesForContacts);

    void showFoundedContacts(List<Profile> foundedContacts);

    void showGetContactsProgress();

    void hideGetContactsProgress();

    void showSearchProgress();

    void hideSearchProgress();

    void showChatView(Profile currentProfile, Profile contact);

    void showContactDetailsView(Profile contact);

    void showPreviousView();
}
