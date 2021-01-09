package com.example.task_management_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.My_tasks.SingleLineItemViewHolder;
import com.example.task_management_app.R;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private ArrayList<Note> mNotes = new ArrayList<>();

    public TaskRecyclerAdapter(ArrayList<Note> mNotes) {
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_item_single_line, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.title.setText(mNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    // Single Line Item ViewHolder from the Material Design Documentation
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView title;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.title = itemView.findViewById(R.id.note_title);
        }

        @NonNull
        public com.example.task_management_app.My_tasks.SingleLineItemViewHolder create(@NonNull ViewGroup parent) {
            return new com.example.task_management_app.My_tasks.SingleLineItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.material_list_item_single_line, parent, false));
        }
    }

}
