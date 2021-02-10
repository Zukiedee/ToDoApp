package com.rosegold.todoapp.Model;

/**
 * Model class for to do tasks
 */
public class ToDoModel {
    private int ID, Status;
    private String Task;

    /**
     * Empty constructor method
     */
    public ToDoModel (){ }

    /**
     * Constructor method
     * Initializes all class variables
     */
    public ToDoModel(int id, String task, int status){
        this.ID = id;
        this.Task = task;
        this.Status = status;
    }

    /**
     * Sets task ID variable
     * @param ID id from database
     */
    public void setId(int ID) {
        this.ID = ID;
    }

    /**
     * Sets checked status variable
     * @param status checked/done status
     */
    public void setStatus(int status) {
        this.Status = status;
    }

    /**
     * Sets the task  variable
     * @param task task
     */
    public void setTask(String task) {
        this.Task = task;
    }

    /**
     * Returns task ID
     * @return task ID in DB
     */
    public int getId() {
        return ID;
    }

    /**
     * Returns task status
     * @return checked status
     */
    public int getStatus() {
        return Status;
    }

    /**
     * Returns task
     * @return task
     */
    public String getTask() {
        return Task;
    }
}
