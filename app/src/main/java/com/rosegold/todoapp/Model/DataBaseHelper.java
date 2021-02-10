package com.rosegold.todoapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.rosegold.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite Database
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    /**
     * Constructor method
     * @param context activity
     */
    public DataBaseHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Creates SQLite database
     * @param db SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserts task into database
     * @param model task in object form to be inserted
     */
    public void insertTask(ToDoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , model.getTask());
        values.put(COL_3 , 0);
        db.insert(TABLE_NAME , null , values);
    }

    /**
     * Updates task in database
     * @param id task id to be updated
     */
    public void updateTask(int id , String task){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2 , task);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    /**
     * Updates task status in database. Whether task is completed or not.
     * @param id task status id to be updated
     * @param status new task status
     */
    public void updateStatus(int id , int status){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3 , status);
        db.update(TABLE_NAME , values , "ID=?" , new String[]{String.valueOf(id)});
    }

    /**
     * Removes task from database
     * @param id task id to be removed
     */
    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME , "ID=?" , new String[]{String.valueOf(id)});
    }

    /**
     * Retrieves all to do list tasks from database
     * @return list of tasks in model form
     */
    public List<ToDoModel> getAllTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME , null , null , null , null , null , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    /**
     * Retrieves all checked items from database
     * @return list of tasks in model form
     */
    public List<ToDoModel> getCheckedTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        String query = "Select* FROM " + TABLE_NAME + " WHERE " + COL_3 + " = \"" + 1 + "\"";
        db.beginTransaction();
        try {
            cursor = db.rawQuery(query , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    /**
     * Retrieves all checked items from database
     * @return list of tasks in model form
     */
    public List<ToDoModel> getUncheckedTasks(){

        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        String query = "Select* FROM " + TABLE_NAME + " WHERE " + COL_3 + " = \"" + 0 + "\"";
        db.beginTransaction();
        try {
            cursor = db.rawQuery(query , null);
            if (cursor !=null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);

                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
