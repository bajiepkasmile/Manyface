package com.nodomain.manyface.data.repositories;


import com.nodomain.manyface.data.datasources.cache.MessagesCache;
import com.nodomain.manyface.data.datasources.local.MessagesLocalStorage;
import com.nodomain.manyface.data.datasources.remote.MessagesRemoteStorage;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.util.List;


public class MessagesRepository {

    private final MessagesRemoteStorage remoteStorage;
    private final MessagesLocalStorage localStorage;
    private final MessagesCache cache;

    public MessagesRepository(MessagesRemoteStorage remoteStorage,
                              MessagesLocalStorage localStorage,
                              MessagesCache cache) {
        this.remoteStorage = remoteStorage;
        this.localStorage = localStorage;
        this.cache = cache;
    }

    public boolean hasCachedUnsentMessagesForContact(Profile contact) {
        return cache.hasUnsentMessagesForContact(contact);
    }

    public List<Message> getCachedUnsentMessagesForContact(Profile contact) {
        return cache.getUnsentMessagesForContact(contact);
    }

    public List<Message> getUnsentMessagesForContactFromLocalStorage(Profile profile, Profile contact) {
        List<Message> unsentMessages = localStorage.getUnsentMessages(profile, contact);
        cache.rewriteUnsentMessages(unsentMessages);
        return unsentMessages;
    }





    public boolean hasCachedUnreadMessages() {
        return cache.hasUnreadMessages();
    }

    public List<Message> getCachedUnreadMessages() {
        return cache.getUnreadMessages();
    }

    public List<Message> getUnreadMessagesForProfileFromRemoteStorage(Profile profile)
            throws ConnectionFailedException {
        List<Message> unreadMessages = remoteStorage.getUnreadMessagesForProfile(profile);
        cache.rewriteUnreadMessages(unreadMessages);
        return unreadMessages;
    }

    public void setDialogMessagesRead(Profile profile, Profile contact) throws ConnectionFailedException {
        remoteStorage.setDialogMessagesRead(profile, contact);
        cache.clearUnreadMessages();
    }

    public boolean hasCachedDialogMessages(Profile profile, Profile contact) {
        return cache.hasDialogMessages(profile, contact);
    }

    public List<Message> getCachedDialogMessages(Profile profile, Profile contact) {
        return cache.getDialogMessages(profile, contact);
    }

    public List<Message> getDialogMessagesFromRemoteStorage(Profile profile, Profile contact)
            throws ConnectionFailedException {
        List<Message> dialogMessages = remoteStorage.getDialogMessages(profile, contact);
        cache.rewriteDialogMessages(profile, contact, dialogMessages);
        return dialogMessages;
    }

    public void clearMessages() {
        cache.clear();
    }

    public Message sendMessage(Message message) throws ConnectionFailedException {
        try {
            Message sentMessage = remoteStorage.sendMessage(message);
            cache.addDialogMessage(sentMessage);
            return sentMessage;
        } catch (ConnectionFailedException e) {
            cache.addUnsentMessage(message);
            localStorage.saveUnsentMessage(message);
            throw e;
        }
    }

    public void resendMessage(Message unsentMessage) throws ConnectionFailedException {
        remoteStorage.sendMessage(unsentMessage);
        cache.deleteUnsentMessage(unsentMessage);
        localStorage.deleteUnsentMessage(unsentMessage);
    }
}
