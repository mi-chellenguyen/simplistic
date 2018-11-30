package com.simplistic.simplistic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    private List<Task> tasksList;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        super(context, 0, taskList);
        this.context = context;
        this.tasksList = taskList;
    }

    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        // Associates the list with the XML Layout file "task_view"
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.task_view,parent,false);

        // Individually handles each Task in the taskList
        Task currentTask = tasksList.get(pos);

        // Extracts the name of the Task
        TextView taskName = listItem.findViewById(R.id.textViewTaskName);
        taskName.setText(currentTask.getTaskName());

        return listItem;
    }
}
