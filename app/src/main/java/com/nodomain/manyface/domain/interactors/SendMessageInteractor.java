package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseQueueTasksInteractor;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
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

    public void execute(long userId, long contactId, String text) {
        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnSendMessageFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                MessageDto message = new MessageDto(text, "", userId); //TODO: get time ?
                MessageDto sentMessage = messagesRepository.sendMessageToContact(message, contactId);
                postOnMainThread(() -> postEvent(new OnSendMessageSuccessEvent(sentMessage)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnSendMessageFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnSendMessageSuccessEvent {

        private MessageDto message;

        public OnSendMessageSuccessEvent(MessageDto message) {
            this.message = message;
        }

        public MessageDto getMessage() {
            return message;
        }
    }

    public static class OnSendMessageFailureEvent extends BaseFailureEvent {

        public OnSendMessageFailureEvent(Error error) {
            super(error);
        }
    }
}
