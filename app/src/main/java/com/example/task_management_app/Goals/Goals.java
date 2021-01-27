package com.example.task_management_app.Goals;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.Add.Add;
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

    public static Goals newInstance() {
        return new Goals();
    }

//    public void setCallback(Add.Callback callback) {
//        this.callback = callback;
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_goals, container, false);

        //find the view
        toolbar = (Toolbar) view.findViewById(R.id.goals_toolbar);
        goals_listView = (ListView) view.findViewById(R.id.goals_list);
        registerForContextMenu(goals_listView);
        ImageView imageView = view.findViewById(R.id.goals_icon);

        dbOpenHelper = new DBOpenHelper(getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);

        openDB();
        lisOfGoals = dbOpenHelper.getAllRecord(sqLiteDatabase);
/*        if (lisOfGoals.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }*/
        imageView.setVisibility(View.GONE);

        adapter = new Adapter_Goals(this.getContext(), lisOfGoals);
        goals_listView.setAdapter(adapter);
        handleRefresh();


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
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

    /**
     * adding the menu for delete or edit iteams in listView
     **/
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.goals_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.goal_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            /**
             * deleting goal
             */
            case R.id.deleteGoal:
                int goalPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
                deleteGoal(lisOfGoals.get(goalPosition));
                adapter.remove(goalPosition);
                return true;
            /**
             * edit the goal
             */
            case R.id.editGoal:
                DialogFragment dialog_goal = Edit_Goal.newInstance();
                dialog_goal.show(getActivity().getSupportFragmentManager(), "tag");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * function for deleting goal
     *
     * @param goal
     */

    private void deleteGoal(Goal goal) {
        openDB();
        dbOpenHelper.deleteGoal(goal, sqLiteDatabase);
        Toast.makeText(getContext(), "goal deleted", Toast.LENGTH_SHORT).show();
        //closeDB();
    }


    /**
     * open database
     *
     * @throws SQLiteException
     */
    public void openDB() throws SQLiteException {
        try {
            sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        }
    }

    /**
     * closing database
     */

    public void closeDB() {
        sqLiteDatabase.close();
    }

    /**
     * handle refresh when adding or deleting a new goal
     */

    public void handleRefresh() {
        monReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                adapter.updateAdapter(dbOpenHelper.getAllRecord(sqLiteDatabase));
            }
        };
        getContext().registerReceiver(monReceiver, new IntentFilter("com.example.broadcastDismiss.goal"));
    }


}
