package com.example.task_management_app.Goals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.task_management_app.R;
import com.example.task_management_app.models.Goal;

public class Goals extends Fragment {


    Toolbar toolbar;
    Adapter_Goals adapter;
    ListView goals_listView;



    ListView listView;
    String mTitle[] = {"Facebook", "Whatsapp", "Twitter", "Instagram", "Youtube"};
    String mDescription[] = {"Facebook Description", "Whatsapp Description", "Twitter Description", "Instagram Description", "Youtube Description"};
    int images[] = {R.drawable.ic_accompli, R.drawable.ic_accompli, R.drawable.ic_accompli, R.drawable.ic_accompli, R.drawable.ic_accompli};
    // so our images and other things are set in array

    // now paste some images in drawable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_goals, container, false);

        //find the view
        toolbar = (Toolbar) view.findViewById(R.id.goals_toolbar);
        goals_listView = (ListView) view.findViewById(R.id.goals_list);

        adapter = new Adapter_Goals(getContext(), mTitle, mDescription, images);
        goals_listView.setAdapter(adapter);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_mygoals);
        //actionBar.setIcon(R.drawable.ic_mygoals);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_format_calendar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("my daily Goals");



        goals_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = new Goal(mTitle[position],mDescription[position]);
                Intent newActivity = new Intent(getActivity(), MyGoal.class);
                newActivity.putExtra("GoalObject",goal);
                startActivity(newActivity);
            }
        });


        return view;
    }
}
