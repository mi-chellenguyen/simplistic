package com.simplistic.simplistic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener{
    private ArrayList<Task> tasksList;
    private TaskAdapter tasksListAdapter;
    private ListView listViewTasks;
    private ArrayAdapter<CharSequence> spinnerArrayAdapter;
    private Spinner spinnerViewOptions;
    private FirebaseDatabase database;
    private DatabaseReference taskRef;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        taskRef = database.getReference("tasks");

        tasksList = new ArrayList<>();

        spinnerViewOptions = findViewById(R.id.spinnerViewOptions);
        spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.view_options_array, android.R.layout.simple_spinner_item);
        spinnerViewOptions.setAdapter(spinnerArrayAdapter);
        spinnerViewOptions.setOnItemSelectedListener(this);
        spinnerViewOptions.setSelection(0);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
    private void addTask() {
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

    //Sets up listeners on ListView
    private void setupListViewListener() {
        //Deletes task when a task is in the ListView is held down
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

        //Go to TaskInfoActivity when task item is clicked
        listViewTasks.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        Task selectedTask = (Task) adapterView.getItemAtPosition(pos);
                        Intent intent = new Intent(MainActivity.this, com.simplistic.simplistic.TaskInfoActivity.class);
                        intent.putExtra("selectedTask", selectedTask);
                        startActivity(intent);
                    }
                }
        );
    }

    //Adds task when user hits "+" button
    public void onClickAddTask(View v) {
        addTask();
    }

    //Adds task when user hits enter key
    public void setupEnterKeyListener() {
        EditText newTask = findViewById(R.id.editTextNewTask);
        newTask.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addTask();
                    return true;
                }
                return false;
            }
        });
    }

    class SortHighLow implements Comparator<Task>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Task a, Task b)
        {
            return a.getPriority() - b.getPriority();
        }
    }

    class SortLowHigh implements Comparator<Task>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Task a, Task b)
        {
            return b.getPriority() - a.getPriority();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        if (selectedItem.equals("High-Low")) {
            Toast.makeText(this, "High-Low selected", Toast.LENGTH_LONG).show();
            Collections.sort(tasksList, new SortHighLow());
        }
        else if (selectedItem.equals("Low-High")) {
            Toast.makeText(this, "Low-High selected", Toast.LENGTH_LONG).show();
            Collections.sort(tasksList, new SortLowHigh());
        }
        spinnerArrayAdapter.notifyDataSetChanged();
        tasksListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
