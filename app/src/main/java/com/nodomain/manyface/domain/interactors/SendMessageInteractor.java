package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseQueueTasksInteractor;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.concurrent.ExecutorService;


public class SendMessageInteractor extends BaseQueueTasksInteractor {

    private final MessagesRepository messagesRepository;
    private final NetworkUtil networkUtil;

    public SendMessageInteractor(Handler mainThreadHandler,
                                 ExecutorService executorService,
                                 MessagesRepository messagesRepository,
                                 NetworkUtil networkUtil) {
        super(mainThreadHandler, executorService);
        this.messagesRepository = messagesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(Profile currentProfile, Profile contact, String messageText) {
        Message sendingMessage = Message.createUnsent(messageText, System.currentTimeMillis(),
                currentProfile.getId(), contact.getId());

        postEvent(new OnSendMessageStartEvent(sendingMessage));

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSendMessageFailureEvent(sendingMessage, Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                Message sentMessage = messagesRepository.sendMessage(sendingMessage);
                postOnMainThread(() -> postEvent(new OnSendMessageSuccessEvent(sentMessage)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() ->
                        postEvent(new OnSendMessageFailureEvent(sendingMessage, Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSendMessageStartEvent {

        private Message sendingMessage;

        public OnSendMessageStartEvent(Message sendingMessage) {
            this.sendingMessage = sendingMessage;
        }

        public Message getSendingMessage() {
            return sendingMessage;
        }
    }

    public static class OnSendMessageSuccessEvent {

        private Message message;

        public OnSendMessageSuccessEvent(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }
    }

    public static class OnSendMessageFailureEvent extends BaseFailureEvent {

        private Message unsentMessage;

        public OnSendMessageFailureEvent(Message message, Error error) {
            super(error);
        }

        public Message getUnsentMessage() {
            return unsentMessage;
        }
    }
}
