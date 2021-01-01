package com.example.task_management_app.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.task_management_app.Add.Add;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static class Constants implements BaseColumns {
        // The database name
        public static final String DATABASE_NAME = "Tododb.db";

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

        public static final String KEY_COL_DETAILS = "deatila";

        public static final String KEY_COL_PRIORITY = "priority";


        // Index des colonnes
        public static final int ID_COLUMN = 1;
        public static final int DATE_COLUMN = 2;
        public static final int TIME_COLUMN = 3;
        public static final int CATEGORY_COLOR_COLUMN = 4;
        public static final int TYPE_COLOR_COLUMN = 5;
        public static final int TITLE_COLUMN = 6;
        public static final int DETAILS_COLUMN = 7;
        public static final int PRIORITY_COLUMN = 8;
    }

    // The static string to create the database.
    private static final String DATABASE_CREATE = "create table "
            + Constants.MY_TABLE + "(" + Constants.KEY_COL_ID
            + " integer primary key autoincrement, "  + Constants.KEY_COL_DATE+ " TEXT, "
            + Constants.KEY_COL_TIME + " TEXT, "
            + Constants.KEY_COL_CATEGORY + " TEXT, "
            + Constants.KEY_COL_TYPE+ " TEXT, "
            + Constants.KEY_COL_TITLE + " TEXT, "
            + Constants.KEY_COL_DETAILS + " TEXT, "
            + Constants.KEY_COL_PRIORITY + " INTEGER)";


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
}
