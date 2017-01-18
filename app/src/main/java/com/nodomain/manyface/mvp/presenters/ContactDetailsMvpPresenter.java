package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.views.ContactDetailsMvpView;


public interface ContactDetailsMvpPresenter extends MvpPresenter<ContactDetailsMvpView> {

    void init(Profile contact);
}
