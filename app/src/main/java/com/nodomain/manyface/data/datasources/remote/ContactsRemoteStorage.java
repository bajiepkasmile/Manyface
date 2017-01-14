package com.nodomain.manyface.data.datasources.remote;


import com.nodomain.manyface.data.datasources.remote.impl.AccountManager;
import com.nodomain.manyface.data.datasources.remote.impl.DtoMapper;
import com.nodomain.manyface.data.datasources.remote.impl.ManyfaceApi;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;
import com.nodomain.manyface.domain.exeptions.ConnectionFailedException;
import com.nodomain.manyface.model.Profile;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;


public class ContactsRemoteStorage {

    private final ManyfaceApi api;
    private final AccountManager accountManager;

    @Inject
    public ContactsRemoteStorage(ManyfaceApi api, AccountManager accountManager) {
        this.api = api;
        this.accountManager = accountManager;
    }

    public List<Profile> getProfileContacts(Profile profile) throws ConnectionFailedException {
        try {
            return tryToGetProfileContacts(profile);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private List<Profile> tryToGetProfileContacts(Profile profile) throws IOException {
        Response<ProfileDto[]> response = submitGetProfileContactsRequest(profile);
        return getProfileContactsFromResponse(response);
    }

    private Response<ProfileDto[]> submitGetProfileContactsRequest(Profile profile) throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.getProfileContacts(accessToken, profile.getId()).execute();
    }

    private List<Profile> getProfileContactsFromResponse(Response<ProfileDto[]> response) {
        ProfileDto[] contactDtos = response.body();
        return DtoMapper.fromDto(contactDtos);
    }

    public List<Profile> searchForContacts(String contactName) throws ConnectionFailedException {
        try {
            return tryToSearchForContacts(contactName);
        } catch (IOException e) {
            throw new ConnectionFailedException();
        }
    }

    private List<Profile> tryToSearchForContacts(String contactName) throws IOException {
        Response<ProfileDto[]> response = submitSearchForContactsRequest(contactName);
        return getFoundedContactsFromResponse(response);
    }

    private Response<ProfileDto[]> submitSearchForContactsRequest(String contactName) throws IOException {
        String accessToken = accountManager.getAccessToken();
        return api.searchForContacts(accessToken, contactName).execute();
    }

    private List<Profile> getFoundedContactsFromResponse(Response<ProfileDto[]> response) {
        ProfileDto[] foundedContactDtos = response.body();
        return DtoMapper.fromDto(foundedContactDtos);
    }
}
