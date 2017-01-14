package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.utils.NetworkUtil;

import java.util.List;


public class GetDialogMessagesInteractor extends BaseSingleTaskInteractor {

    private final MessagesRepository messagesRepository;
    private final NetworkUtil networkUtil;

    public GetDialogMessagesInteractor(Handler mainThreadHandler,
                                       MessagesRepository messagesRepository,
                                       NetworkUtil networkUtil) {
        super(mainThreadHandler);
        this.messagesRepository = messagesRepository;
        this.networkUtil = networkUtil;
    }

    public void execute(long userId, long contactId) {
        if (messagesRepository.hasCachedDialogMessages(userId, contactId)) {
            List<MessageDto> dialogMessages = messagesRepository.getCachedDialogMessages(userId, contactId);
            postEvent(new OnGetDialogMessagesSuccessEvent(dialogMessages));
            return;
        }

        boolean networkIsNotAvailable = !networkUtil.isNetworkAvailable();
        if (networkIsNotAvailable) {
            postEvent(new OnGetDialogMessagesFailureEvent(Error.NETWORK_IS_NOT_AVAILABLE));
            return;
        }

        runInBackground(() -> {
            try {
                List<MessageDto> dialogMessages = messagesRepository.getDialogMessages(userId, contactId);
                postOnMainThread(() -> postEvent(new OnGetDialogMessagesSuccessEvent(dialogMessages)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnGetDialogMessagesFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnGetDialogMessagesSuccessEvent {

        private List<MessageDto> dialogMessages;

        public OnGetDialogMessagesSuccessEvent(List<MessageDto> dialogMessages) {
            this.dialogMessages = dialogMessages;
        }

        public List<MessageDto> getDialogMessages() {
            return dialogMessages;
        }
    }

    public static class OnGetDialogMessagesFailureEvent extends BaseFailureEvent {

        public OnGetDialogMessagesFailureEvent(Error error) {
            super(error);
        }
    }
}
