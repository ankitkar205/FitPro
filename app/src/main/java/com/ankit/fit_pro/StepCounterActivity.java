package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvSteps;
    private Button btnResetSteps;
    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int currentSteps = 0; // Initialize to 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        tvSteps = findViewById(R.id.tvSteps);
        btnResetSteps = findViewById(R.id.btnResetSteps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        } else {
            Toast.makeText(this, "Step counter sensor not available on this device", Toast.LENGTH_SHORT).show();
            btnResetSteps.setEnabled(false); // Disable reset button if no sensor
        }

        btnResetSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSteps();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (stepSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            currentSteps = (int) event.values[0]; // Use TYPE_STEP_COUNTER
            tvSteps.setText(String.valueOf(currentSteps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    private void resetSteps() {
        currentSteps = 0;
        tvSteps.setText(String.valueOf(currentSteps));

        // Optional: Reset step count in persistent storage (SharedPreferences, etc.)
        // For example:
        // SharedPreferences prefs = getSharedPreferences("StepPrefs", MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.putInt("step_count", 0);
        // editor.apply();
    }
}