package com.remixtrainer;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class FinalWorkoutViewModel extends ViewModel {
    private ArrayList<Integer> selectedMuscleGroups = new ArrayList<>();
    private ArrayList<Integer> selectedEquipmentTypes = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> selectedExercises = new ArrayList<>();
    private String workoutKey = "";

    private final int ExercisesPerGroup = 3;

    public void setSelectedMuscleGroups(ArrayList<Integer> rawSelectionList)
    {
        selectedMuscleGroups = new ArrayList<>(rawSelectionList);
    }

    public ArrayList<Integer> getSelectedMuscleGroups()
    {
        return selectedMuscleGroups;
    }

    public void setSelectedEquipmentTypes(ArrayList<Integer> rawSelectionList)
    {
        selectedEquipmentTypes = new ArrayList<>(rawSelectionList);
    }

    public ArrayList<Integer> getSelectedEquipmentTypes()
    {
        return selectedEquipmentTypes;
    }

    public ArrayList<Integer> getExercisesForMuscleGroup(int idGroup)
    {
        ArrayList<Integer> tmpExList = new ArrayList<>();
        return selectedExercises.get(idGroup);
    }

    public ArrayList<ArrayList<Integer>> getAllExercises()
    {
        return selectedExercises;
    }

    public void setExercises(ArrayList<ArrayList<Integer>> exerciseListNested)
    {
        selectedExercises = new ArrayList<>(exerciseListNested);
    }

    public String getWorkoutKey() {
        return workoutKey;
    }

    public void setWorkoutKey(String newKey) {
        workoutKey = newKey;
    }
}