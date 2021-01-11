package com.example.task_management_app.Calendar;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.CloseGuard;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.net.NoRouteToHostException;
import java.util.Arrays;
import java.util.Calendar;

import com.example.task_management_app.MainActivity;
import com.example.task_management_app.R;

import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Note;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.*;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MyCalendar extends Fragment {

    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private static final String TAG = "MyCalendar";
    private Toolbar toolbar;
    private ImageView imageView;
    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        //find the view
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        final ListView TasksListView = view.findViewById(R.id.task_listview);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        imageView = (ImageView) view.findViewById(R.id.calendar_imageView);


        final List<String> listOfTasks = new ArrayList<>();
        final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listOfTasks);
        TasksListView.setAdapter(adapter);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_format_calendar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);


        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        compactCalendarView.invalidate();
        compactCalendarView.setUseThreeLetterAbbreviation(true);


        loadEvents();


        //set initial title
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                //load task lists of the day
                List<Event> tasksFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                if (tasksFromMap != null) {
                    listOfTasks.clear();
                    for (Event task : tasksFromMap) {
                        listOfTasks.add((String) task.getData());
                    }
                    adapter.notifyDataSetChanged();
                    if (tasksFromMap.isEmpty()){
                        imageView.setVisibility(View.VISIBLE);
                    }else{

                        imageView.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });
        //addDummyEvents();
        //  gotoToday();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    


    private void loadEvents() {
        dbOpenHelper = new DBOpenHelper(getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);

        openDB();
        List<Event> events= getEvents();
        compactCalendarView.addEvents(events);
    }


    private List<Event> getEvents() {

        ArrayList<Note> listOfTask = getAllRecord();
        ArrayList<Event> listOfEvent = new ArrayList<Event>();

        for (Note list : listOfTask) {
            listOfEvent.add(new Event(Color.argb(255, 169, 68, 65), list.getDate(),  list.getTitle()+" : "+list.getDescription()));
        }

        return listOfEvent;
    }


    public void openDB() throws SQLiteException {
        try {
            sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        }
    }

    public long insertData(Long db_date, String db_category, String db_type, String db_title, String db_details, String db_prority, String db_state) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBOpenHelper.Constants.KEY_COL_DATE, db_date);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_CATEGORY, db_category);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TYPE, db_type);
        //time is deleted
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, db_title);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, db_details);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PRIORITY, db_prority);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_STATE, db_state);

        // Insert the line in the database
        long rowId = sqLiteDatabase.insert(DBOpenHelper.Constants.MY_TABLE_Note, null, contentValues);
        return rowId;
    }

    /**
     * this methode for fetching data from the dataBase
     **/
    public ArrayList<Note> getAllRecord() {

        ArrayList<Note> listOfNote = new ArrayList<Note>();
        Note note;
        Cursor res = sqLiteDatabase.rawQuery("select * from Note", null);
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
            listOfNote.add(note);

            res.moveToNext();
        }
        return listOfNote;
    }


    public void gotoToday() {
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }


}
