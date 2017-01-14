package com.nodomain.manyface.data.datasources.cache;


import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;


public class MessagesCache {

    private List<Message> cachedUnsentMessages;
    private List<Message> cachedUnreadMessages;
    private List<Message> cachedDialogMessages;
    private long profileIdForWhichDialogMessagesWereCached; //TODO: find better names
    private long contactIdForWhichDialogMessagesWereCached;

    @Inject
    public MessagesCache() {
    }

    public boolean hasUnsentMessages() {
        return cachedUnsentMessages != null;
    }

    public boolean hasUnsentMessagesForContact(Profile contact) {
        if (!hasUnsentMessages())
            return false;

        for (Message unsentMessage : cachedUnsentMessages)
            if (unsentMessage.getReceiverId() == contact.getId())
                return true;

        return false;
    }

    public List<Message> getUnsentMessagesForContact(Profile contact) {
        if (hasUnsentMessages()) {
            ArrayList<Message> unsentMessagesForContact = new ArrayList<>();
            for (Message unsentMessage : cachedUnsentMessages) {
                if (unsentMessage.getReceiverId() == contact.getId()) {
                    Message messageCopy = Message.copy(unsentMessage); //return copy to achieve immutability of cache
                    unsentMessagesForContact.add(messageCopy);
                }
            }
            return unsentMessagesForContact;
        } else {
            return Collections.emptyList();
        }
    }

    public void rewriteUnsentMessages(List<Message> unsentMessages) {
        cachedUnsentMessages = copyMessages(unsentMessages);
    }

    public void addUnsentMessage(Message message) {
        if (cachedUnsentMessages == null) {
            cachedUnsentMessages = new ArrayList<>();
        }

        Message messageCopy = Message.copy(message); //save copy to achieve immutability of cache
        cachedUnsentMessages.add(messageCopy);
    }

    public void deleteUnsentMessage(Message message) {
        cachedUnsentMessages.remove(message);
    }

    public boolean hasUnreadMessages() {
        return cachedUnreadMessages != null;
    }

    public List<Message> getUnreadMessages() {
        if (hasUnreadMessages()) {
            return copyMessages(cachedUnreadMessages); //return copy to achieve immutability of cache
        } else {
            return Collections.emptyList();
        }
    }

    public void rewriteUnreadMessages(List<Message> unreadMessages) {
        cachedUnreadMessages = copyMessages(unreadMessages);
    }

    public void clearUnreadMessages() {
        cachedUnreadMessages = null;
    }

    public boolean hasDialogMessages(Profile profile, Profile contact) {
        return hasDialogMessages(profile.getId(), contact.getId());
    }

    private boolean hasDialogMessages(long profileId, long contactId) {
        return profileIdForWhichDialogMessagesWereCached == profileId
                && contactIdForWhichDialogMessagesWereCached == contactId
                && cachedDialogMessages != null;
    }

    public List<Message> getDialogMessages(Profile profile, Profile contact) {
        if (hasDialogMessages(profile, contact)) {
            return copyMessages(cachedDialogMessages); //return copy to achieve immutability of cache
        } else {
            return Collections.emptyList();
        }
    }

    public void rewriteDialogMessages(Profile profile, Profile contact, List<Message> dialogMessages) {
        cachedDialogMessages = copyMessages(dialogMessages);
        profileIdForWhichDialogMessagesWereCached = profile.getId();
        contactIdForWhichDialogMessagesWereCached = contact.getId();
    }

    public void addDialogMessage(Message message) {
        if (hasDialogMessages(message.getSenderId(), message.getReceiverId())) {
            cachedDialogMessages.add(message);
        }
    }

    public void clear() {
        cachedUnsentMessages = null;
        cachedUnreadMessages = null;
        cachedDialogMessages = null;
    }

    private List<Message> copyMessages(List<Message> messages) {
        List<Message> messagesCopy = new ArrayList<>();
        for (Message message : messages) {
            Message messageCopy = Message.copy(message);
            messagesCopy.add(messageCopy);
        }
        return messagesCopy;
    }
}
