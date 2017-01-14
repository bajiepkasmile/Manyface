package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.views.ContactsMvpView;


public interface ContactsMvpPresenter extends MvpPresenter<ContactsMvpView> {

    void init(ProfileDto currentUser);

    void getContacts();

    void searchForContacts(String contactUsername);

    void navigateToChat(ProfileDto contact);

    void navigateToContactDetails(ProfileDto contact);

    void navigateToBack();
}
