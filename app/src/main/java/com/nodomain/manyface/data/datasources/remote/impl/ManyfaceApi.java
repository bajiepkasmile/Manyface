package com.nodomain.manyface.data.datasources.remote.impl;


import com.nodomain.manyface.data.datasources.remote.impl.dtos.MessageDto;
import com.nodomain.manyface.data.datasources.remote.impl.dtos.ProfileDto;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ManyfaceApi {

    @POST("api/v1/auth")
    Call<Void> signUp(@Header("login") String login, @Header("password") String password);

    @GET("api/v1/auth")
    Call<Void> signIn(@Header("login") String login, @Header("password") String password);

    @GET("api/v1/user")
    Call<ProfileDto[]> getProfiles(@Header("AccessToken") String accessToken);

    @POST("api/v1/user")
    Call<ProfileDto> createProfile(@Header("AccessToken") String accessToken, @Body ProfileDto profileDto);

    @PUT("api/v1/user/{user-id}")
    Call<Void> updateProfile(@Header("AccessToken") String accessToken,
                             @Path("user-id") long profileId, @Body ProfileDto profileDto);

    @DELETE("api/v1/user")
    Call<Void> deleteProfile(@Header("AccessToken") String accessToken, @Header("UserId") long profileId);

    @GET("api/v1/contact")
    Call<ProfileDto[]> getProfileContacts(@Header("AccessToken") String accessToken,
                                          @Header("user-id") long profileId);

    @GET("api/v1/message/{contact-id}")
    Call<MessageDto[]> getDialogMessages(@Header("AccessToken") String accessToken,
                                         @Header("user-id") long profileId, @Path("contact-id") long contactId);

    @GET("/api/v1/message")
    @Headers("readed: false")
    Call<MessageDto[]> getAllUnreadMessages(@Header("AccessToken") String accessToken,
                                         @Header("user-id") long profileId);

    @PUT("api/v1/message/{contact-id}/accept")
    Call<Void> setMessagesRead(@Header("AccessToken") String accessToken,
                               @Header("user-id") long profileId, @Path("contact-id") long contactId);

    @POST("api/v1/message/{contact-id}")
    Call<String> sendMessage(@Header("AccessToken") String accessToken,
                             @Header("user-id") long profileId, @Path("contact-id") long contactId,
                             @Body String text);

    @GET("api/v1/search/username/{name}")
    Call<ProfileDto[]> searchForContacts(@Header("AccessToken") String accessToken,
                                         @Path("name") String contactName);

    @POST("api/v1/user/{id}/photo")
    Call<Void> setProfilePicture(@Header("AccessToken") String accessToken,
                            @Path("id") long profileId, @Body RequestBody profilePicture);
}
