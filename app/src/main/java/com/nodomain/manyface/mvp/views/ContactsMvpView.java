package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;

import java.util.List;


public interface ContactsMvpView extends MvpView {

    void showCurrentUser(ProfileDto currentUser);

    void showContacts(List<ProfileDto> contacts);

    void showFoundedContacts(List<ProfileDto> foundedContacts);

    void showGetContactsProgress();

    void hideGetContactsProgress();

    void showSearchProgress();

    void hideSearchProgress();

    void showChatView(ProfileDto currentUser, ProfileDto contact);

    void showContactDetailsView(ProfileDto contact);

    void showPreviousView();
}
