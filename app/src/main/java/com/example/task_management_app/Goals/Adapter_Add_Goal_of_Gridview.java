package com.example.task_management_app.Goals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.task_management_app.R;

public class Adapter_Add_Goal_of_Gridview extends BaseAdapter {
    Context context;
    int goalIcons[];
    LayoutInflater inflter;
    private final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int IconIdNotExist = 111111111;
    public int selectedImage = IconIdNotExist;


    public Adapter_Add_Goal_of_Gridview(Context applicationContext, int[] goalIcons) {
        this.context = applicationContext;
        this.goalIcons = goalIcons;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return goalIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.gridview_image, null); // inflate the layout
        ImageView icon = (ImageView) convertView.findViewById(R.id.gridView_image); // get the reference of ImageView
        icon.setImageResource(goalIcons[position]);// set logo images
        if (position == selectedImage) {
            convertView.setBackground(icon.getResources().getDrawable(R.drawable.rounder_background_iconselected));
        } else {
            convertView.setAlpha(0.5f);
        }
        return convertView;
    }
}
