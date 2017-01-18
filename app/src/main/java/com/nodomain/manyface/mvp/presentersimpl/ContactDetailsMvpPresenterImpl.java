package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ContactDetailsMvpPresenter;
import com.nodomain.manyface.mvp.views.ContactDetailsMvpView;


public class ContactDetailsMvpPresenterImpl extends BaseMvpPresenterImpl<ContactDetailsMvpView>
        implements ContactDetailsMvpPresenter {

    @Override
    public void init(Profile contact) {
        mvpView.showContactDetails(contact);
    }
}
