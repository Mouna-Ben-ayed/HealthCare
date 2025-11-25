package com.example.healthcare.Manager;

import static com.example.healthcare.Manager.SQLiteHelper.COLUMN_ADDITIONAL_DETAILS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.healthcare.Models.HealthRecord;

import java.util.ArrayList;
import java.util.List;

public class HealthRecordHandler{
    private final SQLiteHelper dbHelper;
    private static final String TABLE_HISTORIQUE = SQLiteHelper.TABLE_HISTORIQUE;
    private static final String COLUMN_ID = SQLiteHelper.COLUMN_ID;
    private static final String COLUMN_USER_ID = SQLiteHelper.COLUMN_USER_ID;
    private static final String COLUMN_MEDICAL_HISTORY = SQLiteHelper.COLUMN_MEDICAL_HISTORY;
    private static final String COLUMN_TEST_RESULTS = SQLiteHelper.COLUMN_TEST_RESULTS;
    private static final String COLUMN_ALLERGIES = SQLiteHelper.COLUMN_ALLERGIES;
    private static final String COLUMN_VACCINATIONS = SQLiteHelper.COLUMN_VACCINATIONS;

    private static final String COLUMN_ADDITIONAL_DETAILS = SQLiteHelper.COLUMN_ADDITIONAL_DETAILS;
    public HealthRecordHandler(Context context) {

        dbHelper = new SQLiteHelper(context);
    }

    public long insertData(int userId, String medicalHistory, String testResults, String allergies, String vaccinations, String addition) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MEDICAL_HISTORY, medicalHistory);
        values.put(COLUMN_TEST_RESULTS, testResults);
        values.put(COLUMN_ALLERGIES, allergies);
        values.put(COLUMN_VACCINATIONS, vaccinations);
        values.put(COLUMN_ADDITIONAL_DETAILS,addition);
        return db.insert(TABLE_HISTORIQUE, null, values);
    }


    public List<HealthRecord> getRecordsByUserId(int userId) {
        List<HealthRecord> recordsList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORIQUE + " WHERE " + COLUMN_USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int recordId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String medicalHistory = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICAL_HISTORY));
                String testResults = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEST_RESULTS));
                String allergies = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALLERGIES));
                String vaccinations = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINATIONS));
                String addition = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_DETAILS));
                HealthRecord record = new HealthRecord(1, medicalHistory, testResults, allergies, vaccinations, addition);
                recordsList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recordsList;
    }

    public void deleteData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_HISTORIQUE, null, null);
    }

    public HealthRecord getLastSavedRecord() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HISTORIQUE + " ORDER BY " + COLUMN_USER_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);

        HealthRecord healthRecord = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String medicalHistory = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDICAL_HISTORY));
            String testResults = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEST_RESULTS));
            String allergies = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALLERGIES));
            String vaccinations = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VACCINATIONS));
            String addition = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDITIONAL_DETAILS));
            healthRecord = new HealthRecord(userId, medicalHistory, testResults, allergies, vaccinations, addition);
        }

        cursor.close();
        db.close();

        return healthRecord;
    }
    public long updateData(HealthRecord healthRecord) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, healthRecord.getUserId());
        values.put(COLUMN_MEDICAL_HISTORY, healthRecord.getMedicalHistory());
        values.put(COLUMN_TEST_RESULTS, healthRecord.getTestResults());
        values.put(COLUMN_ALLERGIES, healthRecord.getAllergies());
        values.put(COLUMN_VACCINATIONS, healthRecord.getVaccinations());
        values.put(COLUMN_ADDITIONAL_DETAILS, healthRecord.getAdditionalDetails());

        // Check if a record with the given userId already exists
        int updatedRows;
        if (getRecordsByUserId(healthRecord.getUserId()).size() == 0)  {
            // No records exist, insert a new record
            values.put(COLUMN_USER_ID, 1); // Set the user id to 1 for the initial record
            updatedRows = (int) db.insert(TABLE_HISTORIQUE, null, values);
        } else {
            // Update the existing record
            updatedRows = db.update(TABLE_HISTORIQUE, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(healthRecord.getUserId())});
        }

        db.close();
        return updatedRows;
    }

}
