package com.example.healthcare.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.healthcare.Models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHandler {

    private static final String TAG = "AppointmentHandler";
    private final SQLiteHelper dbHelper;

    // Références simplifiées
    private static final String TABLE_APPOINTMENTS = SQLiteHelper.TABLE_APPOINTMENTS;
    private static final String COLUMN_ID = SQLiteHelper.COLUMN_ID;
    private static final String COLUMN_DOCTOR_NAME = SQLiteHelper.COLUMN_DOCTOR_NAME;
    private static final String COLUMN_APPOINTMENT_DATETIME = SQLiteHelper.COLUMN_APPOINTMENT_DATETIME;

    public AppointmentHandler(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    // ADD
    public long addAppointment(String doctorName, String appointmentDatetime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, doctorName);
        values.put(COLUMN_APPOINTMENT_DATETIME, appointmentDatetime);

        try {
            id = db.insert(TABLE_APPOINTMENTS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Erreur insertion rendez-vous: " + e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }

    // GET TEXT LIST
    public List<String> getAppointmentDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<String> appointmentDetails = new ArrayList<>();

        try {
            cursor = db.query(
                    TABLE_APPOINTMENTS,
                    new String[]{COLUMN_DOCTOR_NAME, COLUMN_APPOINTMENT_DATETIME},
                    null, null, null, null, null
            );

            if (cursor != null) {
                int nDoctor = cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_NAME);
                int nDate = cursor.getColumnIndexOrThrow(COLUMN_APPOINTMENT_DATETIME);

                while (cursor.moveToNext()) {
                    String doctorName = cursor.getString(nDoctor);
                    String dateTime = cursor.getString(nDate);

                    appointmentDetails.add("Doctor: " + doctorName + " - " + dateTime);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Erreur getAppointmentDetails: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return appointmentDetails;
    }

    // GET FULL OBJECT
    public List<Appointment> getAppointments() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_APPOINTMENTS,
                    null,
                    null, null, null, null,
                    COLUMN_APPOINTMENT_DATETIME + " ASC"
            );

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                int docIndex = cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_NAME);
                int dateIndex = cursor.getColumnIndexOrThrow(COLUMN_APPOINTMENT_DATETIME);

                do {
                    int id = cursor.getInt(idIndex);
                    String doctor = cursor.getString(docIndex);
                    String datetime = cursor.getString(dateIndex);

                    list.add(new Appointment(id, doctor, datetime));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e(TAG, "Erreur getAppointments: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }

    // DELETE ONE
    public int deleteAppointment(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;

        try {
            rows = db.delete(TABLE_APPOINTMENTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Erreur deleteAppointment: " + e.getMessage());
        } finally {
            db.close();
        }

        return rows;
    }

    // DELETE ALL
    public int deleteAllAppointments() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;

        try {
            rows = db.delete(TABLE_APPOINTMENTS, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Erreur deleteAllAppointments: " + e.getMessage());
        } finally {
            db.close();
        }

        return rows;
    }
}
