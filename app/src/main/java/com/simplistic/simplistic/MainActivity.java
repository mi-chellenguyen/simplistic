package com.simplistic.simplistic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> tasks;
    private ArrayAdapter<String> tasksAdapter;
    private ListView listViewTasks;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference taskRef = database.getReference("taskList");
    private boolean firstBootUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewTasks = (ListView)findViewById(R.id.listViewTasks);
        tasks = new ArrayList<String>();
        tasksAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        listViewTasks.setAdapter(tasksAdapter);
        addTasksFromDatabase();
        setupListViewListener();
        setupEnterKeyListener();
    }

    private void addTasksFromDatabase() {
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(firstBootUp) {
                    tasksAdapter.addAll((List<String>) dataSnapshot.getValue());
                    firstBootUp = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addTaskToList(View v) {
        EditText newTask = (EditText)findViewById(R.id.editTextNewTask);
        String taskString = newTask.getText().toString();
        if(!taskString.trim().equals("")) {
            tasksAdapter.add(taskString);
            taskRef.setValue(tasks);
            newTask.setText("");
        }
    }

    private void setupListViewListener() {
        listViewTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tasks.remove(i);
                        taskRef.setValue(tasks);
                        tasksAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    public void onClickAddTask(View v) {
        addTaskToList(v);
    }

    public void setupEnterKeyListener() {
        EditText newTask = (EditText)findViewById(R.id.editTextNewTask);
        newTask.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addTaskToList(v);
                    return true;
                }
                return false;
            }
        });
    }
}
