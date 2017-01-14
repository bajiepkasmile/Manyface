package com.nodomain.manyface.data.datasources.remote;


import com.nodomain.manyface.data.datasources.remote.impl.AccountManager;
import com.nodomain.manyface.data.datasources.remote.impl.ApiConstants;
import com.nodomain.manyface.data.datasources.remote.impl.DtoMapper;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.domain.exeptions.TooManyProfilesException;
import com.nodomain.manyface.domain.exeptions.ProfileAlreadyExistsException;
import com.nodomain.manyface.model.Profile;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;


public class ProfilesRemoteStorage {

    private final ManyfaceApi api;
    private final AccountManager accountManager;

    @Inject
    public ProfilesRemoteStorage(ManyfaceApi api, AccountManager accountManager) {
        this.api = api;
        this.accountManager = accountManager;
    }

    public List<Profile> getProfiles() throws ConnectionFailedException {
        try {
            return tryToGetProfiles();
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private List<Profile> tryToGetProfiles() throws IOException {
        Response<ProfileDto[]> response = submitGetProfilesRequest();
        return getProfilesFromResponse(response);
    }

    private Response<ProfileDto[]> submitGetProfilesRequest() throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.getProfiles(accessToken).execute();
    }

    private List<Profile> getProfilesFromResponse(Response<ProfileDto[]> response) {
        ProfileDto[] profileDtos = response.body();
        return DtoMapper.fromDto(profileDtos);
    }

    public Profile createProfile(String name, String description)
            throws ProfileAlreadyExistsException, TooManyProfilesException, ConnectionFailedException {
        try {
            return tryToCreateProfile(name, description);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private Profile tryToCreateProfile(String name, String description)
            throws ProfileAlreadyExistsException, TooManyProfilesException, ConnectionFailedException, IOException {
        ProfileDto profileDto = createProfileDtoByNameAndDescription(name, description);
        Response<ProfileDto> response = submitCreateProfileRequest(profileDto);
        checkCreateProfileResponseIsSuccessful(response);
        return getCreatedProfileFromResponse(response);
    }

    private ProfileDto createProfileDtoByNameAndDescription(String name, String description) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.name = name;
        profileDto.description = description;
        return profileDto;
    }

    private Response<ProfileDto> submitCreateProfileRequest(ProfileDto profileDto) throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.createProfile(accessToken, profileDto).execute();
    }

    private void checkCreateProfileResponseIsSuccessful(Response response)
            throws ProfileAlreadyExistsException, TooManyProfilesException {
        if (response.code() == ApiConstants.HttpCodes.NOT_ACCEPTABLE)
            throw new ProfileAlreadyExistsException();

        if (response.code() == ApiConstants.HttpCodes.CONFLICT)
            throw new TooManyProfilesException();
    }

    private Profile getCreatedProfileFromResponse(Response<ProfileDto> response) {
        ProfileDto createdProfileDto = response.body();
        return DtoMapper.fromDto(createdProfileDto);
    }

    public void updateProfile(Profile profile) throws ProfileAlreadyExistsException, ConnectionFailedException {
        try {
            tryToUpdateProfile(profile);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private void tryToUpdateProfile(Profile profile) throws ProfileAlreadyExistsException, IOException {
        ProfileDto profileDto = DtoMapper.toDto(profile);
        Response<Void> response = submitUpdateProfileRequest(profileDto);
        checkUpdateProfileResponseIsSuccessful(response);
    }

    private Response<Void> submitUpdateProfileRequest(ProfileDto profileDto) throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.updateProfile(accessToken, profileDto.id, profileDto).execute();
    }

    private void checkUpdateProfileResponseIsSuccessful(Response response) throws ProfileAlreadyExistsException {
        if (response.code() == ApiConstants.HttpCodes.CONFLICT)
            throw new ProfileAlreadyExistsException();
    }

    public void deleteProfile(Profile profile) throws ConnectionFailedException {
        try {
            submitDeleteProfileRequest(profile);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private void submitDeleteProfileRequest(Profile profile) throws IOException {
        String accessToken = accountManager.getAccessToken();
        api.deleteProfile(accessToken, profile.getId()).execute();
    }

    public void setProfilePicture(Profile profile, byte[] pictureByteArray) throws ConnectionFailedException {
        try {
            tryToSetProfilePicture(profile, pictureByteArray);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private void tryToSetProfilePicture(Profile profile, byte[] pictureByteArray) throws IOException {
        RequestBody requestBody = createSetProfilePictureRequestBody(pictureByteArray);
        submitSetProfilePictureRequest(profile, requestBody);
    }

    private RequestBody createSetProfilePictureRequestBody(byte[] pictureByteArray) {
        MediaType mediaType = MediaType.parse(ApiConstants.MediaTypes.IMAGE);
        return RequestBody.create(mediaType, pictureByteArray);
    }

    private void submitSetProfilePictureRequest(Profile profile, RequestBody requestBody) throws IOException {
        String accessToken = accountManager.getAccessToken();
        api.setProfilePicture(accessToken, profile.getId(), requestBody).execute();
    }
}
