package com.nodomain.manyface.model;


public class Message {

    private String text;
    private long firstAttemptTime;
    private long sentTime;
    private long senderId;
    private long receiverId;
    private Status status;

    private Message(String text,
                    long firstAttemptTime,
                    long sentTime,
                    long senderId,
                    long receiverId,
                    Status status) {
        this.text = text;
        this.firstAttemptTime = firstAttemptTime;
        this.sentTime = sentTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public static Message createUnread(Message unreadMessage, long sentTime) {
        return new Message(
                unreadMessage.text,
                unreadMessage.firstAttemptTime,
                sentTime,
                unreadMessage.senderId,
                unreadMessage.receiverId,
                Status.UNREAD);
    }

    public static Message createUnsent(String text, long firstAttemptTime, long senderId, long receiverId) {
        return new Message(text, firstAttemptTime, 0, senderId, receiverId, Status.UNSENT);
    }

    public static Message createReceived(String text, long sentTime, long senderId, Status status) {
        return new Message(text, 0, sentTime, senderId, -1, status);
    }

    public static Message copy(Message message) {
        return new Message(
                message.text,
                message.firstAttemptTime,
                message.sentTime,
                message.senderId,
                message.receiverId,
                message.status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (firstAttemptTime != message.firstAttemptTime) return false;
        if (senderId != message.senderId) return false;
        if (receiverId != message.receiverId) return false;
        return text != null ? text.equals(message.text) : message.text == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (int) (firstAttemptTime ^ (firstAttemptTime >>> 32));
        result = 31 * result + (int) (senderId ^ (senderId >>> 32));
        result = 31 * result + (int) (receiverId ^ (receiverId >>> 32));
        return result;
    }

    public String getText() {
        return text;
    }

    public long getFirstAttemptTime() {
        return firstAttemptTime;
    }

    public long getSentTime() {
        return sentTime;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        SENDING, UNSENT, UNREAD, READ
    }
}
