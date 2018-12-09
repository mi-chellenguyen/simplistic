package com.simplistic.simplistic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TaskInfoActivity extends AppCompatActivity {
    Task selectedTask;
    RadioButton lowPriority;
    RadioButton mediumPriority;
    RadioButton highPriority;
    EditText editTextTaskName;
    private FirebaseDatabase database;
    private DatabaseReference taskRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        database = FirebaseDatabase.getInstance();
        taskRef = database.getReference("tasks");

        selectedTask = getIntent().getParcelableExtra("selectedTask");

        lowPriority = findViewById(R.id.radioButtonLowPriority);
        mediumPriority = findViewById(R.id.radioButtonMediumPriority);
        highPriority = findViewById(R.id.radioButtonHighPriority);
        editTextTaskName = findViewById(R.id.editTextTaskName);

        setupActivityDisplay();
    }
    public void onClickButtonEdit(View view) {
        EditText taskName = findViewById(R.id.editTextTaskName);
        taskName.setEnabled(true);
    }

    public void setupActivityDisplay() {
        editTextTaskName.setText(selectedTask.getTaskName());

        switch(selectedTask.getPriority()) {
            case 0:
                highPriority.setChecked(true);
                break;
            case 1:
                mediumPriority.setChecked(true);
                break;
            case 2:
                lowPriority.setChecked(true);
                break;
        }
    }

    public void onClickButtonBack(View view) {
        Intent intent = new Intent(this, com.simplistic.simplistic.MainActivity.class);
        startActivity(intent);
    }

    public void onClickButtonSave(View view) {
        Intent intent = new Intent(this, com.simplistic.simplistic.MainActivity.class);
        String taskNameStr = editTextTaskName.getText().toString();

        if (!selectedTask.getTaskName().equals(taskNameStr) && !taskNameStr.trim().equals(""))
            taskRef.child(selectedTask.getTid()).child("taskName").setValue(taskNameStr);

        if(lowPriority.isChecked())
            taskRef.child(selectedTask.getTid()).child("priority").setValue(2);
        else if (mediumPriority.isChecked())
            taskRef.child(selectedTask.getTid()).child("priority").setValue(1);
        else
            taskRef.child(selectedTask.getTid()).child("priority").setValue(0);

        startActivity(intent);
    }

}

