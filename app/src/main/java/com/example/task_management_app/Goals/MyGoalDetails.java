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

public class MyGoalDetails extends AppCompatActivity implements GestureDetector.OnGestureListener {
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
//                    ((Edit_Goal) dialog_goal).setCallback(new Add.Callback() {
//                        @Override
//                        public void onActionClick(String name) {
//                            Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    dialog_goal.show(getSupportFragmentManager(), "tag");
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
        progressBar.setMax(goal.getMaxProgress());
        progressBar.setProgress(goal.getProgressCurrent());
        mygoalDetailsDescription.setText(goal.getDescription());




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

}