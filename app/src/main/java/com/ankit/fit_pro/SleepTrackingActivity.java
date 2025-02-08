package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SleepTrackingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SleepPrefs";
    private static final String KEY_TOTAL_SLEEP = "total_sleep";
    private static final String KEY_SLEEP_GOAL = "sleep_goal";

    private TextView tvSleepHours, tvSleepGoal;
    private EditText etSleepHours, etSleepGoal;
    private Button btnLogSleep, btnSetGoal, btnResetSleepData;
    private float totalSleep = 0, sleepGoal = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_tracking);

        // Initialize UI components
        tvSleepHours = findViewById(R.id.tvSleepHours);
        etSleepHours = findViewById(R.id.etSleepHours);
        btnLogSleep = findViewById(R.id.btnLogSleep);
        tvSleepGoal = findViewById(R.id.tvSleepGoal);
        etSleepGoal = findViewById(R.id.etSleepGoal);
        btnSetGoal = findViewById(R.id.btnSetGoal);
        btnResetSleepData = findViewById(R.id.btnResetSleepData); // NEW RESET BUTTON

        // Load previous sleep data
        loadSleepData();
        updateUI();

        // Set button click listeners
        btnLogSleep.setOnClickListener(v -> logSleep());
        btnSetGoal.setOnClickListener(v -> setSleepGoal());
        btnResetSleepData.setOnClickListener(v -> resetSleepData()); // Reset Data Button
    }

    private void loadSleepData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        totalSleep = prefs.getFloat(KEY_TOTAL_SLEEP, 0);
        sleepGoal = prefs.getFloat(KEY_SLEEP_GOAL, 8);
    }

    private void saveSleepData() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putFloat(KEY_TOTAL_SLEEP, totalSleep);
        editor.putFloat(KEY_SLEEP_GOAL, sleepGoal);
        editor.apply();
    }

    private void logSleep() {
        String sleepInput = etSleepHours.getText().toString().trim();

        if (sleepInput.isEmpty()) {
            Toast.makeText(this, "Please enter sleep hours!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float sleepHours = Float.parseFloat(sleepInput);
            if (sleepHours <= 0) {
                Toast.makeText(this, "Enter a valid sleep duration!", Toast.LENGTH_SHORT).show();
                return;
            }

            totalSleep += sleepHours;
            saveSleepData();
            updateUI();
            etSleepHours.setText(""); // Clear input field
            Toast.makeText(this, "Sleep logged successfully!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter a number!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSleepGoal() {
        String goalInput = etSleepGoal.getText().toString().trim();

        if (goalInput.isEmpty()) {
            Toast.makeText(this, "Please enter a sleep goal!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float newGoal = Float.parseFloat(goalInput);
            if (newGoal <= 0) {
                Toast.makeText(this, "Sleep goal must be greater than 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            sleepGoal = newGoal;
            saveSleepData();
            updateUI();
            etSleepGoal.setText(""); // Clear input field
            Toast.makeText(this, "Sleep goal updated!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter a number!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetSleepData() {
        // Reset all sleep data to default values
        totalSleep = 0;
        sleepGoal = 8;

        // Clear SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        // Update UI
        updateUI();

        Toast.makeText(this, "Sleep data has been reset!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        tvSleepHours.setText(String.format("Total Sleep: %.1f hrs", totalSleep));
        tvSleepGoal.setText(String.format("Sleep Goal: %.1f hrs", sleepGoal));
    }
}
