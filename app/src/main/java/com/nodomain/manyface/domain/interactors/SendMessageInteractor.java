package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseQueueTasksInteractor;
import com.nodomain.manyface.model.Message;
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

    public void execute(Message message) {
        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSendMessageFailureEvent(message, Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                Message sentMessage = messagesRepository.sendMessage(message);
                postOnMainThread(() -> postEvent(new OnSendMessageSuccessEvent(sentMessage)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSendMessageFailureEvent(message, Error.CONNECTION_FAILED)));
            }
        });
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
