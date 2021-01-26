package com.example.task_management_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.R;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnTaskListener onTaskListener;

    public TaskRecyclerAdapter(ArrayList<Note> mNotes, OnTaskListener onTaskListener) {
        this.mNotes = mNotes;
        this.onTaskListener = onTaskListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        return new ViewHolder(view, onTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.title.setText(mNotes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView timestamp, title;
        OnTaskListener onTaskListener;

        public ViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            this.onTaskListener = onTaskListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTaskListener.onTaskClick(getAdapterPosition());
        }
    }

    public void updateAdapter(ArrayList<Note> mNotes) {
        this.mNotes = mNotes;
        notifyDataSetChanged();
    }

    public interface OnTaskListener {
        void onTaskClick(int position);
    }

}
