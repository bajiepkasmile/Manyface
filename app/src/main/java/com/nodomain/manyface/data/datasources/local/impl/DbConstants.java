package com.nodomain.manyface.data.datasources.local.impl;


public class DbConstants {

    public static String DB_NAME = "Manyface_db";
    public static int DB_VERSION = 1;

    public static String TABLE_PROFILES = "profiles";
    public static String TABLE_CONTACTS = "contacts";
    public static String TABLE_UNSENT_MESSAGES = "unsent_messages";

    public interface TableProfiles {
        String PROFILE_ID = "profile_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String PICTURE_URL = "picture_url";
    }

    public interface TableContacts extends TableProfiles {
        String CONTACT_ID = "contact_id";
    }

    public interface TableUnsentMessages {
        String TEXT = "text";
        String FIRST_ATTEMPT_TIME = "first_attempt_time";
        String SENDER_ID = "sender_id";
        String RECEIVER_ID = "receiver_id";
    }
}
