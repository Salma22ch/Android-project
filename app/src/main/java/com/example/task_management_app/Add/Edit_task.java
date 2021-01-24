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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.DatePickerDialog;
import com.example.task_management_app.R;
import com.example.task_management_app.models.Note;
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

public class Edit_task extends  DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Callback callback;
    // Databae section
    ContentValues contentValues = new ContentValues();
    int id=1;
    private SQLiteDatabase db;
    DBOpenHelper dbOpenHelper;
     //retrieve data
    int re_id , re_state_not , re_id_not;
    String re_title, re_description, re_category, re_priority, re_state, re_type;
    long re_date;
    //priority
    String[] list_priority = {"None", "Low", "Medium", "High" };

    //date picker
    DatePickerDialog day_picker;
    EditText text_Day;
    TextView tv_date;
    TextView tv_time;
    long db_date;
    final Calendar time = Calendar.getInstance();
    int year ;
    int month ;
    int day ;
    //time picker
    SharedPreferences shpref;
    Boolean for_mode;
    TimePickerDialog time_picker;
    EditText text_time;
    String db_time;
    int hour = time.get(Calendar.HOUR_OF_DAY);
    int minute = time.get(Calendar.MINUTE);
    //category_picker
    String db_category;

    //title value
    EditText title;
    String db_title;
    EditText details;
    String db_details;

    //priority
    String db_prority="None";
    Spinner prio_spinner;

    //set notification
    int not_delay;
    boolean not_on , rec_not=false;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    int id_not;
    //set warning
    TextView tv_title_warning;

    int RECOGNIZER_RESULT;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());

    public static Edit_task newInstance() {
        return new Edit_task();
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
        db = dbOpenHelper.getWritableDatabase();
        Note note;
        ArrayList<Note> notes = new ArrayList<Note>();
        String query = "SELECT * FROM Note WHERE id = "+id;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            re_id = res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID));
            re_title = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TITLE));
            re_description = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DESCRIPTION));
            re_category = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_CATEGORY));
            re_date = res.getLong(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_DATE));
            re_priority = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_PRIORITY));
            re_state = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_STATE));
            re_type = res.getString(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_TYPE));
            re_state_not=res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_STATE_NOT));
            re_id_not=res.getInt(res.getColumnIndex(DBOpenHelper.Constants.KEY_COL_ID_NOT));
            System.out.println("hello"+re_id_not+"/"+re_state_not);
            note = new Note(re_id, re_title, re_description, re_category, re_date, re_priority, re_state, re_type);
            notes.add(note);

            res.moveToNext();
        }
        shpref=getActivity().getApplicationContext().getSharedPreferences("Myprefs" , Context.MODE_PRIVATE);
        for_mode = shpref.getBoolean("for_mode",false);
        not_delay=shpref.getInt("time_reminder",0);


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
        tv_time=view.findViewById(R.id.textView2);
        tv_title_warning=view.findViewById(R.id.id_warningtitle);
        title=view.findViewById(R.id.id_title);
        title.setText(re_title);
        details=view.findViewById(R.id.id_details);
        details.setText(re_description);
        details.setMovementMethod(new ScrollingMovementMethod());
        // set priority adapter
        prio_spinner = view.findViewById(R.id.id_priority);
        prio_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list_priority);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        prio_spinner.setAdapter(adapter);
        if(re_priority.equals("Low")) {
            prio_spinner.setSelection(1);
        }
        if(re_priority.equals("Medium")) {
            prio_spinner.setSelection(2);
        }
        if(re_priority.equals("High")) {
            prio_spinner.setSelection(3);
        }
        //initialize time field
        time.setTimeInMillis(re_date);
        year=time.get(time.YEAR);
        month=time.get(time.MONTH);
        day=time.get(time.DAY_OF_MONTH);
        hour=time.get(time.HOUR);
        minute=time.get(time.MINUTE);
        String datetext=time.get(time.DAY_OF_MONTH)+"/"+(time.get(time.MONTH)+1)+"/"+time.get(time.YEAR);
        tv_date.setText(datetext +"  at");
        String timetext =String.format("hh:mma", time.get(time.HOUR), time.get(time.MINUTE));;
        if(for_mode)timetext=String.format("%02d:%02d", time.get(time.HOUR), time.get(time.MINUTE));
        tv_time.setText(timetext);
        // set chip category selector && initialize with database
        if(re_category!=null) {
            db_category=re_category;
            if (re_category.equals("Work")) {
                Chip ch_sport = view.findViewById(R.id.chip6);
                ch_sport.setChecked(true);
            }
            if (re_category.equals("Home")) {
                Chip ch_sport = view.findViewById(R.id.chip63);
                ch_sport.setChecked(true);
            }
            if (re_category.equals("Sport")) {
                Chip ch_sport = view.findViewById(R.id.chip62);
                ch_sport.setChecked(true);
            }
            if (re_category.equals("Health")) {
                Chip ch_sport = view.findViewById(R.id.chip61);
                ch_sport.setChecked(true);
            }
        }
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
        //notification initialisation
        if(re_state_not!=0) {
            not_on = true;
            btnnot.setImageResource(R.drawable.ic_notifications_active);
        }
        else
            not_on=false;
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
                if (title.getText().toString().equals("")) {
                    tv_title_warning.setText("* This field cannot be empty");
                }
                db_title = title.getText().toString();
                Random r = new Random();
                alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                Intent inte = new Intent(getActivity().getApplicationContext(), AlertReceiver.class);
                inte.putExtra("title", db_title);
                if(re_id_not==0) id_not = r.nextInt(100000);
                else id_not=re_id_not;
                pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),id_not, inte, PendingIntent.FLAG_UPDATE_CURRENT);
                if (time.before(Calendar.getInstance())) {
                    time.add(Calendar.DATE, 1);
                }
                db_details=details.getText().toString();
                Calendar c=Calendar.getInstance();;
                c.set(year, month, day, hour, minute);
                db_date=c.getTimeInMillis();
                System.out.println("hey"+db_date);
                System.out.println(db_details);
                if(not_on) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()-10*1000 -not_delay*60000, pendingIntent);
                    }
                }
                if(!not_on){
                    alarmManager.cancel(pendingIntent);
                }
                if (!title.getText().toString().equals("")) {
                    callback.onActionClick("Saved");
                    long rowId = updateRecord(1 , contentValues);
                    dismiss();
                }

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
                                time.set(Calendar.YEAR, Year);
                                time.set(Calendar.MONTH, monthOfYear);
                                time.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                year=Year;
                                month=monthOfYear;
                                day=dayOfMonth;
                                String date=dayOfMonth+"/"+(monthOfYear+1)+"/"+Year;
                                tv_date.setText(date +"  at");
                                //String currentDateString = TimeFormat.getTimeInstance(DateFormat.FULL).format(c.getTime());

                            }
                        }

                        , year, month, day);
                day_picker.show();
                time_picker=  new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                time.set(Calendar.HOUR_OF_DAY,sHour);
                                time.set(Calendar.MINUTE ,sMinute);
                                hour=sHour;
                                minute=sMinute;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
                                if(for_mode) db_time=String.format("%02d:%02d", hour, minute);
                                else db_time=dateFormat.format(time.getTime());
                                tv_time.setText(db_time);

                            }
                        }, hour, minute, true);
                time_picker.show();

                break;}

            case R.id.NotifButton:
            {
                ImageButton btnnot = v.findViewById(R.id.NotifButton);
                rec_not=true;
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

            case  R.id.id_mic : {
                int REQUEST_MICROPHONE = 0;
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");
                        startActivityForResult(speechIntent, RECOGNIZER_RESULT);
                    } catch (ActivityNotFoundException e) {
                        Toast t = Toast.makeText(getContext(), "Ops! Your device doesn't support Speech to Text",
                                Toast.LENGTH_SHORT);
                        t.show();

                    }
                }else{
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_MICROPHONE);
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
        db_prority=re_priority;
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
    private long updateRecord(int id ,ContentValues contentValues) {
        // Assign the values for each column.
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DATE, db_date);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_CATEGORY, db_category);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_TITLE, db_title);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_DESCRIPTION, db_details);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_PRIORITY, db_prority);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_STATE, "upcoming");
        contentValues.put(DBOpenHelper.Constants.KEY_COL_STATE_NOT, not_on);
        contentValues.put(DBOpenHelper.Constants.KEY_COL_ID_NOT, id_not);


        // Insert the line in the database
        long rowId = db.update(DBOpenHelper.Constants.MY_TABLE_Note, contentValues, "id=" + id,null);
        return rowId;
    }




}

