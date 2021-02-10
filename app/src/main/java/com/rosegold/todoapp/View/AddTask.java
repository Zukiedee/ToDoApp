package com.rosegold.todoapp.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rosegold.todoapp.Model.ToDoModel;
import com.rosegold.todoapp.R;
import com.rosegold.todoapp.Model.DataBaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Adds new or updates task from bottom of the screen
 */
public class AddTask extends BottomSheetDialogFragment {

    public static final String TAG = "AddTask";

    //widgets
    private EditText newTask;
    private Button saveButton;
    private DataBaseHelper taskDB;

    public static AddTask newInstance(){
        return new AddTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_newtask , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Design_BottomSheetDialog);
        newTask = view.findViewById(R.id.inputText);
        saveButton = view.findViewById(R.id.button_save);

        taskDB = new DataBaseHelper(getActivity());

        boolean updateTask = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            updateTask = true;
            String task = bundle.getString("task");
            newTask.setText(task);

            if (task.length() > 0 ){
                saveButton.setEnabled(false);
            }

        }
        newTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                taskTextListener(text);
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                taskTextListener(text);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        final boolean update = updateTask;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTask.getText().toString();

                if (update){
                    taskDB.updateTask(bundle.getInt("id") , text);
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    taskDB.insertTask(item);
                }
                dismiss();

            }
        });


    }

    /**
     * Sets save button to enabled if the input string is not empty
     * Event Handler
     * @param text input task text
     */
    private void taskTextListener(CharSequence text){
        if (text.toString().equals("")){
            saveButton.setEnabled(false);
        }else{
            saveButton.setEnabled(true);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof onDialogCloseListener){
            ((onDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}