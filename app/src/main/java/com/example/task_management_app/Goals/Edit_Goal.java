package com.example.task_management_app.Goals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_management_app.R;
import com.example.task_management_app.models.DBOpenHelper;
import com.example.task_management_app.models.Goal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Edit_Goal extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public Edit_Goal(Goal goal) {
        this.goal = goal;
    }

    public static Edit_Goal newInstance(Goal goal) {
        return new Edit_Goal(goal);
    }


    int[] goalIcons = {R.drawable.ic_goalicon_bad_habits, R.drawable.ic_goalicon_fitness, R.drawable.ic_goalicon_lotus, R.drawable.ic_goalicon_read, R.drawable.ic_goalicon_suitcase, R.drawable.ic_goalicon_money, R.drawable.ic_goalicon_programmer, R.drawable.ic_goalicon_book};
    GridView gridView;
    public int gSelectedIcon;
    public String gTitle;
    public String gDescription;
    public int gProgress;
    Goal goal;

    TextInputLayout titleEditText;
    EditText descriptionEditText;
    TextInputLayout progressEditText;

    TextInputEditText titleEditTextInput;
    TextInputEditText progressEditTextInput;


    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Animation_Design_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_goal, container, false);
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close_edit_goal);
        TextView action = view.findViewById(R.id.fullscreen_dialog_action_edit_goal);
        titleEditText = view.findViewById(R.id.goal_edit_edittext_title);
        titleEditTextInput = view.findViewById(R.id.goal_edit_edittext_title_input);
        descriptionEditText = view.findViewById(R.id.goal_edit_edittext_description);
        progressEditText = view.findViewById(R.id.goal_edit_edittext_progress);
        progressEditTextInput = view.findViewById(R.id.goal_edit_edittext_progress_Input);


        gridView = (GridView) view.findViewById(R.id.edit_goal_gridViewOfIcon); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        Adapter_Add_Goal_of_Gridview customAdapter = new Adapter_Add_Goal_of_Gridview(getContext(), goalIcons);
        gridView.setAdapter(customAdapter);

        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        // implement setOnItemClickListener event on GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Adapter_Add_Goal_of_Gridview myAdapter = (Adapter_Add_Goal_of_Gridview) gridView.getAdapter();
                myAdapter.selectedImage = position;
                gSelectedIcon = goalIcons[position];
                myAdapter.notifyDataSetChanged();
            }
        });



        /**
         * icon
         */
        int myicon = goal.getIcon();
        int myposition = indexOf(goalIcons, myicon);
        Adapter_Add_Goal_of_Gridview myAdapter = (Adapter_Add_Goal_of_Gridview) gridView.getAdapter();
        myAdapter.selectedImage = myposition;
        gSelectedIcon = myicon;
        myAdapter.notifyDataSetChanged();
        /**
         * description
         */
        descriptionEditText.setText(goal.getDescription(), TextView.BufferType.EDITABLE);
        /**
         * title
         */
        titleEditTextInput.setText(goal.getTitle(), TextView.BufferType.EDITABLE);
        /**
         * progress
         */
        progressEditTextInput.setText(goal.getMaxProgress().toString(), TextView.BufferType.EDITABLE);


        // set on click listener
        close.setOnClickListener(this);
        action.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.fullscreen_dialog_close_edit_goal:
                dismiss();
                break;

            case R.id.fullscreen_dialog_action_edit_goal:
                if (!validateTitle() | !validateProgress()) {
                    return;
                } else {
                    gTitle = titleEditText.getEditText().getText().toString().trim();
                    gDescription = descriptionEditText.getText().toString();
                    String gProgressMaxText = progressEditText.getEditText().getText().toString().trim();
                    if (gProgressMaxText.isEmpty() == false) {
                        gProgress = Integer.parseInt(gProgressMaxText);
                    }
                    //create object of Goal class
                    Goal newGoal = new Goal(gTitle, gDescription, gSelectedIcon, gProgress, goal.getProgressCurrent());
                    dbOpenHelper = new DBOpenHelper(getContext(), DBOpenHelper.Constants.DATABASE_NAME, null,
                            DBOpenHelper.Constants.DATABASE_VERSION);


                    Toast.makeText(getActivity(), "goal max = " + gProgress, Toast.LENGTH_SHORT).show();
                    deleteGoal(newGoal);
                    //int i = dbOpenHelper.updateData(goal,sqLiteDatabase);
                    //callback.onActionClick("Goal Saved");
                    Intent onDismissIntent = new Intent();
                    onDismissIntent.setAction("com.example.broadcastDismiss.goal");
                    getContext().sendBroadcast(onDismissIntent);
                    dismiss();
                    break;
                }

        }
    }

    private void deleteGoal(Goal goal) {
        openDB();
        dbOpenHelper.deleteGoal(goal, sqLiteDatabase);
        Toast.makeText(getContext(), "goal deleted ", Toast.LENGTH_SHORT).show();
        closeDB();
    }

    public boolean validateTitle() {
        String title = titleEditText.getEditText().getText().toString().trim();
        if (title.isEmpty()) {
            titleEditText.setError("Field can't be Empty");
            return false;
        } else {
            titleEditText.setError(null);
            titleEditText.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateProgress() {
        String progress = progressEditText.getEditText().getText().toString().trim();
        if (progress.isEmpty()) {
            progressEditText.setError("Field can't be Empty");
            return false;
        } else {
            progressEditText.setError(null);
            progressEditText.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openDB() throws SQLiteException {
        try {
            sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            sqLiteDatabase = dbOpenHelper.getReadableDatabase();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return 11111111;
    }

    public void closeDB() {
        sqLiteDatabase.close();
    }

}