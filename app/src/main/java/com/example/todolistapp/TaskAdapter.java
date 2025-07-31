package com.example.todolistapp;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    Context context;
    ArrayList<Task> tasks;
    TaskChangeListener listener;

    public interface TaskChangeListener {
        void onTaskChanged();
    }

    public TaskAdapter(Context context, ArrayList<Task> tasks, TaskChangeListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView taskTextView;
        Button editButton, deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.taskTextView.setText(task.getText());
        holder.taskCheckBox.setChecked(task.isCompleted());

        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            listener.onTaskChanged(); // Save after checking/unchecking
        });

        holder.deleteButton.setOnClickListener(v -> {
            tasks.remove(position);
            notifyItemRemoved(position);
            listener.onTaskChanged(); // Save after deletion
        });

        holder.editButton.setOnClickListener(v -> {
            EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(task.getText());

            new AlertDialog.Builder(context)
                    .setTitle("Edit Task")
                    .setView(input)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newText = input.getText().toString().trim();
                        if (!newText.isEmpty()) {
                            task.setText(newText);
                            notifyItemChanged(position);
                            listener.onTaskChanged(); // Save after editing
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
