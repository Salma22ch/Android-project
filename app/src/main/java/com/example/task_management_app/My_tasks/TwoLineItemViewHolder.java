package com.example.task_management_app.My_tasks;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.task_management_app.R;

/** A simple two line list item. */
public class TwoLineItemViewHolder extends SingleLineItemViewHolder {

    public final TextView secondary;

    public TwoLineItemViewHolder(@NonNull View view) {
        super(view);
        this.secondary = itemView.findViewById(R.id.note_description);
    }

    @NonNull
    public static TwoLineItemViewHolder create(@NonNull ViewGroup parent) {
        return new TwoLineItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.material_list_item_two_line, parent, false));
    }
}
