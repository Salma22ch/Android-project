package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.MainActivity;
import com.example.task_management_app.R;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyGoalDetails extends AppCompatActivity implements GestureDetector.OnGestureListener, OnNavigationButtonClickedListener {
    private static final float SWIPE_THRESHOLD = 100;
    private static final float SWIPE_VOLACITY_THRESHOLD = 100;
    GestureDetector gestureDetector;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    Intent i;
    Goal goal;

    DBOpenHelper dbOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    private BroadcastReceiver monReceiver;

    CustomCalendar customCalendar;

    boolean isTodaychecked = false;

    HashMap<Integer, Object> mapDateToDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal_details);
        toolbar = findViewById(R.id.mygoals_details_toolbar);
        gestureDetector = new GestureDetector(this);
        progressBar = (ProgressBar) findViewById(R.id.mygoal_details_progressBar);

        i = getIntent();
        goal = (Goal) i.getSerializableExtra("GoalObject");

        TextView mygoalDetailsProgressMax = (TextView) findViewById(R.id.mygoal_details_progressMax);
        TextView mygoalDetailsProgressCurrent = (TextView) findViewById(R.id.mygoal_details_progressCurent);
        TextView mygoalDetailsDescription = (TextView) findViewById(R.id.mygoal_details_description);
        TextView mygoalDetailsTitle = (TextView) findViewById(R.id.mygoal_details_title);


        customCalendar = (CustomCalendar) findViewById(R.id.custom_calendar);


        dbOpenHelper = new DBOpenHelper(this, DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);


        toolbar.setTitle(goal.getTitle());


        /**
         * handle the refresh
         */
        handleRefresh();


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.deleteGoal) {
                    openDB();
                    dbOpenHelper.deleteGoal(goal, sqLiteDatabase);
                    Toast.makeText(getApplicationContext(), "delete goal", Toast.LENGTH_SHORT).show();
                    closeDB();
                    //goback();
                    goToGoals();
                } else if (item.getItemId() == R.id.editGoal) {
                    DialogFragment dialog_goal = Edit_Goal.newInstance(goal);
                    dialog_goal.show(getSupportFragmentManager(), "tag");
                } else {
                    // do something
                }

                return false;
            }
        });

        toolbar.setTitle(goal.getTitle());
        toolbar.setLogo(goal.getIcon());


        mygoalDetailsProgressCurrent.setText(goal.getProgressCurrent().toString());
        mygoalDetailsProgressMax.setText(goal.getMaxProgress().toString());
        progressBar.setMax(goal.getMaxProgress());
        progressBar.setProgress(goal.getProgressCurrent());
        mygoalDetailsDescription.setText(goal.getDescription());
        //mygoalsDetailsicon.setImageResource(goal.getIcon());
        mygoalDetailsTitle.setText(goal.getTitle());


        /**
         * define the layouts of the tree cases
         */
        HashMap<Object, Property> mapDescToProp = new HashMap<>();

        Property propDefault = new Property();
        propDefault.layoutResource = R.layout.default_view;
        propDefault.dateTextViewResource = R.id.default_datetextview;
        mapDescToProp.put("default", propDefault);

        Property propUnavailable = new Property();
        propUnavailable.layoutResource = R.layout.unavailable_view;
        //propUnavailable.enable = false;
        propUnavailable.dateTextViewResource = R.id.unavailable_datetextview;
        mapDescToProp.put("unavailable", propUnavailable);

        Property propHoliday = new Property();
        propHoliday.layoutResource = R.layout.holiday_view;
        propHoliday.dateTextViewResource = R.id.holiday_datetextview;
        mapDescToProp.put("holiday", propHoliday);

        customCalendar.setMapDescToProp(mapDescToProp);
        /**
         * ----------------------------------------------------------------------------
         */


        /**
         * add the days with their descriptions
         */

        Calendar calendar = Calendar.getInstance();
        mapDateToDesc = new HashMap<>();


        /**
         * put the cheked days
         */
        mapDateToDesc.put(getToday(), "default");
        mapDateToDesc.putAll(addchekedDays(getThisMounth()));

        /**
         * put the uncheked days
         */
        mapDateToDesc.putAll(addUnchekedDays(getThisMounth()));


        customCalendar.setDate(calendar, mapDateToDesc);
        /**
         * -------------------------------------------------------------------------
         */
        /**
         * listners for change the mounth with navigation button
         */
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);


        /**
         * listner when day clicked
         */
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                //Snackbar.make(customCalendar, selectedDate.get(Calendar.DAY_OF_MONTH) + " selected", Snackbar.LENGTH_LONG).show();
            }
        });


    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        float diffX = moveEvent.getX() - downEvent.getX();
        float diffY = moveEvent.getY() - downEvent.getY();
        boolean result = false;

        if (Math.abs(diffY) > Math.abs(diffX)) { //vertical
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VOLACITY_THRESHOLD) {
                if (diffY > 0) {
                    swipeDown();
                } else
                    swipeUP();

            }
            //result = true;
        }
        return result;
    }

    public void goback() {
        this.finish();
    }

    public void goToGoals() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        Fragment fragment =  new Goals();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
