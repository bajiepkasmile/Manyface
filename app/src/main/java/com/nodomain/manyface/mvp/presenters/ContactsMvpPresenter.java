package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.views.ContactsMvpView;


public interface ContactsMvpPresenter extends MvpPresenter<ContactsMvpView> {

    void init(Profile currentProfile);

    void getContacts();

    void searchForContacts(String contactName);

    void navigateToChat(Profile contact);

    void navigateToContactDetails(Profile contact);

    void navigateToBack();
}
