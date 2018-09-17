package com.example.bibek.myvehicle;

/**
 * Created by Pratik on 5/16/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBAdapter {

    static final String KEY_ROWID = "_id";
    static final String KEY_VEHICLE = "vehicle";
    static final String DRIVER = "driver";
    static final String REGO = "rego";
    static final String START = "start";
    static final String FIRST = "first";
    static final String SECOND = "second";
    static final String END = "end";

    static final String DATABASE_NAME = "logs";
    static final String DATABASE_TABLE = "logs_table";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            "create table logs_table (_id integer primary key autoincrement, "
                    + "vehicle integer not null, driver text not null, rego text not null," +
                    "start text not null, first text not null, second text not null, end text not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            //         Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            //                + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a Entry into the database---
    public long insertEntry(int vehicle, String driver, String rego, String start, String first, String second, String end)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_VEHICLE, vehicle);
        initialValues.put(DRIVER, driver);
        initialValues.put(REGO, rego);
        initialValues.put(START, start);
        initialValues.put(FIRST, first);
        initialValues.put(SECOND, second);
        initialValues.put(END, end);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes all entries---
    public boolean removeAll()
    {
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }

    //---deletes a particular entry---
    public boolean deleteEntry(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the entry---
    public Cursor getAllEntries()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_VEHICLE, DRIVER,
                REGO, START, FIRST, SECOND, END}, null, null, null, null, null);
    }

    //---retrieves a particular entry---
    public Cursor getEntry(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_VEHICLE, DRIVER, REGO, START, FIRST, SECOND, END}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a entry---
    public boolean updateEntry(long rowId, int vehicle, String driver, String rego, String start, String first, String second, String end)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_VEHICLE, vehicle);
        args.put(DRIVER, driver);
        args.put(REGO, rego);
        args.put(START, start);
        args.put(FIRST, first);
        args.put(SECOND, second);
        args.put(END, end);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
