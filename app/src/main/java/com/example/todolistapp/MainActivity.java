package com.example.todolistapp;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import com.example.todolistapp.Task;
import com.example.todolistapp.TaskAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText taskInput;
    Button addButton;
    RecyclerView recyclerView;
    ArrayList<Task> taskList;
    TaskAdapter adapter;

    private void saveTasks() {
        SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString("task_list", json);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("todo_prefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("task_list", null);
        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        taskList = gson.fromJson(json, type);
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.taskInput);
        addButton = findViewById(R.id.addButton);
        recyclerView = findViewById(R.id.taskRecyclerView);

        loadTasks();
        adapter = new TaskAdapter(this, taskList, this::saveTasks);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String taskText = taskInput.getText().toString().trim();
            if (!taskText.isEmpty()) {
                taskList.add(new Task(taskText));
                adapter.notifyItemInserted(taskList.size() - 1);
                taskInput.setText("");
                saveTasks();
            }
        });
    }

}
