package com.example.task_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new My_tasks()).commit();
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
                    SelectedFragement=new Calendar();
                    break;
                case R.id.Goals:
                    SelectedFragement=new Goals();
                    break;
                case R.id.Completed:
                    SelectedFragement=new Completed();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,SelectedFragement).commit();
            return true;
        }
    };

}
