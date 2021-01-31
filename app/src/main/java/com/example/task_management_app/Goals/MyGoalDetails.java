package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.Add.Add;
import com.example.task_management_app.MainActivity;
import com.example.task_management_app.R;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;
import com.google.android.material.snackbar.Snackbar;

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

                if(item.getItemId()==R.id.deleteGoal)
                {
                    openDB();
                    dbOpenHelper.deleteGoal(goal,sqLiteDatabase);
                    Toast.makeText(getApplicationContext(),"delete goal",Toast.LENGTH_SHORT).show();
                    closeDB();
                    //goback();
                    goToGoals();
                }
                else if(item.getItemId()== R.id.editGoal)
                {
                    DialogFragment dialog_goal = Edit_Goal.newInstance(goal);
                    dialog_goal.show(getSupportFragmentManager(), "tag");
                }
                else{
                    // do something
                }

                return false;
            }
        });

        mygoalDetailsProgressCurrent.setText( goal.getProgressCurrent().toString());
        mygoalDetailsProgressMax.setText(goal.getMaxProgress().toString());
        progressBar.setMax(goal.getMaxProgress());
        progressBar.setProgress(goal.getProgressCurrent());
        mygoalDetailsDescription.setText(goal.getDescription());


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
        //You can leave the text view field blank. Custom calendar won't try to set a date on such views
        propUnavailable.enable = false;
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

        ArrayList<Long> arrayListOfDaysFromDB =  goal.getArrayListOfDays();

        HashMap<Integer, Object> mapDateToDesc = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        //put the day cheked
        for (Long time : arrayListOfDaysFromDB){
            calendar2.setTimeInMillis(time);
            int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
            mapDateToDesc.put(mDay, "holiday");
        }


      /*  //put the day unchecked

        long daycreated = goal.getDateCreated();
        Date date = java.util.Calendar.getInstance().getTime();
        Long today = date.getTime();


//
//        for (Long time : arrayList) {
//            calendar2.setTimeInMillis(time);
//            int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
//            if (mDay == today) {
//                isTodaychecked = true;
//            }
//        }

        while (daycreated<today){
            calendar2.setTimeInMillis(daycreated);
            int createdDayConverted = calendar2.get(Calendar.DAY_OF_MONTH);

            for (Long time : arrayListOfDaysFromDB) {
                calendar2.setTimeInMillis(time);
                int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
                if (mDay == createdDayConverted) {
                    isTodaychecked = true;
                }
            }
            if (mDay == ){

            }
            daycreated+=24*60*60;
        }
*/

//        mapDateToDesc.put(2, "default");
//        mapDateToDesc.put(5, "holiday");
//        mapDateToDesc.put(10, "default"); //You don't need to explicitly mention "default" description dates.
//        mapDateToDesc.put(11, "unavailable");
//        mapDateToDesc.put(19, "holiday");
//        mapDateToDesc.put(20, "holiday");
//        mapDateToDesc.put(24, "unavailable");
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
                Snackbar.make(customCalendar, selectedDate.get(Calendar.DAY_OF_MONTH) + " selected", Snackbar.LENGTH_LONG).show();
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

    public void goToGoals(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        Fragment fragment =  new Goals();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
//        getSupportFragmentManager().executePendingTransactions();


        Toast.makeText(getApplicationContext(),"edit goal",Toast.LENGTH_SHORT).show();
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
        switch(newMonth.get(Calendar.MONTH)) {
            case Calendar.AUGUST:
                arr[0] = new HashMap<>(); //This is the map linking a date to its description
                arr[0].put(3, "unavailable");
                arr[0].put(6, "holiday");
                arr[0].put(21, "unavailable");
                arr[0].put(24, "holiday");
                arr[1] = null; //Optional: This is the map linking a date to its tag.
                break;
            case Calendar.JUNE:
                arr[0] = new HashMap<>();
                arr[0].put(5, "unavailable");
                arr[0].put(10, "holiday");
                arr[0].put(19, "holiday");
                break;
        }
        return arr;
    }
}