package com.nodomain.manyface.data.datasources.remote.impl.dtos;


import com.google.gson.annotations.SerializedName;


public class MessageDto {

    @SerializedName("message")
    public String text;
    @SerializedName("send_time")
    public String sentTime;
    @SerializedName("sender")
    public long senderId;
    @SerializedName("readed")
    public boolean isRead;
}
