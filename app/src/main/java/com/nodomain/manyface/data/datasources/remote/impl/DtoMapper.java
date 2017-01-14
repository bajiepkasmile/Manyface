package com.nodomain.manyface.data.datasources.remote.impl;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Message.Status;
import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.List;


public class DtoMapper {

    public static Profile fromDto(ProfileDto profileDto) {
        return new Profile(profileDto.id, profileDto.name, profileDto.description, profileDto.pictureUrl);
    }

    public static Message fromDto(MessageDto messageDto) {
        long sentTime = TimeConverter.stringToTime(messageDto.sentTime);
        Status status = messageDto.isRead ? Status.READ : Status.UNREAD;
        return Message.createReceived(messageDto.text, sentTime, messageDto.senderId, status);
    }

    public static List<Profile> fromDto(ProfileDto[] profileDtos) {
        ArrayList<Profile> profiles = new ArrayList<>(profileDtos.length);
        for (ProfileDto profileDto : profileDtos) {
            profiles.add(fromDto(profileDto));
        }
        return profiles;
    }

    public static List<Message> fromDto(MessageDto[] messageDtos) {
        ArrayList<Message> messages = new ArrayList<>(messageDtos.length);
        for (MessageDto messageDto : messageDtos) {
            messages.add(fromDto(messageDto));
        }
        return messages;
    }

    public static ProfileDto toDto(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.id = profile.getId();
        profileDto.name = profile.getName();
        profileDto.description = profile.getDescription();
        profileDto.pictureUrl = profile.getPictureUrl();
        return profileDto;
    }

    public static MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.text = message.getText();
        messageDto.senderId = message.getSenderId();
        messageDto.sentTime = TimeConverter.timeToSting(message.getSentTime());
        messageDto.isRead = message.getStatus() == Status.READ;
        return messageDto;
    }
}
