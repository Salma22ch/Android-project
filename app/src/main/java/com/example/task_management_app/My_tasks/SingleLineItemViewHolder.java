package com.example.task_management_app.My_tasks;


import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.task_management_app.R;

/** A simple single line list item. */
public class SingleLineItemViewHolder extends ViewHolder {

    public final TextView text;

    public SingleLineItemViewHolder(@NonNull View view) {
        super(view);
        this.text = itemView.findViewById(R.id.note_title);
    }

    @NonNull
    public static SingleLineItemViewHolder create(@NonNull ViewGroup parent) {
        return new SingleLineItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.material_list_item_single_line, parent, false));
    }
}
