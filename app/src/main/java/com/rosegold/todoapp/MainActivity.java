package com.rosegold.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rosegold.todoapp.Model.DataBaseHelper;
import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.Presenter.Adapter.ToDoAdapter;
import com.rosegold.todoapp.View.AddTask;
import com.rosegold.todoapp.View.RecyclerViewTouchHelper;
import com.rosegold.todoapp.View.onDialogCloseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements onDialogCloseListener {
    private DataBaseHelper taskDB;
    private List<ToDoModel> taskList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView nudge = findViewById(R.id.empty_list_nudge);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
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
        fab.setOnClickListener(v -> AddTask.newInstance().show(getSupportFragmentManager() , AddTask.TAG));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }



    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        taskList = taskDB.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
        adapter.notifyDataSetChanged();
    }
}