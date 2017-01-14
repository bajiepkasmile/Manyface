package com.nodomain.manyface.mvp.views;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;

import java.util.List;


public interface ChatMvpView extends MvpView { //TODO: недоделаны сообщения

    void showChatMembers(ProfileDto currentUser, ProfileDto contact);

    void showMessages(List<MessageDto> messages);

    void showMessage(MessageDto message);

    void showGetMessagesProgress();

    void hideGetMessagesProgress();

    void showPreviousView();
}
