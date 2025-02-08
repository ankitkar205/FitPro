package com.ankit.fit_pro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private CardView cardSleepTracker;
    private TextView tvWelcome;

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "name";
    private static final int REQUEST_USER_INFO = 1; // Request Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);

        // Load and display user name
        loadUserName();

        // Apply system insets (only if R.id.main exists)
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize CardViews
        setupCardClick(R.id.cardSteps, StepCounterActivity.class);
        setupCardClick(R.id.cardCalories, CalorieTrackerActivity.class);
        setupCardClick(R.id.cardWorkout, WorkoutLoggerActivity.class);
        setupCardClick(R.id.cardWater, WaterIntakeActivity.class);

        // Open UserInfoActivity to update name
        findViewById(R.id.cardUserInfo).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivityForResult(intent, REQUEST_USER_INFO);
        });

        // Sleep Tracker
        cardSleepTracker = findViewById(R.id.cardSleepTracker);
        if (cardSleepTracker != null) {
            cardSleepTracker.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SleepTrackingActivity.class)));
        }
    }

    private void loadUserName() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userName = prefs.getString(KEY_NAME, "User");
        tvWelcome.setText("Welcome, " + userName + "!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserName(); // Refresh the welcome message
    }

    // Handle result from UserInfoActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_USER_INFO && resultCode == RESULT_OK) {
            String updatedName = data.getStringExtra(KEY_NAME);
            if (updatedName != null) {
                tvWelcome.setText("Welcome, " + updatedName + "!");
            }
        }
    }

    // Helper method to initialize CardView clicks
    private void setupCardClick(int cardId, Class<?> activityClass) {
        CardView cardView = findViewById(cardId);
        if (cardView != null) {
            cardView.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, activityClass)));
        }
    }
}
