package com.example.task_management_app.Goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task_management_app.R;
import com.example.task_management_app.models.Goal;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Goals extends BaseAdapter {

    Context context;
    ArrayList<Goal> goal;
    LayoutInflater layoutInflater;


    Adapter_Goals (Context c, ArrayList<Goal> goal) {
        //super(c,R.layout.custom_row_goals,R.id.goals_row_title);
        //super(c,R.layout.custom_row_goals,goal);
        //super(c, R.layout.custom_row_goals, R.id.goals_row_title, title);
        this.context = c;
        this.goal = goal;
        layoutInflater = (LayoutInflater.from(c));

    }

    @Override
    public int getCount() {
        return goal.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = layoutInflater.inflate(R.layout.custom_row_goals, parent, false);
        ImageView images = row.findViewById(R.id.goals_row_image);
        TextView myTitle = row.findViewById(R.id.goals_row_title);
        TextView myDescription = row.findViewById(R.id.goals_row_description);
        ProgressBar progressBar = row.findViewById(R.id.goals_row_progress);
        TextView progressBarText = row.findViewById(R.id.goals_row_progress_text);

        // now set our resources on views
        images.setImageResource(goal.get(position).getIcon());
        myTitle.setText(goal.get(position).getTitle());
        myDescription.setText(goal.get(position).getDescription());
        progressBar.setMax(goal.get(position).getMaxProgress());
        progressBar.setProgress(goal.get(position).getProgressCurrent());
        progressBarText.setText(goal.get(position).getProgressCurrent()+"/"+goal.get(position).getMaxProgress());

        return row;
    }

    public void updateAdapter(ArrayList<Goal> goals) {
        this.goal = goals;
//        if (goal != null){
//            notifyDataSetChanged();
//        }
        notifyDataSetChanged();

    }
}