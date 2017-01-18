package com.nodomain.manyface.domain.interactors;


import android.os.Handler;

import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.data.repositories.MessagesRepository;
import com.nodomain.manyface.domain.Error;
import com.nodomain.manyface.domain.interactors.base.BaseSingleTaskInteractor;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;
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

    public void execute(Profile profile, Profile contact) {
        if (messagesRepository.hasCachedDialogMessages(profile, contact)) {
            List<Message> dialogMessages = messagesRepository.getCachedDialogMessages(profile, contact);
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
                List<Message> dialogMessages =
                        messagesRepository.getDialogMessagesFromRemoteStorage(profile, contact);
                postOnMainThread(() -> postEvent(new OnGetDialogMessagesSuccessEvent(dialogMessages)));
            } catch (ConnectionFailedException e) {
                postOnMainThread(() -> postEvent(new OnGetDialogMessagesFailureEvent(Error.CONNECTION_FAILED)));
            }
        });
    }

    public static class OnGetDialogMessagesSuccessEvent {

        private List<Message> dialogMessages;

        public OnGetDialogMessagesSuccessEvent(List<Message> dialogMessages) {
            this.dialogMessages = dialogMessages;
        }

        public List<Message> getDialogMessages() {
            return dialogMessages;
        }
    }

    public static class OnGetDialogMessagesFailureEvent extends BaseFailureEvent {

        public OnGetDialogMessagesFailureEvent(Error error) {
            super(error);
        }
    }
}
