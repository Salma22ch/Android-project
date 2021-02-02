package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.R;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyGoal extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 100;
    private static final float SWIPE_VOLACITY_THRESHOLD = 100;
    SlideToActView sta;
    private Toolbar toolbar;
    TextView mygoals_title;
    TextView mygoals_description;
    ImageView mygoals_icon;

    private float x1, x2, y1, y2;
    private GestureDetector gestureDetector;

    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;

    MediaPlayer mp ;


    public MyGoal() {
    }


    Intent i;
    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal);

        i = getIntent();
        goal = (Goal) i.getSerializableExtra("GoalObject");

        mp = MediaPlayer.create(this, R.raw.check_mark_sound_effect);
        sta = (SlideToActView) findViewById(R.id.example);
        toolbar = findViewById(R.id.mygoals_toolbar);
        mygoals_title = (TextView) findViewById(R.id.mygoals_title);
        mygoals_description = (TextView) findViewById(R.id.mygoals_description);
        mygoals_icon = (ImageView) findViewById(R.id.mygoals_icon);

        gestureDetector = new GestureDetector(this);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24); // Set the icon

        // Icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goback();

            }
        });

        dbOpenHelper = new DBOpenHelper(this, DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        // when draged bar add 1 to progress
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {

                //adding +1 to progress current
                goal.setProgressCurrent(goal.getProgressCurrent() + 1);
                openDB();

                dbOpenHelper.updateData(goal, sqLiteDatabase);

                Toast.makeText(getApplicationContext(), "+1", Toast.LENGTH_SHORT).show();
                addDateTocompleted();


                Intent onDismissIntent = new Intent();
                onDismissIntent.setAction("com.example.broadcastDismiss.goal");
                getApplicationContext().sendBroadcast(onDismissIntent);

                makeSoundWhenCheked();
                goDetailsAvtivity();

//                Intent intent = new Intent();
//                intent.setAction("com.example.broadcastDismiss.goaldetails");
//                getApplicationContext().sendBroadcast(intent);


            }
        });

        mygoals_title.setText(goal.getTitle());
        mygoals_description.setText(goal.getDescription());
        mygoals_icon.setImageResource(goal.getIcon());

    }


    boolean isTodaychecked = false;

    public void addDateTocompleted() {

        Calendar calendar2 = Calendar.getInstance();
        ArrayList<Long> arrayListOfDays = goal.getArrayListOfDays();
        Date date = java.util.Calendar.getInstance().getTime();
        int today = date.getDay();

        for (Long time : arrayListOfDays) {
            calendar2.setTimeInMillis(time);
            int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
            if (mDay == today) {
                isTodaychecked = true;
            }
        }

        if (isTodaychecked == false) {
            arrayListOfDays.add(date.getTime());
            goal.setArrayListOfDays(arrayListOfDays);
            dbOpenHelper.updateData(goal,sqLiteDatabase);
        }


    }


    public void goback() {
        this.finish();
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

    private void swipeDown() {
    }

    private void swipeUP() {
        goDetailsAvtivity();
    }

    public void goDetailsAvtivity(){
        Intent myactivity = new Intent(this, MyGoalDetails.class);
        myactivity.putExtra("GoalObject", goal);
        startActivity(myactivity);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    public void makeSoundWhenCheked(){
        mp.start();
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
}