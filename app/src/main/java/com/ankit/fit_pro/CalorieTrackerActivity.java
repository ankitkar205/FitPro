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

import java.util.ArrayList;

public class CalorieTrackerActivity extends AppCompatActivity {

    private EditText etFoodName, etCalories;
    private Button btnAddFood, btnResetCalData;
    private RecyclerView rvFoodList;
    private FoodAdapter adapter;
    private ArrayList<FoodItem> foodItems = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CalorieTrackerPrefs";
    private static final String KEY_TOTAL_CALORIES = "total_calories";

    private int totalCalories = 0; // Track total calorie intake

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        etFoodName = findViewById(R.id.etFoodName);
        etCalories = findViewById(R.id.etCalories);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnResetCalData = findViewById(R.id.btnResetCalData);
        rvFoodList = findViewById(R.id.rvFoodList);

        adapter = new FoodAdapter(foodItems);
        rvFoodList.setAdapter(adapter);
        rvFoodList.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        totalCalories = sharedPreferences.getInt(KEY_TOTAL_CALORIES, 0);

        btnAddFood.setOnClickListener(v -> addFoodItem());

        btnResetCalData.setOnClickListener(v -> resetCalorieData());
    }

    private void addFoodItem() {
        String foodName = etFoodName.getText().toString().trim();
        String caloriesStr = etCalories.getText().toString().trim();

        if (foodName.isEmpty() || caloriesStr.isEmpty()) {
            Toast.makeText(this, "Please enter both food name and calories", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int calories = Integer.parseInt(caloriesStr);
            if (calories <= 0) {
                Toast.makeText(this, "Calories must be greater than zero", Toast.LENGTH_SHORT).show();
                return;
            }

            totalCalories += calories;
            saveCaloriesData();

            FoodItem newItem = new FoodItem(foodName, calories);
            foodItems.add(newItem);
            adapter.notifyItemInserted(foodItems.size() - 1);

            etFoodName.setText("");
            etCalories.setText("");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid calories input", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetCalorieData() {
        totalCalories = 0;
        saveCaloriesData();
        foodItems.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Calorie data reset!", Toast.LENGTH_SHORT).show();
    }

    private void saveCaloriesData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_TOTAL_CALORIES, totalCalories);
        editor.apply();
    }
}
