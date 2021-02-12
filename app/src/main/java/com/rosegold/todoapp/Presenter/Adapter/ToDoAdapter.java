package com.rosegold.todoapp.Presenter.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rosegold.todoapp.View.AddTask;
import com.rosegold.todoapp.MainActivity;
import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.R;
import com.rosegold.todoapp.Model.DataBaseHelper;

import java.util.List;

/**
 * To DO List Adapter
 */
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> tasksList;
    private final MainActivity activity;
    private final DataBaseHelper taskDB;
    private final TextView nudge;

    /**
     * Constructor Method
     * @param taskDB SQLite database
     * @param activity main activity to be displayed on
     */
    public ToDoAdapter(DataBaseHelper taskDB, MainActivity activity, TextView nudge){
        this.activity = activity;
        this.taskDB = taskDB;
        this.nudge = nudge;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel item = tasksList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        fillCheckbox(toBoolean(item.getStatus()), holder, item);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> fillCheckbox(isChecked, holder, item));
    }

    /**
     * Converts input checkbox integer value to boolean
     * @param num 0 if false, 1 if true.
     * @return Returns false if input num is equal to 0
     */
    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<ToDoModel> list){
        this.tasksList = list;
        notifyDataSetChanged();
        empty(list);
    }

    /**
     * Empty tasks placeholder on screen
     * If there are no tasks, a empty list placeholder is on screen
     * @param list database list
     */
    private void empty(List<ToDoModel> list){
        if (list.isEmpty()){
            nudge.setVisibility(View.VISIBLE);
        }
        else {
            nudge.setVisibility(View.GONE);
        }
    }

    /**
     * Removes a task from the database
     * @param position task position in database
     */
    public void deleteTask(int position){
        ToDoModel item = tasksList.get(position);
        taskDB.deleteTask(item.getId());
        tasksList.remove(position);
        Toast.makeText(activity.getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT).show();
        notifyItemRemoved(position);
        notifyDataSetChanged();
        empty(tasksList);
    }

    /**
     * Changes task item's description in DB
     * @param position task position
     */
    public void editItem(int position){
        ToDoModel item = tasksList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddTask task = new AddTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
        notifyDataSetChanged();

    }

    /**
     * colours in checkbox if status is checked
     * @param isChecked task checked status
     * @param holder placeholder for checkbox in list item
     * @param item task object
     */
    private void fillCheckbox(boolean isChecked, MyViewHolder holder, ToDoModel item){
        if (isChecked){
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            taskDB.updateStatus(item.getId() , 1);
        }else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            taskDB.updateStatus(item.getId(), 0);
        }
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}