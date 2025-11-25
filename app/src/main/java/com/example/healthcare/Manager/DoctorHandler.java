package com.example.healthcare.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.healthcare.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorHandler {

    private final SQLiteHelper dbHelper;

    public DoctorHandler(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public int insertDoctor(int userId, Doctor doctor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USER_ID, userId);
        values.put(SQLiteHelper.COLUMN_FIRST_NAME, doctor.getFirstName());
        values.put(SQLiteHelper.COLUMN_LAST_NAME, doctor.getLastName());
        values.put(SQLiteHelper.COLUMN_PHONE, doctor.getPhone());
        values.put(SQLiteHelper.COLUMN_ADDRESS, doctor.getAddress());

        long insertedId = db.insert(SQLiteHelper.TABLE_DOCTORS, null, values);
        db.close();

        return (int) insertedId;
    }

    public int deleteDoctor(int doctorId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rows = db.delete(SQLiteHelper.TABLE_DOCTORS,
                SQLiteHelper.COLUMN_DOCTOR_ID + " = ?",
                new String[]{String.valueOf(doctorId)});

        db.close();
        return rows;
    }

    public List<Doctor> getDoctorsByUserId(int userId) {
        List<Doctor> doctorsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.TABLE_DOCTORS,
                null,
                SQLiteHelper.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DOCTOR_ID));
                String first = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_FIRST_NAME));
                String last = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_LAST_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_PHONE));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ADDRESS));

                Doctor doctor = new Doctor(first, last, phone, address);
                doctor.setId(id);
                doctorsList.add(doctor);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return doctorsList;
    }
}
