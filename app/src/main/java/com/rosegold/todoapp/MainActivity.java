package com.rosegold.todoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rosegold.todoapp.Model.DataBaseHelper;
import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.Presenter.Adapter.ToDoAdapter;
import com.rosegold.todoapp.View.AddTask;
import com.rosegold.todoapp.View.RecyclerViewTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper taskDB;
    private List<ToDoModel> taskList;
    private ToDoAdapter adapter;
    private RecyclerView recyclerView;

    public void addTask (){
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.add_newtask, null);
        new AddTask(MainActivity.this, view, taskDB, adapter, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView nudge = findViewById(R.id.empty_list_nudge);
        recyclerView = findViewById(R.id.recyclerview);
        FloatingActionButton fab = findViewById(R.id.fab);
        taskDB = new DataBaseHelper(MainActivity.this);
        taskList = new ArrayList<>();
        adapter = new ToDoAdapter(taskDB, MainActivity.this, nudge);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        taskList = taskDB.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);

        //add new task
        fab.setOnClickListener(view -> addTask());
        //handles task swipe left and right actions
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
