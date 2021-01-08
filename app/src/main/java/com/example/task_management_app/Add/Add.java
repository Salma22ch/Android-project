package com.example.task_management_app.Add;

import com.example.task_management_app.MainActivity;
import com.example.task_management_app.models.DBOpenHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.app.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.DatePickerDialog;
import com.example.task_management_app.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.timepicker.TimeFormat;

import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import javax.security.auth.callback.Callback;

import static android.content.Context.ALARM_SERVICE;

public class Add extends  DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Callback callback;
    ContentValues contentValues = new ContentValues();
    String[] list_priority = {"None", "Low", "Medium", "High" };
    // Databae section
    private SQLiteDatabase db;
    DBOpenHelper dbOpenHelper;

    //date picker
    DatePickerDialog day_picker;
    EditText text_Day;
    long db_date;
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR );
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    //time picker
    TimePickerDialog time_picker;
    EditText text_time;
    String db_time;
    //Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);
    //category_picker
    String db_category;

    //title value
    EditText title;
    String db_title;
    EditText details;
    String db_details;

    //type
     String db_type="Task";
    //priority
    String db_prority;
    Spinner prio_spinner;

    //set notification
    boolean not_on=false;
    public static Add newInstance() {
        return new Add();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Animation_Design_BottomSheetDialog);

        // Create  database
        dbOpenHelper = new DBOpenHelper(getContext() , DBOpenHelper.Constants.DATABASE_NAME, null,
                DBOpenHelper.Constants.DATABASE_VERSION);
        openDB();
        // rowId = updateRecord(contentValues, rowId);
        //queryTheDatabase();
        //deleteRecord(rowId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close);
        TextView action = view.findViewById(R.id.fullscreen_dialog_action);
        ImageButton btnday = view.findViewById(R.id.DayButton);
        ImageButton btntime = view.findViewById(R.id.TimeButton);
        ImageButton btncateg = view.findViewById(R.id.CategoryButton);
        ImageButton btnnot= view.findViewById(R.id.NotifButton);
        title=view.findViewById(R.id.id_title);
        details=view.findViewById(R.id.id_details);
        details.setMovementMethod(new ScrollingMovementMethod());
        // set priority adapter
        prio_spinner = view.findViewById(R.id.id_priority);
        prio_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list_priority);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        prio_spinner.setAdapter(adapter);
        // set chip type selector
        ChipGroup chipGroup = view.findViewById(R.id.id_type);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    db_type= (String) chip.getChipText();

                 System.out.println(db_type);

            }
        });
        // set on click listener
        close.setOnClickListener(this);
        action.setOnClickListener(this);
        btnday.setOnClickListener(this);
        btntime.setOnClickListener(this);
        btncateg.setOnClickListener(this);
        btnnot.setOnClickListener(this);

        return view;
    }


    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.fullscreen_dialog_close:
                dismiss();
                break;

            case R.id.fullscreen_dialog_action:
                callback.onActionClick("Saved");
                db_title=title.getText().toString();
                db_details=details.getText().toString();
                Calendar time = Calendar.getInstance();
                time.set(year, month, day, hour, minute);
                db_date=time.getTimeInMillis();
                System.out.println(db_date);
                Random r = new Random();


                if(not_on) {
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    Intent inte = new Intent(getActivity().getApplicationContext(), AlertReceiver.class);
                    inte.putExtra("title", db_title);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), r.nextInt(100000), inte, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, db_date, pendingIntent);
                        // getContext().sendBroadcast(intent);
                    }
                }
                long rowId = insertRecord(contentValues);
                dismiss();
                break;

            case R.id.DayButton:
            {
                ImageButton btnday= v.findViewById(R.id.DayButton);
                btnday.setBackgroundResource(R.drawable.background_blue_light);
                btnday.setColorFilter(Color.argb(255, 255, 255, 255));

                day_picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.YEAR, Year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                year=Year;
                                month=monthOfYear;
                                day=dayOfMonth;
                                //db_date=Year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                                //System.out.println(db_date);
                                //String currentDateString = TimeFormat.getTimeInstance(DateFormat.FULL).format(c.getTime());
                            }
                        }

                        , year, month, day);
                day_picker.show();
                break;}
            case R.id.TimeButton:
            {
                ImageButton btntime = v.findViewById(R.id.TimeButton);
                btntime.setBackgroundResource(R.drawable.background_blue_light);
                btntime.setColorFilter(Color.argb(255, 255, 255, 255));

               time_picker=  new TimePickerDialog(getActivity(),
                       new TimePickerDialog.OnTimeSetListener() {
                           @Override
                           public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                               Calendar c = Calendar.getInstance();
                               c.set(Calendar.HOUR_OF_DAY,sHour);
                               c.set(Calendar.MINUTE ,sMinute);
                               hour=sHour;
                               minute=sMinute;
                               db_time=String.format("%02d:%02d", sHour, sMinute);
                               System.out.println(db_time);


                           }
                       }, hour, minute, true);
                time_picker.show();
                break;
            }
            case R.id.CategoryButton:
            {
                ImageButton btncateg = v.findViewById(R.id.CategoryButton);
                btncateg.setBackgroundResource(R.drawable.background_blue_light);
                btncateg.setColorFilter(Color.argb(255, 255, 255, 255));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose a category");
                final String[] list_category = {"Work", "Health", "Home", "Sport" ,"Others"};
                builder.setSingleChoiceItems(list_category, 0, null).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener(){
                    @Override
                public void onClick(DialogInterface dialog, int selectedPosition) {
                        dialog.dismiss();
                        int which = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        switch (which) {
                            case 0:
                                db_category="Work";
                                break;

                            case 1:
                                db_category="Health";
                                break;

                            case 2:
                                db_category="Home";
                                break;

                            case 3:
                                db_category="Sport";
                                break;

                            case 4:
                                db_category="Others";
                                break;

                        }
                        System.out.println(db_category);

                }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            }

            case R.id.NotifButton:
            {
                ImageButton btnnot = v.findViewById(R.id.NotifButton);
                btnnot.setBackgroundResource(R.drawable.background_blue_light);
                btnnot.setColorFilter(Color.argb(255, 255, 255, 255));
                not_on=true;



            }



        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        db_prority=list_priority[position];
        System.out.println(db_prority);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       db_prority="None";
    }

    public interface Callback {
        void onActionClick(String name);
    }

    @Override
    public void onResume() {
        super.onResume();
        openDB();
    }

    @Override
    public void onPause() {
        super.onPause();
        closeDB();
    }

    /**
     * * Open the database* *
     *
     * @throws SQLiteException
     */
    public void openDB() throws SQLiteException {
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    /** *Close Database */
    public void closeDB() {
        db.close();
    }

    /**
     * Insert a record
     *
     * @param contentValues
     *            (an empty contentValues)
     * @return the inserted row id
     */
    private long insertRecord(ContentValues contentValues) {
        // Assign the values for each column.
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DATE, db_date);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_CATEGORY, db_category);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TYPE, db_type);
        //time is deleted
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, db_title);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, db_details);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PRIORITY, db_prority);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_STATE, "upcoming");

        // Insert the line in the database
        long rowId = db.insert(DBOpenHelper.Constants.MY_TABLE, null, contentValues);
        return rowId;
    }




}

