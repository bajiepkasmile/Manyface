package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.util.List;


public interface ChatMvpView extends MvpView {

    void showChatMembers(Profile currentProfile, Profile contact);

    void showMessages(List<Message> messages);

    void showMessage(Message message);

    void showSendMessageSuccess(Message sentMessage);

    void showSendMessageError(Message unsentMessage);

    void showGetMessagesProgress();

    void hideGetMessagesProgress();

    void showPreviousView();
}
