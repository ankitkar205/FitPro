package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HeartRateActivity extends AppCompatActivity {

    private TextView tvHeartRate;
    private Button btnStartMonitoring;
    private Button btnStopMonitoring;
    private boolean isMonitoring = false; // Flag to track monitoring state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate); // Make sure you have this layout

        tvHeartRate = findViewById(R.id.tvHeartRate);
        btnStartMonitoring = findViewById(R.id.btnStartMonitoring);
        btnStopMonitoring = findViewById(R.id.btnStopMonitoring);

        btnStartMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMonitoring();
            }
        });

        btnStopMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMonitoring();
            }
        });
    }

    private void startMonitoring() {
        if (!isMonitoring) {
            // 1. Check for sensor availability and permissions (complex part)
            // 2. Initialize and start heart rate sensor (complex part)

            // Placeholder for starting monitoring
            isMonitoring = true;
            btnStartMonitoring.setText(R.string.stop_monitoring); // Change button text
            Toast.makeText(this, "Heart rate monitoring started", Toast.LENGTH_SHORT).show();

            // Example of updating the heart rate (replace with actual sensor data):
            updateHeartRate(75); // Replace 75 with actual heart rate value

        } else {
            stopMonitoring(); // If already monitoring, stop
        }
    }


    private void stopMonitoring() {
        if (isMonitoring) {
            // 1. Stop heart rate sensor (complex part)

            // Placeholder for stopping monitoring
            isMonitoring = false;
            btnStartMonitoring.setText(R.string.start_monitoring); // Change button text back
            Toast.makeText(this, "Heart rate monitoring stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHeartRate(int heartRate) {
        // Update the heart rate TextView on the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvHeartRate.setText(String.format(getString(R.string.heart_rate_reading), heartRate));
            }
        });
    }
}