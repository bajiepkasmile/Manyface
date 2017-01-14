package com.nodomain.manyface.data.datasources.remote.impl.dtos;


import com.google.gson.annotations.SerializedName;


public class ProfileDto {

    @SerializedName("id")
    public long id;
    @SerializedName("username")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("photoLink")
    public String pictureUrl;
}
