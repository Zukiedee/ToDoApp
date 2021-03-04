package com.rosegold.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rosegold.todoapp.Model.DataBaseHelper;
import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.Presenter.Adapter.ToDoAdapter;
import com.rosegold.todoapp.View.RecyclerViewTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper taskDB;
    private List<ToDoModel> taskList;
    private ToDoAdapter adapter;
    private EditText newTask;
    private RecyclerView recyclerView;

    public void openDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_newtask, null);

        builder.setView(view)
                .setTitle("Add task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = newTask.getText().toString();

                        ToDoModel item = new ToDoModel();
                        item.setTask(text);
                        item.setStatus(0);
                        taskDB.insertTask(item);
                        taskList = taskDB.getAllTasks();
                        Collections.reverse(taskList);
                        adapter.setTasks(taskList);

                        setMsg("Task Added");
                    }
                });
        newTask = view.findViewById(R.id.inputText);
        builder.create().show();
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
        fab.setOnClickListener(view -> openDialog());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateTasks() {
        taskList = taskDB.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
        adapter.notifyDataSetChanged();
    }

    public void setMsg(String msg){
        Snackbar.make(recyclerView, msg, Snackbar.LENGTH_SHORT).show();
    }
}
