package com.example.task_management_app.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;


    public static class Constants implements BaseColumns {
        // The database name
        public static final String DATABASE_NAME = "TaskDb.db";

        // The database version
        public static final int DATABASE_VERSION = 1;

        // The table Name
        public static final String MY_TABLE_Note = "Note";
        public static final String MY_TABLE_Goal = "Goal";

        public static final String KEY_COL_ID = "id";// Mandatory

        public static final String KEY_COL_DATE = "date";

        public static final String KEY_COL_TIME = "time";

        public static final String KEY_COL_CATEGORY = "category";

        public static final String KEY_COL_TYPE = "type";

        public static final String KEY_COL_TITLE = "title";

        public static final String KEY_COL_DESCRIPTION = "Description";

        public static final String KEY_COL_PRIORITY = "priority";

        public static final String KEY_COL_STATE = "State";

        public static final String KEY_COL_STATE_NOT = "state_notification";

        public static final String KEY_COL_ID_NOT = "id_notification";

        public static final String KEY_COL_PROGRESSMAX = "ProgressMax";

        public static final String KEY_COL_PROGRESSCURRENT = "ProgressCurrent";

        public static final String KEY_COL_ICON = "Icon";


        // Index des colonnes
        public static final int ID_COLUMN = 1;
        public static final int DATE_COLUMN = 2;
        public static final int TIME_COLUMN = 3;
        public static final int CATEGORY_COLOR_COLUMN = 4;
        public static final int TYPE_COLOR_COLUMN = 5;
        public static final int TITLE_COLUMN = 6;
        public static final int DECRIPTION_COLUMN = 7;
        public static final int PRIORITY_COLUMN = 8;
        public static final int STATE_NOTIFICATION = 9;
        public static final int ID_NOTIFICATION = 10;
    }

    // The static string to create the database.
    private static final String DATABASE_CREATE_GOAL = "create table "
            + Constants.MY_TABLE_Goal + "(" + Constants.KEY_COL_ID
            + " integer primary key autoincrement, "
            + Constants.KEY_COL_TITLE + " TEXT, "
            + Constants.KEY_COL_DESCRIPTION + " TEXT, "
            + Constants.KEY_COL_ICON + " BIGINT ,"
            + Constants.KEY_COL_PROGRESSMAX + " INT ,"
            + Constants.KEY_COL_PROGRESSCURRENT + " INT)";

    private static final String DATABASE_CREATE = "create table "
            + Constants.MY_TABLE_Note + "(" + Constants.KEY_COL_ID
            + " integer primary key autoincrement, "
            + Constants.KEY_COL_DATE + " BIGINT, "
            + Constants.KEY_COL_CATEGORY + " TEXT, "
            + Constants.KEY_COL_TYPE + " TEXT, "
            + Constants.KEY_COL_TITLE + " TEXT, "
            + Constants.KEY_COL_DESCRIPTION + " TEXT, "
            + Constants.KEY_COL_PRIORITY + " TEXT ,"
            + Constants.KEY_COL_STATE + " TEXT,"
            + Constants.KEY_COL_STATE_NOT + " TEXT,"
            + Constants.KEY_COL_ID_NOT + " BIGINT)";


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
        db.execSQL(DATABASE_CREATE_GOAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("DBOpenHelper", "Mise à jour de la version " + oldVersion
                + " vers la version " + newVersion
                + ", les anciennes données seront détruites ");
        // Drop the old database
        db.execSQL("DROP TABLE IF EXISTS " + Constants.MY_TABLE_Note);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.MY_TABLE_Goal);
        // Create the new one
        onCreate(db);
        // or do a smartest stuff
    }


    public int deleteGoal(Goal goal, SQLiteDatabase sqLiteDatabase) {

        String table = Constants.MY_TABLE_Goal;
        String whereClause = DBOpenHelper.Constants.KEY_COL_ID + " = ?";
        String[] whereArgs = {String.valueOf(goal.getId())};
        return sqLiteDatabase.delete(table, whereClause, whereArgs);

    }

    // Deleting a task. (TODO: Check for bugs in case of different list order)
    public int deleteTask(Note note, SQLiteDatabase sqLiteDatabase) {
        String table = Constants.MY_TABLE_Note;
        String whereClause = DBOpenHelper.Constants.KEY_COL_ID + " = ?";
        String[] whereArgs = {String.valueOf(note.getId())};
        return sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public int updateData(Goal goal,SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        String whereClause = DBOpenHelper.Constants.KEY_COL_ID + " = ?";
        contentValues.put(DBOpenHelper.Constants.KEY_COL_ID, goal.getId());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, goal.getTitle());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, goal.getDescription());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_ICON, goal.getIcon());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSMAX, goal.getMaxProgress());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSCURRENT, goal.getProgressCurrent() + 1);

        String[] whereArgs = {String.valueOf(goal.getId())};
        int i = sqLiteDatabase.update(DBOpenHelper.Constants.MY_TABLE_Goal, contentValues, whereClause, whereArgs);
        return i;
    }

    /**
     * get all record from database of table Goal
     * @return
     */
    public ArrayList<Goal> getAllRecord(SQLiteDatabase sqLiteDatabase) {

        ArrayList<Goal> listOfGoal = new ArrayList<Goal>();
        Goal goal;
        Cursor res = sqLiteDatabase.rawQuery("select * from " + DBOpenHelper.Constants.MY_TABLE_Goal, null);
        res.moveToFirst();
        if (res.isAfterLast() == true){
            return listOfGoal;
        }

        while (res.isAfterLast() == false) {
            Integer id = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID));
            String title = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TITLE));
            String description = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DESCRIPTION));
            Integer icon = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ICON));
            Integer progressMax = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PROGRESSMAX));
            Integer progressCurrent = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PROGRESSCURRENT));
            goal = new Goal(id, title, description, icon, progressMax, progressCurrent);
            listOfGoal.add(goal);
            res.moveToNext();
        }
        res.close();
        return listOfGoal;
    }

    public long insertData(Goal goal,SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, goal.getTitle());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, goal.getDescription());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_ICON, goal.getIcon());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSMAX, goal.getMaxProgress());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSCURRENT, goal.getProgressCurrent());

        // Insert the line in the database
        long rowId = sqLiteDatabase.insert(DBOpenHelper.Constants.MY_TABLE_Goal, null, contentValues);
        return rowId;
    }

}
