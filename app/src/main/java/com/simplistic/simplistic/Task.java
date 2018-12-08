package com.simplistic.simplistic;


import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private String taskName;
    private String tid; //taskID
    private int priority; //0 = high, 1 = medium, 2 = low

    public Task() {
        taskName = "";
    }

    public Task(String taskName, String tid) {
        this.taskName = taskName;
        this.tid = tid;
        this.priority = 1;
    }

    public Task(Parcel in) {
        priority = in.readInt();
        taskName = in.readString();
        tid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data
        dest.writeInt(priority);
        dest.writeString(taskName);
        dest.writeString(tid);
    }

    // De-serialize the object
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getTaskName(){
        return taskName;
    }

    public String getTid(){
        return tid;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}