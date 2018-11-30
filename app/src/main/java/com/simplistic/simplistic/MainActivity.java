package com.simplistic.simplistic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Task> tasksList;
    private ArrayAdapter<Task> tasksListAdapter;
    private ListView listViewTasks;
    private FirebaseDatabase database;
    private DatabaseReference taskRef;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        taskRef = database.getReference("tasks");

        tasksList = new ArrayList<Task>();
        tasksListAdapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, tasksList);

        setupEnterKeyListener();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //tasksList.add(dataSnapshot.getValue(Task.class));
                tasksListAdapter.add(dataSnapshot.getValue(Task.class));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        taskRef.addChildEventListener(childEventListener);
        tasksListAdapter = new TaskAdapter(this, tasksList);
        listViewTasks = findViewById(R.id.listViewTasks);
        listViewTasks.setAdapter(tasksListAdapter);

        setupListViewListener();
        setupEnterKeyListener();
    }

    //Helper function to add a task
    private void addTask(View v) {
        EditText editTask = findViewById(R.id.editTextNewTask);
        String taskName = editTask.getText().toString();

        if(!taskName.trim().equals("")) { //try if length > 0
            String key = taskRef.push().getKey();
            Task newTask = new Task(taskName, key);
            taskRef.child(key).setValue(newTask);

            //Reset field
            editTask.setText("");
        }
    }

    //Deletes task when a task is in the ListView is held down
    private void setupListViewListener() {
        listViewTasks.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Task selectedTask = (Task) adapterView.getItemAtPosition(pos);
                        taskRef.child(selectedTask.getTid()).removeValue();
                        tasksList.remove(selectedTask);
                        tasksListAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }

    //Adds task when user hits "+" button
    public void onClickAddTask(View v) {
        addTask(v);
    }

    //Adds task when user hits enter key
    public void setupEnterKeyListener() {
        EditText newTask = findViewById(R.id.editTextNewTask);
        newTask.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addTask(v);
                    return true;
                }
                return false;
            }
        });
    }
}
