package com.example.task_management_app.My_tasks;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.Add.AlertReceiver;
import com.example.task_management_app.MainActivity;
import com.example.task_management_app.R;
import com.example.task_management_app.adapters.TaskRecyclerAdapter;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;
import com.example.task_management_app.models.Note;

import com.example.task_management_app.Add.Edit_task;
import android.content.SharedPreferences;

import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

public class My_tasks extends Fragment implements TaskRecyclerAdapter.OnTaskListener {

    // Misc vars
    private BroadcastReceiver monReceiver;

    // DB vars
    private SQLiteDatabase db;
    private DBOpenHelper dbHelper;

    // UI vars
    private RecyclerView recyclerView;
    private TaskRecyclerAdapter taskRecyclerAdapter;

    // onClick vars
    private SharedPreferences shpref;
    private SharedPreferences.Editor myedit;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // onClick necessary stuff
        shpref = getActivity().getApplicationContext().getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
        myedit = shpref.edit();

        // Create or retrieve the database
        dbHelper = new DBOpenHelper(this.getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        // open the database
        open();

        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mytasks, container, false);

        //creating the recyclerview
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // we pass our task array to the adapter, they should be filtered already
        taskRecyclerAdapter = new TaskRecyclerAdapter(getDatabaseTasks(), this);
        recyclerView.setAdapter(taskRecyclerAdapter);
        handleRefresh();

        return view;

    }

    // refreshing the task list
    public void handleRefresh() {
        monReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i)
            {
                taskRecyclerAdapter.updateAdapter(getDatabaseTasks());
            }
        };
        getContext().registerReceiver(monReceiver, new IntentFilter("com.example.broadcastDismiss"));
    }


    // open the database
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    // close the database
    public void close() {
        db.close();
    }


    // fetching tasks from database
    public ArrayList<Note> getDatabaseTasks() {
        ArrayList<Note> notes = new ArrayList<Note>();
        Note note;
        Cursor res = db.rawQuery("select * from Note", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Integer id = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID));
            String title = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TITLE));
            String description = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DESCRIPTION));
            String category = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_CATEGORY));
            long date = res.getLong(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DATE));
            String priority = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PRIORITY));
            String state = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_STATE));
            String type = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TYPE));
            Integer id_not=res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID_NOT));
            note = new Note(id, title, description, category, date, priority, state, type , id_not);
            notes.add(note);

            res.moveToNext();
        }
        return notes;
    }


    // clicking the task opens edit page
    @Override
    public void onTaskClick(int position) {
        myedit.putInt("id_task" , getDatabaseTasks().get(position).getId());
        myedit.apply();
        DialogFragment dialog = Edit_task.newInstance();
        dialog.show(getFragmentManager(), "tag");
    }


    // Setting task swipe action
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you confirm deleting the task?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // code by salma
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                            Intent inte = new Intent(getActivity().getApplicationContext(), AlertReceiver.class);
                            alarmManager.cancel(PendingIntent.getBroadcast(getActivity().getApplicationContext(),getDatabaseTasks().get(viewHolder.getAdapterPosition()).getId_notification(), inte, PendingIntent.FLAG_UPDATE_CURRENT));
                            System.out.println("heyy");
                            // this is where the delete action should be executed
                            dbHelper.deleteTask(getDatabaseTasks().get(viewHolder.getAdapterPosition()), db);
                            taskRecyclerAdapter.updateAdapter(getDatabaseTasks());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskRecyclerAdapter.updateAdapter(getDatabaseTasks());
                            }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };
}

