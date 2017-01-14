package com.nodomain.manyface.mvp.presenters.impl;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.CheckMessages;
import com.nodomain.manyface.domain.interactors.GetDialogMessagesInteractor;
import com.nodomain.manyface.domain.interactors.GetDialogMessagesInteractor.*;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.domain.interactors.SendMessageInteractor;
import com.nodomain.manyface.domain.interactors.SendMessageInteractor.*;
import com.nodomain.manyface.mvp.views.ChatMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class ChatMvpPresenterImpl extends BaseMvpPresenterImpl<ChatMvpView> implements ChatMvpPresenter {

    private final GetDialogMessagesInteractor getDialogMessagesInteractor;
    private final SendMessageInteractor sendMessageInteractor;

    private ProfileDto currentUser;
    private ProfileDto contact;

    @Inject
    public ChatMvpPresenterImpl(GetDialogMessagesInteractor getDialogMessagesInteractor,
                                SendMessageInteractor sendMessageInteractor) {
        this.getDialogMessagesInteractor = getDialogMessagesInteractor;
        this.sendMessageInteractor = sendMessageInteractor;
    }

    @Override
    public void init(ProfileDto currentUser, ProfileDto contact) {
        this.currentUser = currentUser;
        this.contact = contact;
        mvpView.showChatMembers(currentUser, contact);
        CheckMessages.getInstance().start();
    }

    @Override
    public void getMessages() {
        mvpView.showGetMessagesProgress();
        getDialogMessagesInteractor.execute(currentUser.getId(), contact.getId());
    }

    @Override
    public void sendMessage(String text) {
        sendMessageInteractor.execute(currentUser.getId(), contact.getId(), text);
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
        CheckMessages.getInstance().stop();
    }

    @Subscribe
    public void onGetDialogMessagesSuccess(OnGetDialogMessagesSuccessEvent event) {
        mvpView.hideGetMessagesProgress();
        mvpView.showMessages(event.getDialogMessages());
    }

    @Subscribe
    public void onGetDialogMessagesFailure(OnGetDialogMessagesFailureEvent event) {
        mvpView.hideGetMessagesProgress();
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onSendMessageSuccess(OnSendMessageSuccessEvent event) {
        mvpView.showMessage(event.getMessage());
    }

    @Subscribe
    public void onSendMessageFailure(OnSendMessageFailureEvent event) {
        mvpView.showError(event.getError());
    }

    @Subscribe
    public void onTime(CheckMessages.OnTimerEvent event) {
        getDialogMessagesInteractor.execute(currentUser.getId(), contact.getId());
    }
}
