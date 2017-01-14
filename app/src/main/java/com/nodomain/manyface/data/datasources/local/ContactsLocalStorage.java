package com.nodomain.manyface.data.datasources.local;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nodomain.manyface.data.datasources.local.impl.DbConstants.*;
import com.nodomain.manyface.data.datasources.local.impl.DbHelper;
import com.nodomain.manyface.model.Profile;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.nodomain.manyface.data.datasources.local.impl.DbConstants.TABLE_CONTACTS;


public class ContactsLocalStorage {

    private final DbHelper dbHelper;

    @Inject
    public ContactsLocalStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Profile> getProfileContacts(Profile profile) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor contactsCursor = queryProfileContactsFromDb(profile.getId(), db);
        List<Profile> contacts = getContactsFromCursor(contactsCursor);
        contactsCursor.close();
        dbHelper.close();
        return contacts;
    }

    private Cursor queryProfileContactsFromDb(long profileId, SQLiteDatabase db) {
        String selection = TableContacts.PROFILE_ID + "=?";
        String profileIdStr = Long.toString(profileId);
        String[] selectionArgs = new String[]{profileIdStr};

        return db.query(TABLE_CONTACTS, null, selection, selectionArgs, null, null, null);
    }

    private List<Profile> getContactsFromCursor(Cursor contactsCursor) {
        int columnContactIds = contactsCursor.getColumnIndex(TableContacts.CONTACT_ID);
        int columnNames = contactsCursor.getColumnIndex(TableContacts.NAME);
        int columnDescriptions = contactsCursor.getColumnIndex(TableContacts.DESCRIPTION);
        int columnPictureUrls = contactsCursor.getColumnIndex(TableContacts.PICTURE_URL);

        List<Profile> contacts = new ArrayList<>(contactsCursor.getCount());
        contactsCursor.moveToFirst();
        while (!contactsCursor.isAfterLast()) {
            long contactId = contactsCursor.getLong(columnContactIds);
            String contactName = contactsCursor.getString(columnNames);
            String contactDescription = contactsCursor.getString(columnDescriptions);
            String contactPictureUrl = contactsCursor.getString(columnPictureUrls);

            Profile contact = new Profile(contactId, contactName, contactDescription, contactPictureUrl);

            contacts.add(contact);
            contactsCursor.moveToNext();
        }

        return contacts;
    }

    public void rewriteProfileContacts(Profile profile, List<Profile> contacts) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteProfileContactsFromDb(profile.getId(), db);
        insertProfileContactsIntoDb(profile.getId(), contacts, db);
        dbHelper.close();
    }

    private void deleteProfileContactsFromDb(long profileId, SQLiteDatabase db) {
        String whereClause = TableContacts.PROFILE_ID + "=?";
        String profileIdStr = Long.toString(profileId);
        String[] whereArgs = new String[]{profileIdStr};

        db.delete(TABLE_CONTACTS, whereClause, whereArgs);
    }

    private void insertProfileContactsIntoDb(long profileId, List<Profile> contacts, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        for (Profile contact : contacts) {
            putProfileContactIntoContentValues(profileId, contact, cv);
            db.insert(TABLE_CONTACTS, null, cv);
        }
    }

    public void saveProfileContact(Profile profile, Profile contact) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        insertProfileContactIntoDb(profile.getId(), contact, db);
        dbHelper.close();
    }

    private void insertProfileContactIntoDb(long profileId, Profile contact, SQLiteDatabase db) {
        ContentValues cvContact = new ContentValues();
        putProfileContactIntoContentValues(profileId, contact, cvContact);
        db.insert(TABLE_CONTACTS, null, cvContact);
    }

    public void clearAllContacts() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CONTACTS, null, null);
        dbHelper.close();
    }

    private void putProfileContactIntoContentValues(long profileId, Profile contact, ContentValues cv) {
        cv.put(TableContacts.PROFILE_ID, profileId);
        cv.put(TableContacts.CONTACT_ID, contact.getId());
        cv.put(TableContacts.NAME, contact.getName());
        cv.put(TableContacts.DESCRIPTION, contact.getDescription());
        cv.put(TableContacts.PICTURE_URL, contact.getPictureUrl());
    }
}
