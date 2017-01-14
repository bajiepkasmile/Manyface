package com.nodomain.manyface.data.datasources.remote.impl;


public class ApiConstants {

    public static final String BASE_URL = "http://151.248.123.236/";

    public interface HttpCodes {
        int NOT_ACCEPTABLE = 406;
        int CONFLICT = 409;
    }

    public interface MediaTypes {
        String IMAGE = "image/jpeg";
    }
}
