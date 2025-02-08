package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WorkoutLoggerActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "WorkoutPrefs";
    private static final String KEY_WORKOUTS = "workouts";

    private EditText etExerciseName, etDuration;
    private Button btnLogWorkout, btnResetWorkData;
    private RecyclerView rvWorkoutList;
    private WorkoutAdapter adapter;

    private ArrayList<WorkoutItem> workoutItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_logger);

        etExerciseName = findViewById(R.id.etExerciseName);
        etDuration = findViewById(R.id.etDuration);
        btnLogWorkout = findViewById(R.id.btnLogWorkout);
        btnResetWorkData = findViewById(R.id.btnResetWorkData);
        rvWorkoutList = findViewById(R.id.rvWorkoutList);

        loadWorkouts();
        adapter = new WorkoutAdapter(workoutItems);
        rvWorkoutList.setAdapter(adapter);
        rvWorkoutList.setLayoutManager(new LinearLayoutManager(this));

        btnLogWorkout.setOnClickListener(v -> logWorkout());
        btnResetWorkData.setOnClickListener(v -> resetWorkoutData()); // Reset button functionality
    }

    private void loadWorkouts() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_WORKOUTS, null);

        if (json != null) {
            Type type = new TypeToken<ArrayList<WorkoutItem>>() {}.getType();
            workoutItems = gson.fromJson(json, type);
            if (workoutItems == null) {
                workoutItems = new ArrayList<>();
            }
        }
    }

    private void saveWorkouts() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutItems);
        editor.putString(KEY_WORKOUTS, json);
        editor.apply();
    }

    private void logWorkout() {
        String exerciseName = etExerciseName.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();

        if (exerciseName.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "Please enter both exercise name and duration", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr);
            if (duration <= 0) {
                Toast.makeText(this, "Duration must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            WorkoutItem newItem = new WorkoutItem(exerciseName, duration);
            workoutItems.add(newItem);
            saveWorkouts();
            adapter.notifyItemInserted(workoutItems.size() - 1);

            etExerciseName.setText("");
            etDuration.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration input", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetWorkoutData() {
        workoutItems.clear(); // Clear the list
        saveWorkouts(); // Save empty data to SharedPreferences
        adapter.notifyDataSetChanged(); // Update UI
        Toast.makeText(this, "Workout data reset!", Toast.LENGTH_SHORT).show();
    }
}
