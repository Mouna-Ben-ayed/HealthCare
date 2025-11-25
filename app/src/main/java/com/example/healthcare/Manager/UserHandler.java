package com.example.healthcare.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserHandler {
    private final SQLiteHelper dbHelper;
    private static final String TABLE_USERS = SQLiteHelper.TABLE_USERS;
    private static final String COLUMN_ID = SQLiteHelper.COLUMN_ID;
    private static final String COLUMN_USERNAME = SQLiteHelper.COLUMN_USERNAME;
    private static final String COLUMN_EMAIL = SQLiteHelper.COLUMN_EMAIL;
    private static final String COLUMN_PASSWORD = SQLiteHelper.COLUMN_PASSWORD;
    public UserHandler(Context context){

        dbHelper = new SQLiteHelper(context);

    }

    public long registerUser(String username, String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }


    public int loginUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            int userIdColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            userId = cursor.getInt(userIdColumnIndex);

            cursor.close();
        }

        db.close();

        return userId;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            int userIdColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            userId = cursor.getInt(userIdColumnIndex);

            cursor.close();
        }

        db.close();

        return userId;
    }
}
