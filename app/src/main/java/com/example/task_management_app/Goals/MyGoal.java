package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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


    Intent i;
    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal);

        i = getIntent();
        goal = (Goal) i.getSerializableExtra("GoalObject");

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
                Log.d("cek", "home selected");
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                goback();

            }
        });

        dbOpenHelper = new DBOpenHelper(this, DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        // when draged bar add 1 to progress
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                openDB();
                updateData(goal);
                //closeDB();
                Toast.makeText(getApplicationContext(), " +1", Toast.LENGTH_SHORT).show();
                Intent onDismissIntent = new Intent();
                onDismissIntent.setAction("com.example.broadcastDismiss.goal");
                getApplicationContext().sendBroadcast(onDismissIntent);

            }
        });

        mygoals_title.setText(goal.getTitle());
        mygoals_description.setText(goal.getDescription());
        mygoals_icon.setImageResource(goal.getIcon());

    }

    private int updateData(Goal goal) {
        ContentValues contentValues = new ContentValues();
        String whereClause = DBOpenHelper.Constants.KEY_COL_ID + " = ?";
        //String whereClause = DBOpenHelper.Constants.KEY_COL_ID +" = "+goal.getId();

        contentValues.put(DBOpenHelper.Constants.KEY_COL_ID, goal.getId());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, goal.getTitle());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, goal.getDescription());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_ICON, goal.getIcon());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSMAX, goal.getMaxProgress());
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PROGRESSCURRENT, goal.getProgressCurrent() + 1);

        String[] whereArgs = {String.valueOf(goal.getId())};
        int i = sqLiteDatabase.update(DBOpenHelper.Constants.MY_TABLE_Goal, contentValues, whereClause, whereArgs);
        //long rowId = sqLiteDatabase.insert(DBOpenHelper.Constants.MY_TABLE_Goal, null, contentValues);
        return i;
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
        Intent myactivity = new Intent(this, MyGoalDetails.class);
        myactivity.putExtra("GoalObject", goal);
        startActivity(myactivity);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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