//        getSupportFragmentManager().executePendingTransactions();


        Toast.makeText(getApplicationContext(), "edit goal", Toast.LENGTH_SHORT).show();
    }

    private void swipeDown() {
        goback();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    private void swipeUP() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void openDB() throws SQLiteException {
        try {
            sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        }
    }

    public void closeDB() {
        sqLiteDatabase.close();
    }

    public void handleRefresh() {
        monReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                // i = getIntent();
                goal = (Goal) i.getSerializableExtra("GoalObject");
            }
        };
        getApplicationContext().registerReceiver(monReceiver, new IntentFilter("com.example.broadcastDismiss.goaldetails"));
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        HashMap<Integer, Object> hashMap;
        switch (newMonth.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.JANUARY);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.JANUARY));
                break;
            case Calendar.FEBRUARY:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.FEBRUARY);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.FEBRUARY));
                break;
            case Calendar.MARCH:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.MARCH);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.MARCH));
                break;
            case Calendar.APRIL:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.APRIL);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.APRIL));
                break;
            case Calendar.MAY:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.MAY);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.MAY));
                break;
            case Calendar.JUNE:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.JUNE);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.JUNE));
                break;
            case Calendar.JULY:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.JULY);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.JULY));
                break;
            case Calendar.AUGUST:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.AUGUST);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.AUGUST));
                break;
            case Calendar.SEPTEMBER:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.SEPTEMBER);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.SEPTEMBER));
                break;
            case Calendar.OCTOBER:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.OCTOBER);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.OCTOBER));
                break;
            case Calendar.NOVEMBER:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.NOVEMBER);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.NOVEMBER));
                break;
            case Calendar.DECEMBER:
                arr[0] = new HashMap<>();
                arr[0].put(getToday(), "default");
                hashMap = addchekedDays(Calendar.DECEMBER);
                arr[0].putAll(hashMap);
                arr[0].putAll(addUnchekedDays(Calendar.DECEMBER));
                break;
        }
        return arr;
    }

    private int getToday() {
        Calendar date = Calendar.getInstance();
        int today = date.get(Calendar.DAY_OF_MONTH);
        return today;
    }

    private int getThisMounth() {
        Calendar date = Calendar.getInstance();
        int thisMounth = date.get(Calendar.MONTH);
        return thisMounth;
    }


    public HashMap<Integer, Object> addUnchekedDays(int mounth) {
        ArrayList<Long> arrayListOfDaysFromDB = goal.getArrayListOfDays();

        HashMap<Integer, Object> arr = new HashMap<Integer, Object>();
        //get the date when the goal was created
        long daycreated = goal.getDateCreated();
        //day of today
        Date date = java.util.Calendar.getInstance().getTime();
        Long todayByMilli = date.getTime();
        //------------------

        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();

        while (daycreated < todayByMilli) {
            isTodaychecked = false;
            calendar3.setTimeInMillis(daycreated);
            int createdDayConvertedOfyear = calendar3.get(Calendar.DAY_OF_YEAR);
            int createdDayConvertedOfmounth = calendar3.get(Calendar.DAY_OF_MONTH);
            int createdMounth = calendar3.get(Calendar.MONTH);
            //Log.d("aaaaaqqqqq", "createdDayConverted: " + createdDayConvertedOfyear);

            for (Long time : arrayListOfDaysFromDB) {
                calendar4.setTimeInMillis(time);
                int mDay = calendar4.get(Calendar.DAY_OF_YEAR);
                Log.d("aaaaaqqqqq", "mday: " + calendar3.get(Calendar.DATE));
                if (createdDayConvertedOfyear == mDay) {
                    isTodaychecked = true;
                }
            }
            Log.d("aaaaaqqqqq", "===== getToday() ======= " + getToday());
            Log.d("aaaaaqqqqq", "=====createdDayConvertedOfmounth==== " + createdDayConvertedOfmounth);
            if (isTodaychecked == false && createdMounth == mounth && getToday() != createdDayConvertedOfmounth) {
                Log.d("aaaaaqqqqq", "ha li galna ");
                arr.put(createdDayConvertedOfmounth, "unavailable");
            }

            daycreated += 86400000l;
        }
        return arr;
    }


    public HashMap<Integer, Object> addchekedDays(int mounth) {
        ArrayList<Long> arrayListOfDaysFromDB = goal.getArrayListOfDays();
        HashMap<Integer, Object> arr = new HashMap<Integer, Object>();
        Calendar calendar2 = Calendar.getInstance();
        //put the day cheked
        for (Long time : arrayListOfDaysFromDB) {
            calendar2.setTimeInMillis(time);
            int getMounth = calendar2.get(Calendar.MONTH);
            if (getMounth == mounth) {
                int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
                arr.put(mDay, "holiday");
            }

        }
        return arr;
    }
}