package com.example.task_management_app.Add;


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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog;
import com.example.task_management_app.R;

import javax.security.auth.callback.Callback;

public class Add extends  DialogFragment implements View.OnClickListener {

    private Callback callback;
    //date picker
    DatePickerDialog day_picker;
    EditText text_Day;
    String db_date;
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
        close.setOnClickListener(this);
        action.setOnClickListener(this);
        btnday.setOnClickListener(this);

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
            { final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR );
                final int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                day_picker=new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, month);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                                System.out.println(currentDateString);
                            }
                        }

                        , year, month, day);
                day_picker.show();
                break;}


        }

    }

    public interface Callback {

        void onActionClick(String name);

    }
}

