package com.rosegold.todoapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.rosegold.todoapp.Model.DataBaseHelper;
import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.Presenter.Adapter.ToDoAdapter;
import com.rosegold.todoapp.R;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates and shows pop-up dialog box for user to add or edit a task.
 */
public class AddTask {
    private List<ToDoModel> taskList = new ArrayList<>();
    private EditText newTask;
    private DataBaseHelper taskDB;
    private ToDoAdapter adapter;
    private boolean update;

    /**
     * Empty constuctor
     */
    public AddTask(){}

    /**
     * Class Constructor
     * @param context Activity to display dialog in
     * @param view view in Activity to display dialog
     * @param taskDB database containing task items
     * @param adapter Adapter that controls access to database
     * @param bundle database bundle if task is being updated
     */
    public AddTask(Context context, View view, DataBaseHelper taskDB, ToDoAdapter adapter, Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        newTask = view.findViewById(R.id.inputText);

        this.taskDB = taskDB;
        this.adapter = adapter;

        update = false;
        if (bundle != null){
            update = true;
            newTask.setText(bundle.getString("task"));
        }
        builder.setView(
                view)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                })
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String text = newTask.getText().toString();

                    if (!update){
                        insertTask(text);
                    }
                    else{
                        updateTask(bundle.getInt("id"), text);
                    }
                });
        if (update){ builder.setTitle("Edit Task"); }
        else { builder.setTitle("Add Task"); }
        builder.create();
        builder.show();
    }

    /**
     * Updates task in database
     * @param position position of task in database
     * @param task task to be updated
     */
    private void updateTask(int position, String task){
        taskDB.updateTask(position, task);
        taskList = taskDB.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
    }

    /**
     * Inserts task in database
     * @param task new task to be inserted
     */
    private void insertTask(String task){
        ToDoModel item = new ToDoModel();
        item.setTask(task);
        item.setStatus(0);
        taskDB.insertTask(item);
        taskList = taskDB.getAllTasks();
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
    }
}
