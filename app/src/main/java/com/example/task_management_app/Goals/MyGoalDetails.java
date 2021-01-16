package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.R;
import com.example.task_management_app.models.Goal;

public class MyGoalDetails extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private static final float SWIPE_THRESHOLD = 100;
    private static final float SWIPE_VOLACITY_THRESHOLD = 100;
    GestureDetector gestureDetector;
    private Toolbar toolbar;

    Intent i;
    Goal goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal_details);
        toolbar = findViewById(R.id.mygoals_details_toolbar);
        gestureDetector = new GestureDetector(this);

        i = getIntent();
        goal = (Goal) i.getSerializableExtra("GoalObject");

        TextView mygoalDetailsProgressMax = (TextView) findViewById(R.id.mygoal_details_progressMax);
        TextView mygoalDetailsProgressCurrent = (TextView) findViewById(R.id.mygoal_details_progressCurent);



        toolbar.setTitle(goal.getTitle());
        toolbar.inflateMenu(R.menu.goal_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.deleteGoal)
                {
                    // do something
                    Toast.makeText(getApplicationContext(),"delete goal",Toast.LENGTH_SHORT).show();

                }
                else if(item.getItemId()== R.id.editGoal)
                {
                    // do something
                    Toast.makeText(getApplicationContext(),"edit goal",Toast.LENGTH_SHORT).show();
                }
                else{
                    // do something
                }

                return false;
            }
        });

        mygoalDetailsProgressCurrent.setText( goal.getProgressCurrent().toString());
        mygoalDetailsProgressMax.setText(goal.getMaxProgress().toString());


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

    private void swipeDown() {
        Toast.makeText(this, "swipeDown", Toast.LENGTH_SHORT).show();
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
}