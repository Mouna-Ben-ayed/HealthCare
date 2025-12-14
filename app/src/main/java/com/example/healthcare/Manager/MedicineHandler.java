package com.example.healthcare.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.healthcare.Models.Medicine;

import java.util.ArrayList;
import java.util.List;

public class MedicineHandler {

    private final SQLiteHelper dbHelper;

    public MedicineHandler(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public long addMedicine(int userId, String medicineName, int hour, int minute) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_USER_ID, userId);
        values.put(SQLiteHelper.COLUMN_MEDICINE_NAME, medicineName);
        values.put(SQLiteHelper.COLUMN_MEDICINE_HOUR, hour);
        values.put(SQLiteHelper.COLUMN_MEDICINE_MINUTE, minute);

        long id = db.insert(SQLiteHelper.TABLE_MEDICINES, null, values);
        db.close();
        return id;
    }

    public List<Medicine> getMedicinesByUserId(int userId) {
        List<Medicine> medicines = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = SQLiteHelper.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query(
                SQLiteHelper.TABLE_MEDICINES,
                null,
                selection,
                selectionArgs,
                null,
                null,
                SQLiteHelper.COLUMN_MEDICINE_HOUR + " ASC, " + SQLiteHelper.COLUMN_MEDICINE_MINUTE + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_MEDICINE_NAME));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_MEDICINE_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_MEDICINE_MINUTE));

                Medicine medicine = new Medicine(userId, name, hour, minute);
                medicine.setMedicineId(id);
                medicines.add(medicine);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return medicines;
    }

    public boolean deleteMedicine(long medicineId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(SQLiteHelper.TABLE_MEDICINES,
                SQLiteHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(medicineId)});
        db.close();
        return rows > 0;
    }

}
