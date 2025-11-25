package com.example.healthcare.Manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MySQLite";
    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MEDICINES = "medicines";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_DOCTORS = "doctors";

    public static final String TABLE_HISTORIQUE = "health_data";


    // Common column names
    public static final String COLUMN_ID = "_id";

    // USERS Table - column names
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // MEDICINES Table - column names
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MEDICINE_NAME = "name";
    public static final String COLUMN_MEDICINE_HOUR = "hour";
    public static final String COLUMN_MEDICINE_MINUTE = "minute";

    // APPOINTMENTS Table - column names
    public static final String COLUMN_DOCTOR_NAME = "doctor_name";
    public static final String COLUMN_APPOINTMENT_DATETIME = "appointment_datetime";

    // DOCTORS Table - column names
    public static final String COLUMN_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";


    public static final String COLUMN_MEDICAL_HISTORY = "medical_history";
    public static final String COLUMN_TEST_RESULTS = "test_results";
    public static final String COLUMN_ALLERGIES = "allergies";
    public static final String COLUMN_VACCINATIONS = "vaccinations";
    public static final String COLUMN_ADDITIONAL_DETAILS = "addition";
    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USERNAME + " TEXT NOT NULL," +
            COLUMN_EMAIL + " TEXT NOT NULL," +
            COLUMN_PASSWORD + " TEXT NOT NULL" +
            ")";

    private static final String CREATE_TABLE_MEDICINES = "CREATE TABLE " + TABLE_MEDICINES +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_ID + " INTEGER," +
            COLUMN_MEDICINE_NAME + " TEXT NOT NULL," +
            COLUMN_MEDICINE_HOUR + " INTEGER," +
            COLUMN_MEDICINE_MINUTE + " INTEGER" +
            ")";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS +
            "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DOCTOR_NAME + " TEXT, " +
            COLUMN_APPOINTMENT_DATETIME + " TEXT" +
            ")";

    private static final String CREATE_TABLE_DOCTORS = "CREATE TABLE " + TABLE_DOCTORS +
            "(" +
            COLUMN_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USER_ID + " INTEGER," +
            COLUMN_FIRST_NAME + " TEXT," +
            COLUMN_LAST_NAME + " TEXT," +
            COLUMN_PHONE + " TEXT," +
            COLUMN_ADDRESS + " TEXT," +
            "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
            ")";

    private static final String CREATE_TABLE_HISTORIQUE = "CREATE TABLE " + TABLE_HISTORIQUE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_MEDICAL_HISTORY + " TEXT, "
            + COLUMN_TEST_RESULTS + " TEXT, "
            + COLUMN_ALLERGIES + " TEXT, "
            + COLUMN_VACCINATIONS + " TEXT, "
            + COLUMN_ADDITIONAL_DETAILS + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
            + ")";


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_MEDICINES);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_DOCTORS);
        db.execSQL(CREATE_TABLE_HISTORIQUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIQUE);

        // create new tables
        onCreate(db);
    }
}
