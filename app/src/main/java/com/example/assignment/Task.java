package com.example.assignment;

public class Task {

    private String taskName;
    private int pathImage;
    private String description;


    public Task(String taskname, int pathimage, String _description){
        this.taskName = taskname;
        this.pathImage = pathimage;
        this.description = _description;
    }


    public String getDescription() {
        return description;
    }

    public int getPathImage() {
        return pathImage;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPathImage(int pathImage) {
        this.pathImage = pathImage;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
