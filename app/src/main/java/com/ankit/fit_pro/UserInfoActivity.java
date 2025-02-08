package com.ankit.fit_pro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UserInfoActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_WEIGHT = "weight";

    private EditText etName, etAge, etWeight;
    private RadioGroup rgGender;
    private Button btnSave, btnBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        rgGender = findViewById(R.id.rgGender);
        btnSave = findViewById(R.id.btnSave);
        btnBMI = findViewById(R.id.btnCalculateBMI);

        loadUserData();

        btnSave.setOnClickListener(v -> saveUserData());

        // Open BMI Calculator Activity when clicking the BMI button
        btnBMI.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, BMICalculatorActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load Name
        etName.setText(prefs.getString(KEY_NAME, ""));

        // Load Age
        int savedAge = prefs.getInt(KEY_AGE, 0);
        etAge.setText(savedAge > 0 ? String.valueOf(savedAge) : "");

        // Load Weight
        float savedWeight = prefs.getFloat(KEY_WEIGHT, 0);
        etWeight.setText(savedWeight > 0 ? String.valueOf(savedWeight) : "");

        // Load Gender
        String gender = prefs.getString(KEY_GENDER, "");
        if ("Male".equals(gender)) {
            ((RadioButton) findViewById(R.id.rbMale)).setChecked(true);
        } else if ("Female".equals(gender)) {
            ((RadioButton) findViewById(R.id.rbFemale)).setChecked(true);
        }
    }

    private void saveUserData() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        if (name.isEmpty()) {
            showToast("Please enter your name");
            return;
        }
        if (ageStr.isEmpty()) {
            showToast("Please enter your age");
            return;
        }
        if (weightStr.isEmpty()) {
            showToast("Please enter your weight");
            return;
        }
        if (selectedGenderId == -1) {
            showToast("Please select your gender");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            float weight = Float.parseFloat(weightStr);

            if (age <= 0) {
                showToast("Age must be greater than zero");
                return;
            }
            if (weight <= 0) {
                showToast("Weight must be greater than zero");
                return;
            }

            String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();

            // Save data in SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(KEY_NAME, name);
            editor.putInt(KEY_AGE, age);
            editor.putString(KEY_GENDER, gender);
            editor.putFloat(KEY_WEIGHT, weight);
            editor.apply();

            showToast("User information saved successfully!");

            // Return to MainActivity and refresh the name
            Intent intent = new Intent();
            intent.putExtra(KEY_NAME, name);
            setResult(RESULT_OK, intent);
            finish();

        } catch (NumberFormatException e) {
            showToast("Invalid input for age or weight");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
