package com.simplistic.simplistic;

public class Task {
    private String taskName;
    private String tid; //taskID

    public Task() {
        taskName = "";
    }

    public Task(String taskName, String tid) {
        this.taskName = taskName;
        this.tid = tid;
    }

    public String getTaskName(){
        return taskName;
    }

    public String getTid(){
        return tid;
    }
}