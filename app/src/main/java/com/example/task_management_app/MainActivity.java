package com.example.task_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.task_management_app.Add.Add;
import com.example.task_management_app.Calendar.MyCalendar;
import com.example.task_management_app.settings.Settings;
import com.example.task_management_app.Goals.Goals;
import com.example.task_management_app.My_tasks.My_tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new My_tasks()).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

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
                case R.id.Completed:
                    SelectedFragement=new Settings();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SelectedFragement).commit();
            return true;
        }
    };

}
