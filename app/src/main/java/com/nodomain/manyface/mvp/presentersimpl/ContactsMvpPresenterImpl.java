package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.GetContactsInteractor;
import com.nodomain.manyface.domain.interactors.GetContactsInteractor.*;
import com.nodomain.manyface.domain.interactors.SearchInteractor;
import com.nodomain.manyface.domain.interactors.SearchInteractor.*;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ContactsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactsMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class ContactsMvpPresenterImpl extends BaseMvpPresenterImpl<ContactsMvpView> implements ContactsMvpPresenter {

    private final GetContactsInteractor getContactsInteractor;
    private final SearchInteractor searchInteractor;

    private Profile currentProfile;

    @Inject
    public ContactsMvpPresenterImpl(GetContactsInteractor getContactsInteractor,
                                    SearchInteractor searchInteractor) {
        this.getContactsInteractor = getContactsInteractor;
        this.searchInteractor = searchInteractor;
    }

    @Override
    public void init(Profile currentProfile) {
        this.currentProfile = currentProfile;
        mvpView.showCurrentProfile(currentProfile);
    }

    @Override
    public void getContacts() {
        mvpView.showGetContactsProgress();
        getContactsInteractor.execute(currentProfile);
    }

    @Override
    public void searchForContacts(String contactName) {
        mvpView.showSearchProgress();
        searchInteractor.execute(contactName);
    }

    @Override
    public void navigateToChat(Profile contact) {
        mvpView.showChatView(currentProfile, contact);
    }

    @Override
    public void navigateToContactDetails(Profile contact) {
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
