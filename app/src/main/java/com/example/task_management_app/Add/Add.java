package com.example.task_management_app.Add;

import com.example.task_management_app.MainActivity;
import com.example.task_management_app.models.DBOpenHelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.app.TimePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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

import static android.app.Activity.RESULT_OK;
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
    TextView tv_date;
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

    //priority
    String db_prority;
    Spinner prio_spinner;

    //set notification
    boolean not_on=false;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    int id_not;


    int RECOGNIZER_RESULT;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());

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
        ImageButton btnnot= view.findViewById(R.id.NotifButton);
        ImageButton btnmic= view.findViewById(R.id.id_mic);
        tv_date=view.findViewById(R.id.textView);
        title=view.findViewById(R.id.id_title);
        details=view.findViewById(R.id.id_details);
        details.setMovementMethod(new ScrollingMovementMethod());
        // set priority adapter
        prio_spinner = view.findViewById(R.id.id_priority);
        prio_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list_priority);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        prio_spinner.setAdapter(adapter);
        // set chip category selector
        ChipGroup chipGroup = view.findViewById(R.id.id_category);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    db_category= (String) chip.getChipText();

                 System.out.println(db_category);

            }
        });
        // set on click listener
        close.setOnClickListener(this);
        action.setOnClickListener(this);
        btnday.setOnClickListener(this);
        btnnot.setOnClickListener(this);
        btnmic.setOnClickListener(this);
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
                System.out.println(db_details);
                Random r = new Random();


                if(not_on) {
                    alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    Intent inte = new Intent(getActivity().getApplicationContext(), AlertReceiver.class);
                    inte.putExtra("title", db_title);
                    id_not= r.nextInt(100000);
                    pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),id_not, inte, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (time.before(Calendar.getInstance())) {
                        time.add(Calendar.DATE, 1);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, db_date, pendingIntent);
                        // getContext().sendBroadcast(intent);
                    }

                    if(!not_on){
                        alarmManager.cancel(pendingIntent);
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
                                //String db_date=Year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                                //System.out.println(db_date);
                                //String currentDateString = TimeFormat.getTimeInstance(DateFormat.FULL).format(c.getTime());
                            }
                        }

                        , year, month, day);
                day_picker.show();
                time_picker=  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY,sHour);
                                c.set(Calendar.MINUTE ,sMinute);
                                hour=sHour;
                                minute=sMinute;




                            }
                        }, hour, minute, true);
                time_picker.show();
                db_time=String.format("%02d:%02d", hour, minute);
                String com=day+"/"+(month+1)+"/"+year+" at "+db_time;
                tv_date.setText(com);

                /*Calendar t = Calendar.getInstance();
                t.set(year, month, day, hour, minute);
                db_date=t.getTimeInMillis();
                dateFormatForDisplaying.format(dateClicked);*/
                break;}

            case R.id.NotifButton:
            {
                ImageButton btnnot = v.findViewById(R.id.NotifButton);
                not_on=!not_on;
                if(not_on){
                    btnnot.setImageResource(R.drawable.ic_notifications_active);
                    Toast.makeText(getActivity(), "notification is on", Toast.LENGTH_SHORT).show();
                }else{
                    btnnot.setImageResource(R.drawable.ic_notifications);
                    Toast.makeText(getActivity(), "notification is off", Toast.LENGTH_SHORT).show();
                }

                System.out.println(not_on);
                break;

            }

            case  R.id.id_mic :
            {
                try {
                    Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");
                    startActivityForResult(speechIntent, RECOGNIZER_RESULT);
                }catch (ActivityNotFoundException e){
                    Toast t = Toast.makeText(getContext(), "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();

                }
            }


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RECOGNIZER_RESULT && null != data){
            ArrayList<String> matches=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            System.out.println(matches.get(0).toString());
            title.setText(matches.get(0));
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
     * * Sending Intent to indicate that the add page is closed *
     *
     * * Code by Abderrahim Tantaoui
     */
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent onDismissIntent = new Intent();
        onDismissIntent.setAction("com.example.broadcastDismiss");
        getContext().sendBroadcast(onDismissIntent);
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
        //time is deleted
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, db_title);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, db_details);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PRIORITY, db_prority);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_STATE, "upcoming");

        // Insert the line in the database
        long rowId = db.insert(DBOpenHelper.Constants.MY_TABLE_Note, null, contentValues);
        return rowId;
    }




}

