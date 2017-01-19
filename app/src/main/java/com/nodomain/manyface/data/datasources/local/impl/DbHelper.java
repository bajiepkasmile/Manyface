package com.nodomain.manyface.data.datasources.local.impl;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nodomain.manyface.data.datasources.local.impl.DbConstants.*;

import javax.inject.Inject;

import static com.nodomain.manyface.data.datasources.local.impl.DbConstants.*;


public class DbHelper extends SQLiteOpenHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INT = " INTEGER";
    private static final String COMMA_SEP = ", ";

    @Inject
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PROFILES + "("
                + DbConstants.TableProfiles.PROFILE_ID + TYPE_INT + COMMA_SEP
                + TableProfiles.NAME + TYPE_TEXT + COMMA_SEP
                + TableProfiles.DESCRIPTION + TYPE_TEXT + COMMA_SEP
                + TableProfiles.PICTURE_URL + TYPE_TEXT
                + ");"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONTACTS + "("
                + TableContacts.PROFILE_ID + TYPE_INT + COMMA_SEP
                + TableContacts.CONTACT_ID + TYPE_INT + COMMA_SEP
                + TableContacts.NAME + TYPE_TEXT + COMMA_SEP
                + TableContacts.DESCRIPTION + TYPE_TEXT + COMMA_SEP
                + TableContacts.PICTURE_URL + TYPE_TEXT
                + ");"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_UNSENT_MESSAGES + "("
                + TableUnsentMessages.TEXT + TYPE_TEXT + COMMA_SEP
                + TableUnsentMessages.FIRST_ATTEMPT_TIME + TYPE_INT + COMMA_SEP
                + TableUnsentMessages.SENDER_ID + TYPE_INT + COMMA_SEP
                + TableUnsentMessages.RECEIVER_ID + TYPE_INT
                + ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_UNSENT_MESSAGES);
        onCreate(sqLiteDatabase);
    }
}
