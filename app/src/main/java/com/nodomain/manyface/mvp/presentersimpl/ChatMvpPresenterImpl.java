package com.nodomain.manyface.mvp.presentersimpl;


import com.nodomain.manyface.domain.interactors.GetDialogMessagesInteractor;
import com.nodomain.manyface.domain.interactors.GetDialogMessagesInteractor.*;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.mvp.presenters.ChatMvpPresenter;
import com.nodomain.manyface.domain.interactors.SendMessageInteractor;
import com.nodomain.manyface.domain.interactors.SendMessageInteractor.*;
import com.nodomain.manyface.mvp.views.ChatMvpView;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;


public class ChatMvpPresenterImpl extends BaseMvpPresenterImpl<ChatMvpView> implements ChatMvpPresenter {

    private final GetDialogMessagesInteractor getDialogMessagesInteractor;
    private final SendMessageInteractor sendMessageInteractor;

    private Profile currentProfile;
    private Profile contact;

    @Inject
    public ChatMvpPresenterImpl(GetDialogMessagesInteractor getDialogMessagesInteractor,
                                SendMessageInteractor sendMessageInteractor) {
        this.getDialogMessagesInteractor = getDialogMessagesInteractor;
        this.sendMessageInteractor = sendMessageInteractor;
    }

    @Override
    public void init(Profile currentProfile, Profile contact) {
        this.currentProfile = currentProfile;
        this.contact = contact;
        mvpView.showChatMembers(currentProfile, contact);
    }

    @Override
    public void getMessages() {
        mvpView.showGetMessagesProgress();
        getDialogMessagesInteractor.execute(currentProfile, contact);
    }

    @Override
    public void sendMessage(String text) {
        sendMessageInteractor.execute(currentProfile, contact, text);
    }

    @Override
    public void navigateToBack() {
        mvpView.showPreviousView();
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
    public void onSendMessageStart(OnSendMessageStartEvent event) {
        mvpView.showSendingMessage(event.getSendingMessage());
    }

    @Subscribe
    public void onSendMessageSuccess(OnSendMessageSuccessEvent event) {
        mvpView.showSendMessageSuccess(event.getMessage());
    }

    @Subscribe
    public void onSendMessageFailure(OnSendMessageFailureEvent event) {
        mvpView.showSendMessageError(event.getUnsentMessage());
        mvpView.showError(event.getError());
    }
}
