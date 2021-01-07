package com.example.task_management_app.My_tasks;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_management_app.R;
import com.example.task_management_app.adapters.TaskRecyclerAdapter;
import com.example.task_management_app.models.Note;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class My_tasks extends Fragment{

    private RecyclerView recyclerView;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private ArrayList<Note> notes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mytasks, container, false);

        //creating the recyclerview
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taskRecyclerAdapter = new TaskRecyclerAdapter(notes);
        recyclerView.setAdapter(taskRecyclerAdapter);


        //populate the list with fake tasks
        for(int i = 0; i < 1000; i++){
            Note note = new Note();
            note.setTitle("title #" + i);
            notes.add(note);
        }
        taskRecyclerAdapter.notifyDataSetChanged();

        return view;

    }
}

