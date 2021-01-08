package com.example.task_management_app.Calendar;


import android.os.Bundle;
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
    DBOpenHelper db;
    Note note;


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


        //loadEvents();
        //loadEventsForYear(2017);
        //logEventsByMonth(compactCalendarView);


        //set initial title
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {


                Toast.makeText(getActivity(), "Date : " + dateClicked.toString(), Toast.LENGTH_SHORT).show();
                toolbar.setTitle(dateFormatForMonth.format(dateClicked));

                //load task lists of the day
                List<Event> tasksFromMap = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "inside onclick " + dateFormatForDisplaying.format(dateClicked));

                if (tasksFromMap != null) {

                    Log.d(TAG, "aaaaaaaa" + tasksFromMap.toString());
                    listOfTasks.clear();
                    for (Event task : tasksFromMap) {
                        listOfTasks.add((String) task.getData());
                    }
                    adapter.notifyDataSetChanged();
                } else {

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


//    private void addDummyEvents() {
//        addEvents(compactCalendarView, Calendar.APRIL);
//        addEvents(compactCalendarView, Calendar.MAY);
//        addEvents(compactCalendarView, Calendar.JUNE);
//        // Refresh calendar to update events
//        compactCalendarView.invalidate();
//    }
//
//    private void addEvents(int month, int year) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
//        Date firstDayOfMonth = currentCalender.getTime();
//        for (int i = 0; i < 6; i++) {
//            currentCalender.setTime(firstDayOfMonth);
//            if (month > -1) {
//                currentCalender.set(Calendar.MONTH, month);
//            }
//            if (year > -1) {
//                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
//                currentCalender.set(Calendar.YEAR, year);
//            }
//            currentCalender.add(Calendar.DATE, i);
//            setToMidnight(currentCalender);
//            long timeInMillis = currentCalender.getTimeInMillis();
//
//            List<Event> events = getEvents(timeInMillis, i);
//
//            compactCalendarView.addEvents(events);
//        }
//    }


    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


//    private void loadEvents() {
//        addEvents(-1, -1);
//        addEvents(Calendar.DECEMBER, -1);
//        addEvents(Calendar.AUGUST, -1);
//    }

//    private void addEvents(int month, int year) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
//        Date firstDayOfMonth = currentCalender.getTime();
//        for (int i = 0; i < 30; i++) {
//            currentCalender.setTime(firstDayOfMonth);
//            if (month > -1) {
//                currentCalender.set(Calendar.MONTH, month);
//            }
//            if (year > -1) {
//                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
//                currentCalender.set(Calendar.YEAR, year);
//            }
//            currentCalender.add(Calendar.DATE, i);
//            setToMidnight(currentCalender);
//            long timeInMillis = currentCalender.getTimeInMillis();
//
//            List<Event> events = getEvents(timeInMillis, i);
//
//            compactCalendarView.addEvents(events);
//        }
//    }


    private void loadEvents() {
        List<Event> events = getEvents();
        compactCalendarView.addEvents(events);
        db.insertData("sd","23","VHIHAJA","VHIHAJA","VHIHAJA","VHIHAJA","VHIHAJA");
    }

    private List<Event> getEvents() {
        ArrayList<Note> listOfTask ;
        listOfTask = db.getAllRecord();
        ArrayList<Event> listOfEvent = new ArrayList<Event>();

        for (Note list : listOfTask){
            Log.d(TAG, "get date from database "+list.getDate());
        }

        listOfEvent.add(new Event(Color.argb(255, 169, 68, 65), 1610113527000L, "Task at " + new Date(1610280000000L)));
        listOfEvent.add(new Event(Color.argb(255, 100, 68, 65), 1610280000000L, "Task 2 at " + new Date(1610280000000L)));
        return listOfEvent;
    }

  /*
    private void loadEvents() {
        addEvents(3, 1, 6);
    }



    private void addEvents(int month, int year, int day) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, day);


        long timeInMillis = currentCalender.getTimeInMillis();
        List<Event> events = getEvents(timeInMillis, day);
        compactCalendarView.addEvents(events);
    }
    private List<Event> getEvents(long timeInMillis, int day) {
        ArrayList<Event> listOfEvent = new ArrayList<Event>();
        listOfEvent.add(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Task at " + new Date(timeInMillis)));
        listOfEvent.add(new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Task 2 at " + new Date(timeInMillis)));
        return listOfEvent;
    }
*/
    public void gotoToday() {
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }


}
