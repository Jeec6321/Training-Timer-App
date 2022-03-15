package com.example.trainingtimer.models;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    public static int EXERCISE_TYPE_SIMPLE = 0;
    public static int EXERCISE_TYPE_COMPUSED = 0;


    private List<Step> steps = new ArrayList<>();
    private int exerciseType;
    private String exerciseName;

    public Exercise(long stepsQuantity, long workSeconds, long restSeconds, int exerciseType) {
        this.exerciseType = exerciseType;

        for( int i = 0; i < stepsQuantity; i++ )
            steps.add( new Step( workSeconds, restSeconds ) );

    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
