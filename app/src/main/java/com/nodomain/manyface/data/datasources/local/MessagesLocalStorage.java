package com.nodomain.manyface.data.datasources.local;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nodomain.manyface.data.datasources.local.impl.DbConstants.*;
import com.nodomain.manyface.data.datasources.local.impl.DbHelper;
import com.nodomain.manyface.model.Message;
import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.nodomain.manyface.data.datasources.local.impl.DbConstants.TABLE_UNSENT_MESSAGES;


public class MessagesLocalStorage {

    private final DbHelper dbHelper;

    @Inject
    public MessagesLocalStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Message> getUnsentMessages(Profile profile, Profile contact) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor unsentMessagesCursor = queryUnsentMessagesFromDb(profile.getId(), contact.getId(), db);
        List<Message> unsentMessages = getUnsentMessagesFromCursor(unsentMessagesCursor);
        unsentMessagesCursor.close();
        dbHelper.close();
        return unsentMessages;
    }

    private Cursor queryUnsentMessagesFromDb(long profileId, long contactId, SQLiteDatabase db) {
        String profileIdStr = Long.toString(profileId);
        String contactIdStr = Long.toString(contactId);
        String selection = TableUnsentMessages.SENDER_ID + "=?, " + TableUnsentMessages.RECEIVER_ID + "=?";
        String selectionArgs[] = new String[]{profileIdStr, contactIdStr};

        return db.query(TABLE_UNSENT_MESSAGES, null, selection, selectionArgs, null, null, null, null);
    }

    private List<Message> getUnsentMessagesFromCursor(Cursor unsentMessagesCursor) {
        int columnText = unsentMessagesCursor.getColumnIndex(TableUnsentMessages.TEXT);
        int columnFirstAttemptTime = unsentMessagesCursor.getColumnIndex(TableUnsentMessages.FIRST_ATTEMPT_TIME);
        int columnSenderId = unsentMessagesCursor.getColumnIndex(TableUnsentMessages.SENDER_ID);
        int columnReceiverId = unsentMessagesCursor.getColumnIndex(TableUnsentMessages.RECEIVER_ID);

        List<Message> unsentMessages = new ArrayList<>(unsentMessagesCursor.getCount());
        unsentMessagesCursor.moveToFirst();
        while (!unsentMessagesCursor.isAfterLast()) {
            String text = unsentMessagesCursor.getString(columnText);
            long firstAttemptTime = unsentMessagesCursor.getLong(columnFirstAttemptTime);
            long senderId = unsentMessagesCursor.getLong(columnSenderId);
            long receiverId = unsentMessagesCursor.getLong(columnReceiverId);

            Message unsentMessage = Message.createUnsent(text, firstAttemptTime, senderId, receiverId);
            unsentMessages.add(unsentMessage);
            unsentMessagesCursor.moveToNext();
        }

        return unsentMessages;
    }

    public void saveUnsentMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        insertUnsentMessageIntoDb(message, db);
        dbHelper.close();
    }

    private void insertUnsentMessageIntoDb(Message message, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        putUnsentMessageIntoContentValues(message, cv);
        db.insert(TABLE_UNSENT_MESSAGES, null, cv);
    }

    public void deleteUnsentMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteUnsentMessageFromDb(message.getFirstAttemptTime(), db);
        dbHelper.close();
    }

    private void deleteUnsentMessageFromDb(long messageFirstAttemptTime, SQLiteDatabase db) {
        String firstAttemptTimeStr = Long.toString(messageFirstAttemptTime);
        String whereClause = TableUnsentMessages.FIRST_ATTEMPT_TIME + "=?";
        String[] whereArgs = new String[]{firstAttemptTimeStr};

        db.delete(TABLE_UNSENT_MESSAGES, whereClause, whereArgs);
    }

    private void putUnsentMessageIntoContentValues(Message message, ContentValues cv) {
        cv.put(TableUnsentMessages.TEXT, message.getText());
        cv.put(TableUnsentMessages.FIRST_ATTEMPT_TIME, message.getFirstAttemptTime());
        cv.put(TableUnsentMessages.SENDER_ID, message.getSenderId());
        cv.put(TableUnsentMessages.RECEIVER_ID, message.getReceiverId());
    }
}
