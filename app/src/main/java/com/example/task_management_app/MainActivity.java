package com.example.task_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.Add.Add;
import com.example.task_management_app.Goals.Add_Goal;
import com.example.task_management_app.Calendar.MyCalendar;
import com.example.task_management_app.settings.Settings;
import com.example.task_management_app.Goals.Goals;
import com.example.task_management_app.My_tasks.My_tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences shpref;
    private FloatingActionButton fab, fabtask, fabgoal;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView add_task, add_goal;
    Boolean isOpen = false;
    FrameLayout fragment_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_container = (FrameLayout) findViewById(R.id.fragment_container);
        shpref=getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
        Boolean dark_mode = shpref.getBoolean("dark_mode",false);
        if(dark_mode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new My_tasks()).commit();

        fab = findViewById(R.id.fab);
        fabtask = findViewById(R.id.fabTask);
        fabgoal = findViewById(R.id.fabGoals);
        add_task = (TextView) findViewById(R.id.task_add);
        add_goal = (TextView) findViewById(R.id.goal_add);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {

                    add_goal.setVisibility(View.INVISIBLE);
                    add_task.setVisibility(View.INVISIBLE);
                    fabtask.startAnimation(fab_close);
                    fabgoal.startAnimation(fab_close);
                    fab.startAnimation(fab_anticlock);
                    fabtask.setClickable(false);
                    fabtask.setClickable(false);
                    fragment_container.setAlpha(1f);
                    isOpen = false;
                } else {
                    add_goal.setVisibility(View.VISIBLE);
                    add_task.setVisibility(View.VISIBLE);
                    fabtask.startAnimation(fab_open);
                    fabgoal.startAnimation(fab_open);
                    fab.startAnimation(fab_clock);
                    fabtask.setClickable(true);
                    fabgoal.setClickable(true);
                    fragment_container.setAlpha(0.2f);
                    isOpen = true;
                }

            }
        });

        fabtask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = Add.newInstance();
                ((Add) dialog).setCallback(new Add.Callback() {
                    @Override
                    public void onActionClick(String name) {
                        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "tag");
            }

        });

        fabgoal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog_goal = Add_Goal.newInstance();
                ((Add_Goal) dialog_goal).setCallback(new Add.Callback() {
                    @Override
                    public void onActionClick(String name) {
                        Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
                    }
                });
                dialog_goal.show(getSupportFragmentManager(), "tag");
            }

        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment SelectedFragement=null;
            switch (item.getItemId())
            {
                case R.id.Tasks:
                    SelectedFragement=new My_tasks();
                    break;
                case R.id.Calendar:
                    SelectedFragement=new MyCalendar();
                    break;
                case R.id.Goals:
                    SelectedFragement=new Goals();
                    break;
                case R.id.setting:
                    SelectedFragement=new Settings();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SelectedFragement).commit();
            getSupportFragmentManager().executePendingTransactions();
            return true;
        }
    };

}
