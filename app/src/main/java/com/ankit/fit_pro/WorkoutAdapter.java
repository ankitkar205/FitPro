package com.ankit.fit_pro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private ArrayList<WorkoutItem> workoutItems;

    public WorkoutAdapter(ArrayList<WorkoutItem> workoutItems) {
        this.workoutItems = workoutItems;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutItem workoutItem = workoutItems.get(position);
        holder.tvExerciseName.setText(workoutItem.exerciseName);
        holder.tvDuration.setText(String.valueOf(workoutItem.duration));
    }

    @Override
    public int getItemCount() {
        return workoutItems.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public TextView tvExerciseName;
        public TextView tvDuration;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvDuration = itemView.findViewById(R.id.tvDuration);
        }
    }
}