package com.nodomain.manyface.data.datasources.remote;


import com.nodomain.manyface.data.datasources.remote.impl.DtoMapper;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;
import com.nodomain.manyface.utils.TimeConverter;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;


public class MessagesRemoteStorage {

    private final ManyfaceApi api;
    private final AccountManager accountManager;

    @Inject
    public MessagesRemoteStorage(ManyfaceApi api, AccountManager accountManager) {
        this.api = api;
        this.accountManager = accountManager;
    }

    public List<Message> getDialogMessages(Profile profile, Profile contact) throws ConnectionFailedException {
        try {
            return tryToGetDialogMessages(profile, contact);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private List<Message> tryToGetDialogMessages(Profile profile, Profile contact) throws IOException {
        Response<MessageDto[]> response = submitGetDialogMessagesRequest(profile, contact);
        return getDialogMessagesFromResponse(response);
    }

    private Response<MessageDto[]> submitGetDialogMessagesRequest(Profile profile, Profile contact)
            throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.getDialogMessages(accessToken, profile.getId(), contact.getId()).execute();
    }

    private List<Message> getDialogMessagesFromResponse(Response<MessageDto[]> response) {
        MessageDto[] dialogMessageDtos = response.body();
        return DtoMapper.fromDto(dialogMessageDtos);
    }

    public List<Message> getUnreadMessagesForProfile(Profile profile) throws ConnectionFailedException {
        try {
            return tryToGetUnreadMessagesForProfile(profile);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private List<Message> tryToGetUnreadMessagesForProfile(Profile profile) throws IOException {
        Response<MessageDto[]> response = submitGetUnreadMessagesForProfileRequest(profile);
        return getUnreadMessagesFromResponse(response);
    }

    private Response<MessageDto[]> submitGetUnreadMessagesForProfileRequest(Profile profile) throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.getAllUnreadMessages(accessToken, profile.getId()).execute();
    }

    private List<Message> getUnreadMessagesFromResponse(Response<MessageDto[]> response) {
        MessageDto[] messageDtos = response.body();
        return DtoMapper.fromDto(messageDtos);
    }

    public void setDialogMessagesRead(Profile profile, Profile contact) throws ConnectionFailedException {
        try {
            submitSetMessageReadRequest(profile.getId(), contact.getId());
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private void submitSetMessageReadRequest(long profileId, long contactId) throws IOException {
        String accessToken = accountManager.getAccessToken();
        api.setMessagesRead(accessToken, profileId, contactId).execute();
    }

    public Message sendMessage(Message message) throws ConnectionFailedException {
        try {
            return tryToSendMessage(message);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private Message tryToSendMessage(Message message) throws IOException {
        Response<String> response = submitSendMessageRequest(message);
        long sentTime = getMessageSentTimeFromResponse(response);
        return Message.createUnread(message, sentTime);
    }

    private Response<String> submitSendMessageRequest(Message message) throws IOException {
        String accessToken = accountManager.getAccessToken();
        String messageEncoded = message.getText();
        return api
                .sendMessage(accessToken, message.getSenderId(), message.getReceiverId(), messageEncoded)
                .execute();
    }

    private long getMessageSentTimeFromResponse(Response<String> response) {
        String timeStr = response.body();
        return TimeConverter.fullDateStringToTime(timeStr);
    }
}
