package com.example.task_management_app.Add;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;
import android.app.DatePickerDialog;
import com.example.task_management_app.R;
import com.google.android.material.timepicker.TimeFormat;

import android.text.format.DateFormat;
import android.widget.TimePicker;

import javax.security.auth.callback.Callback;

public class Add extends  DialogFragment implements View.OnClickListener {

    private Callback callback;
    //date picker
    DatePickerDialog day_picker;
    EditText text_Day;
    String db_date;

    //time picker
    TimePickerDialog time_picker;
    EditText text_time;
    String db_time;

    //category_picker
    String db_category;
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
                dismiss();
                break;

            case R.id.DayButton:
            {
                ImageButton btnday= v.findViewById(R.id.DayButton);
                btnday.setBackgroundResource(R.drawable.background_blue_light);
                btnday.setColorFilter(Color.argb(255, 255, 255, 255));
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR );
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                day_picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                db_date=year+"/"+monthOfYear+"/"+dayOfMonth;
                                System.out.println(db_date);
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
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
               time_picker=  new TimePickerDialog(getActivity(),
                       new TimePickerDialog.OnTimeSetListener() {
                           @Override
                           public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                               Calendar c = Calendar.getInstance();
                               c.set(Calendar.HOUR_OF_DAY,sHour);
                               c.set(Calendar.MINUTE ,sMinute);
                               db_time=String.format("%02d:%02d", sHour, sMinute);
                               System.out.println(db_time);
                               //String currentDateString = TimeFormat.getTimeInstance(DateFormat.FULL).format(c.getTime());
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
            }

        }

    }

    public interface Callback {

        void onActionClick(String name);

    }
}

