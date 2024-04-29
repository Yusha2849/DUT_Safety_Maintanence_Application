package com.example.dutmaintanenceapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReportDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "maintenance_reports.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_REPORTS = "reports";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CAMPUS = "campus";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_BLOCK = "block";
    private static final String COLUMN_ISSUE_TYPE = "issue_type";
    private static final String COLUMN_DESCRIPTION = "description";
    private  static final String COLUMN_STATUS="status";

    // SQL query to create the reports table
    private static final String SQL_CREATE_TABLE_REPORTS =
            "CREATE TABLE " + TABLE_REPORTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CAMPUS + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_BLOCK + " TEXT, " +
                    COLUMN_ISSUE_TYPE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT)";

    public ReportDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_REPORTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed and create fresh
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        onCreate(db);
    }

    // Method to add a new report to the database
    public long addReport(String campus, String location, String block, String issueType, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAMPUS, campus);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_BLOCK, block);
        values.put(COLUMN_ISSUE_TYPE, issueType);
        values.put(COLUMN_DESCRIPTION, description);
        long newRowId = db.insert(TABLE_REPORTS, null, values);
        db.close();
        return newRowId;
    }

    // Method to read all reports from the database
    public Cursor getAllReports() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REPORTS, null, null, null, null, null, null);
    }

    // Method to update a report in the database
    public int updateReport(long id, String campus, String location, String block, String issueType, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAMPUS, campus);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_BLOCK, block);
        values.put(COLUMN_ISSUE_TYPE, issueType);
        values.put(COLUMN_DESCRIPTION, description);
        return db.update(TABLE_REPORTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Method to delete a report from the database
    public int deleteReport(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REPORTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
