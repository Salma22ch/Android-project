package com.example.task_management_app.My_tasks;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.R;
import com.example.task_management_app.adapters.TaskRecyclerAdapter;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;

public class My_tasks extends Fragment{

    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;
    private RecyclerView recyclerView;
    private TaskRecyclerAdapter taskRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Create or retrieve the database
        dbHelper = new DBOpenHelper(this.getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        // open the database
        open();

        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mytasks, container, false);

        //creating the recyclerview
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taskRecyclerAdapter = new TaskRecyclerAdapter(getDatabaseTasks());
        recyclerView.setAdapter(taskRecyclerAdapter);
        taskRecyclerAdapter.notifyDataSetChanged();

        return view;

    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public ArrayList<Note> getDatabaseTasks() {
        ArrayList<Note> notes = new ArrayList<Note>();
        Note note;
        Cursor res = db.rawQuery("select * from Note", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Integer id = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID));
            String title = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TITLE));
            String description = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DESCRIPTION));
            String category = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_CATEGORY));
            long date = res.getLong(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DATE));
            String priority = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PRIORITY));
            String state = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_STATE));
            String type = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TYPE));

            note = new Note(id, title, description, category, date, priority, state, type);
            notes.add(note);

            res.moveToNext();
        }
        return notes;
    }


}

