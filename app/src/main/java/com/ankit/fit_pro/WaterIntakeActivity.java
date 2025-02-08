package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WaterIntakeActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "WaterPrefs";
    private static final String KEY_WATER_CONSUMED = "water_consumed";
    private static final String KEY_WATER_GOAL = "water_goal";

    private TextView tvWaterConsumed;
    private EditText etWaterAmount;
    private Button btnAddWater;
    private TextView tvWaterGoal;
    private EditText etWaterGoalInput; // Renamed to avoid confusion

    private int waterConsumed = 0;
    private int waterGoal = 2000; // Default goal (2 liters)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_intake);

        tvWaterConsumed = findViewById(R.id.tvWaterConsumed);
        etWaterAmount = findViewById(R.id.etWaterAmount);
        btnAddWater = findViewById(R.id.btnAddWater);
        tvWaterGoal = findViewById(R.id.tvWaterGoal);
        etWaterGoalInput = findViewById(R.id.etWaterGoal); // Initialize

        loadWaterData();
        updateUI(); // Update text views

        btnAddWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater();
            }
        });

        etWaterGoalInput.setOnFocusChangeListener(new View.OnFocusChangeListener() { // Use the Input field
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // When focus is lost
                    setWaterGoal();
                }
            }
        });
    }

    private void loadWaterData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        waterConsumed = prefs.getInt(KEY_WATER_CONSUMED, 0);
        waterGoal = prefs.getInt(KEY_WATER_GOAL, 2000); // Default 2000 ml
    }

    private void saveWaterData() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_WATER_CONSUMED, waterConsumed);
        editor.putInt(KEY_WATER_GOAL, waterGoal);
        editor.apply();
    }

    private void addWater() {
        String amountStr = etWaterAmount.getText().toString();
        if (!amountStr.isEmpty()) {
            try {
                int amount = Integer.parseInt(amountStr);
                waterConsumed += amount;
                saveWaterData();
                updateUI();
                etWaterAmount.setText(""); // Clear input
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid water amount input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setWaterGoal() {
        String goalStr = etWaterGoalInput.getText().toString(); // Use the Input field
        if (!goalStr.isEmpty()) {
            try {
                waterGoal = Integer.parseInt(goalStr);
                saveWaterData();
                updateUI();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid water goal input", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void updateUI() {
        tvWaterConsumed.setText(String.format(getString(R.string.water_consumed_placeholder), waterConsumed));
        tvWaterGoal.setText(String.format(getString(R.string.water_goal), waterGoal));
    }
}