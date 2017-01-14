package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.mvp.views.ChatMvpView;

public interface ChatMvpPresenter extends MvpPresenter<ChatMvpView> {

    void init(ProfileDto currentUser, ProfileDto contact);

    void getMessages();

    void sendMessage(String text);

    void navigateToBack();
}
