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

import static com.nodomain.manyface.data.datasources.local.impl.DbConstants.TABLE_PROFILES;


public class ProfilesLocalStorage {

    private final DbHelper dbHelper;

    @Inject
    public ProfilesLocalStorage(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Profile> getProfiles() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor profilesCursor = queryProfilesFromDb(db);
        List<Profile> profiles = getProfilesFromCursor(profilesCursor);
        profilesCursor.close();
        dbHelper.close();
        return profiles;
    }

    private Cursor queryProfilesFromDb(SQLiteDatabase db) {
        return db.query(TABLE_PROFILES, null, null, null, null, null, null);
    }

    private List<Profile> getProfilesFromCursor(Cursor profilesCursor) {
        int columnProfileIds = profilesCursor.getColumnIndex(TableProfiles.PROFILE_ID);
        int columnNames = profilesCursor.getColumnIndex(TableProfiles.NAME);
        int columnDescriptions = profilesCursor.getColumnIndex(TableProfiles.DESCRIPTION);
        int columnPictureUrls = profilesCursor.getColumnIndex(TableProfiles.PICTURE_URL);

        List<Profile> profiles = new ArrayList<>(profilesCursor.getCount());

        profilesCursor.moveToFirst();
        while (!profilesCursor.isAfterLast()) {
            long profileId = profilesCursor.getLong(columnProfileIds);
            String name = profilesCursor.getString(columnNames);
            String description = profilesCursor.getString(columnDescriptions);
            String pictureUrl = profilesCursor.getString(columnPictureUrls);

            Profile profile = new Profile(profileId, name, description, pictureUrl);
            profiles.add(profile);
            profilesCursor.moveToNext();
        }

        return profiles;
    }

    public void rewriteProfiles(List<Profile> profiles) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteProfilesFromDb(db);
        insertProfilesIntoDb(profiles, db);
        dbHelper.close();
    }

    private void insertProfilesIntoDb(List<Profile> profiles, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        for (Profile profile : profiles) {
            putProfileIntoContentValues(profile, cv);
            db.insert(TABLE_PROFILES, null, cv);
        }
    }

    public void clearProfiles() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteProfilesFromDb(db);
        dbHelper.close();
    }

    private void deleteProfilesFromDb(SQLiteDatabase db) {
        db.delete(TABLE_PROFILES, null, null);
    }

    public void saveProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        insertProfileIntoDb(profile, db);
        dbHelper.close();
    }

    private void insertProfileIntoDb(Profile profile, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        putProfileIntoContentValues(profile, cv);
        db.insert(TABLE_PROFILES, null, cv);
    }

    public void updateProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        updateProfileInDb(profile, db);
        dbHelper.close();
    }

    private void updateProfileInDb(Profile profile, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        putProfileIntoContentValues(profile, cv);

        String whereClause = TableProfiles.PROFILE_ID + "=?";
        String profileIdStr = Long.toString(profile.getId());
        String[] whereArgs = new String[]{profileIdStr};

        db.update(TABLE_PROFILES, cv, whereClause, whereArgs);
    }

    public void deleteProfile(Profile profile) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteProfileFromDb(profile.getId(), db);
        dbHelper.close();
    }

    private void deleteProfileFromDb(long profileId, SQLiteDatabase db) {
        String whereClause = TableProfiles.PROFILE_ID + "=?";
        String profileIdStr = Long.toString(profileId);
        String[] whereArgs = new String[]{profileIdStr};

        db.delete(TABLE_PROFILES, whereClause, whereArgs);
    }

    private void putProfileIntoContentValues(Profile profile, ContentValues cv) {
        cv.put(TableProfiles.PROFILE_ID, profile.getId());
        cv.put(TableProfiles.NAME, profile.getName());
        cv.put(TableProfiles.DESCRIPTION, profile.getDescription());
        cv.put(TableProfiles.PICTURE_URL, profile.getPictureUrl());
    }
}
