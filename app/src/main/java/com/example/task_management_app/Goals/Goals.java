package com.example.task_management_app.Goals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.task_management_app.R;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;

public class Goals extends Fragment {


    Toolbar toolbar;
    Adapter_Goals adapter;
    ListView goals_listView;

    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;

    ArrayList<Goal> lisOfGoals;
    ListView listView;

    private BroadcastReceiver monReceiver;


    // now paste some images in drawable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_goals, container, false);

        //find the view
        toolbar = (Toolbar) view.findViewById(R.id.goals_toolbar);
        goals_listView = (ListView) view.findViewById(R.id.goals_list);
        ImageView imageView = view.findViewById(R.id.goals_icon);

        dbOpenHelper = new DBOpenHelper(getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);

        openDB();
        lisOfGoals = getAllRecord();


        //Log.d("lisOfGoals", lisOfGoals.toString());
        if (!lisOfGoals.isEmpty() ){
            adapter = new Adapter_Goals(this.getContext(),lisOfGoals);
            goals_listView.setAdapter(adapter);
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
        }
        handleRefresh();


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_mygoals);
        //actionBar.setIcon(R.drawable.ic_mygoals);
        actionBar.setHomeAsUpIndicator(R.drawable.mydailygoals);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("My daily Goals");



        goals_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(getActivity(), MyGoal.class);
                newActivity.putExtra("GoalObject", lisOfGoals.get(position));
                startActivity(newActivity);
            }
        });

        return view;
    }

    public ArrayList<Goal> getAllRecord() {

        ArrayList<Goal> listOfGoal = new ArrayList<Goal>();
        Goal goal;
        Cursor res = sqLiteDatabase.rawQuery("select * from "+DBOpenHelper.Constants.MY_TABLE_Goal, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Integer id = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID));
            String title = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TITLE));
            String description = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DESCRIPTION));
            Integer icon = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ICON));
            Integer progressMax = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PROGRESSMAX));
            Integer progressCurrent = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PROGRESSCURRENT));
            goal = new Goal(id, title, description,icon,progressMax,progressCurrent);
            listOfGoal.add(goal);
            res.moveToNext();
        }
        return listOfGoal;
    }

    public void openDB() throws SQLiteException {
        try {
            sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        }
    }

    public void handleRefresh() {
        monReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i)
            {

                if (getAllRecord() != null) {
                    adapter.updateAdapter(getAllRecord());
                }

            }
        };
        getContext().registerReceiver(monReceiver, new IntentFilter("com.example.broadcastDismiss.goal"));
    }


}
