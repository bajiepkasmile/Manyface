package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.domain.interactors.GetContactsInteractor;
import com.nodomain.manyface.domain.interactors.GetContactsInteractor.*;
import com.nodomain.manyface.domain.interactors.SearchInteractor;
import com.nodomain.manyface.domain.interactors.SearchInteractor.*;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactsMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class ContactsMvpPresenterImpl extends BaseMvpPresenterImpl<ContactsMvpView> implements ContactsMvpPresenter {

    private final GetContactsInteractor getContactsInteractor;
    private final SearchInteractor searchInteractor;

    private ProfileDto currentUser;

    @Inject
    public ContactsMvpPresenterImpl(GetContactsInteractor getContactsInteractor,
                                    SearchInteractor searchInteractor) {
        this.getContactsInteractor = getContactsInteractor;
        this.searchInteractor = searchInteractor;
    }

    @Override
    public void init(ProfileDto currentUser) {
        this.currentUser = currentUser;
        mvpView.showCurrentUser(currentUser);
    }

    @Override
    public void getContacts() {
        mvpView.showGetContactsProgress();
        getContactsInteractor.execute(currentUser.getId());
    }

    @Override
    public void searchForContacts(String contactUsername) {
        mvpView.showSearchProgress();
        searchInteractor.execute(contactUsername);
    }

    @Override
    public void navigateToChat(ProfileDto contact) {
        mvpView.showChatView(currentUser, contact);
    }

    @Override
    public void navigateToContactDetails(ProfileDto contact) {
        mvpView.showContactDetailsView(contact);
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
    }

    @Subscribe
    public void onGetContactsSuccess(OnGetContactsSuccessEvent event) {
        mvpView.hideGetContactsProgress();
        mvpView.showContacts(event.getContacts());
    }

    @Subscribe
    public void onGetContactsFailure(OnGetContactsFailureEvent event) {
        mvpView.hideGetContactsProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onSearchSuccess(OnSearchSuccessEvent event) {
        mvpView.hideSearchProgress();
        mvpView.showFoundedContacts(event.getFoundedContacts());
    }

    @Subscribe
    public void onSearchFailure(OnSearchFailureEvent event) {
        mvpView.hideSearchProgress();
        mvpView.showError(event.getError());
    }
}
