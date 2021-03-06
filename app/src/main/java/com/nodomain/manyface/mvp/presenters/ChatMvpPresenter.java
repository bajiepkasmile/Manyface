package com.nodomain.manyface.mvp.presenters;


import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.views.ChatMvpView;


public interface ChatMvpPresenter extends MvpPresenter<ChatMvpView> {

    void init(Profile currentProfile, Profile contact);

    void getMessages();

    void sendMessage(String messageText);

    void resendMessage(Message message);

    void deleteMessage(Message message);

    void navigateToBack();
}
