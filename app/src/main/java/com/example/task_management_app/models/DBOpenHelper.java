package com.example.task_management_app.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static class Constants implements BaseColumns {
        // The database name
        public static final String DATABASE_NAME = "TaskDb.db";

        // The database version
        public static final int DATABASE_VERSION = 1;

        // The table Name
        public static final String MY_TABLE = "Note";

        public static final String KEY_COL_ID = "id";// Mandatory

        public static final String KEY_COL_DATE = "date";

        public static final String KEY_COL_TIME = "time";

        public static final String KEY_COL_CATEGORY = "category";

        public static final String KEY_COL_TYPE = "type";

        public static final String KEY_COL_TITLE = "title";

        public static final String KEY_COL_DESCRIPTION = "Description";

        public static final String KEY_COL_PRIORITY = "priority";

        public static final String KEY_COL_STATE = "State";


        // Index des colonnes
        public static final int ID_COLUMN = 1;
        public static final int DATE_COLUMN = 2;
        public static final int TIME_COLUMN = 3;
        public static final int CATEGORY_COLOR_COLUMN = 4;
        public static final int TYPE_COLOR_COLUMN = 5;
        public static final int TITLE_COLUMN = 6;
        public static final int DECRIPTION_COLUMN = 7;
        public static final int PRIORITY_COLUMN = 8;
    }

    // The static string to create the database.
    private static final String DATABASE_CREATE = "create table "
            + Constants.MY_TABLE + "(" + Constants.KEY_COL_ID
            + " integer primary key autoincrement, "
            + Constants.KEY_COL_DATE + " BIGINT, "
            + Constants.KEY_COL_CATEGORY + " TEXT, "
            + Constants.KEY_COL_TYPE + " TEXT, "
            + Constants.KEY_COL_TITLE + " TEXT, "
            + Constants.KEY_COL_DESCRIPTION + " TEXT, "
            + Constants.KEY_COL_PRIORITY + " TEXT ,"
            + Constants.KEY_COL_STATE + " TEXT)";


    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the new database using the SQL string Database_create
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        // Drop the old database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.MY_TABLE);
        // Create the new one
        onCreate(db);
        // or do a smartest stuff
    }


    /**
     * this methode for inserting data in the dataBase and returning the rowId
     **/
    public long insertData(String db_date, String db_time, String db_category, String db_type, String db_title, String db_details, String db_prority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Assign the values for each column.
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DATE, db_date);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TIME, db_time);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_CATEGORY, db_category);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TYPE, db_type);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, db_title);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, db_details);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PRIORITY, db_prority);

        // Insert the line in the database
        long rowId = db.insert(DBOpenHelper.Constants.MY_TABLE, null, contentValues);
        return rowId;
    }

    /**
     * this methode for fetching data from the dataBase
     **/
    public ArrayList<Note> getAllRecord() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> listOfNote = new ArrayList<Note>();
        Note note;

        Cursor res = db.rawQuery("select * from Note", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            //integer
            Integer id = res.getInt(res.getColumnIndex(Constants.KEY_COL_ID));
            //string
            String title = res.getString(res.getColumnIndex(Constants.KEY_COL_TITLE));
            String description = res.getString(res.getColumnIndex(Constants.KEY_COL_DESCRIPTION));
            String category = res.getString(res.getColumnIndex(Constants.KEY_COL_ID));
            //date
            Date date = new Date(res.getLong(res.getColumnIndex(Constants.KEY_COL_DATE)));
            //integer
            Integer priority = res.getInt(res.getColumnIndex(Constants.KEY_COL_PRIORITY));
            //String
            String state = res.getString(res.getColumnIndex(Constants.KEY_COL_TYPE));


            //note = new Note(id, title, description, category, date, priority, state);
            //listOfNote.add(note);
            res.moveToNext();
        }
        return listOfNote;
    }
}
