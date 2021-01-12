package com.example.task_management_app.Goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task_management_app.R;
import com.example.task_management_app.models.Goal;

public class Adapter_Goals extends ArrayAdapter<String> {

    Context context;
    Goal goal;
    String rTitle[];
    String rDescription[];
    int rImgs[];

    Adapter_Goals (Context c, String title[], String description[], int imgs[]) {
        super(c, R.layout.custom_row_goals, R.id.goals_row_title, title);
        this.context = c;
        this.rTitle = title;
        this.rDescription = description;
        this.rImgs = imgs;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.custom_row_goals, parent, false);
        ImageView images = row.findViewById(R.id.goals_row_image);
        TextView myTitle = row.findViewById(R.id.goals_row_title);
        TextView myDescription = row.findViewById(R.id.goals_row_description);

        // now set our resources on views
        images.setImageResource(rImgs[position]);
        myTitle.setText(rTitle[position]);
        myDescription.setText(rDescription[position]);



        return row;
    }
}