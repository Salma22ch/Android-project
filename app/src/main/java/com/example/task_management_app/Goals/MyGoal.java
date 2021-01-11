package com.example.task_management_app.Goals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.task_management_app.R;
import com.ncorti.slidetoact.SlideToActView;

public class MyGoal extends AppCompatActivity {
    SlideToActView sta;
    private Toolbar toolbar;
    private float x1,x2,y1,y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goal);

        sta = (SlideToActView) findViewById(R.id.example);
        toolbar = findViewById(R.id.mygoals_toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24); // Set the icon

        // Icon click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cek", "home selected");
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                goback();

            }
        });
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                Toast.makeText(getApplicationContext(),"is finished",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void goback(){
        this.finish();
    }

   







}