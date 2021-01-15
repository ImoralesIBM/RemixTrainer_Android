package com.remixtrainer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class PreliminaryWorkoutViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Integer>> selectedMuscleGroups = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Integer>> selectedEquipmentTypes = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ArrayList<Integer>>> selectedExercises = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ArrayList<Boolean>>> regenFlags = new MutableLiveData<>();
    private List<Integer> exercisesUsed;

    private final int ExercisesPerGroup = 3;

    public void setSelectedMuscleGroups(ArrayList<Integer> rawSelectionList)
    {
        selectedMuscleGroups.setValue(new ArrayList<>(rawSelectionList));
    }

    public MutableLiveData<ArrayList<Integer>> getSelectedMuscleGroups()
    {
        return selectedMuscleGroups;
    }

    public void setSelectedEquipmentTypes(ArrayList<Integer> rawSelectionList)
    {
        selectedEquipmentTypes.setValue(new ArrayList<>(rawSelectionList));
    }

    public MutableLiveData<ArrayList<Integer>> getSelectedEquipmentTypes()
    {
        return selectedEquipmentTypes;
    }

    public MutableLiveData<ArrayList<Integer>> getExercisesForMuscleGroup(int idGroup)
    {
        MutableLiveData<ArrayList<Integer>> tmpExList = new MutableLiveData<>();
        tmpExList.setValue(selectedExercises.getValue().get(idGroup));
        return tmpExList;
    }

    public MutableLiveData<ArrayList<ArrayList<Integer>>> getAllExercises()
    {
        return selectedExercises;
    }

    public MutableLiveData<ArrayList<ArrayList<Boolean>>> getAllRegenFlags()
    {
        return regenFlags;
    }

    public Boolean getRegenFlag(int idGroupPos, int idExercise)
    {
        return regenFlags.getValue().get(idGroupPos).get(idExercise);
    }

    public void setRegenFlag(int idGroupPos, int idExercise, Boolean isChecked)
    {
        ArrayList<ArrayList<Boolean>> tmpRegenFlagList = regenFlags.getValue();
        ArrayList<Boolean> tmpGroupFlags = tmpRegenFlagList.get(idGroupPos);
        tmpGroupFlags.set(idExercise, isChecked);
        tmpRegenFlagList.set(idGroupPos, tmpGroupFlags);
        regenFlags.setValue(tmpRegenFlagList);
    }

    public void setAllRegenFlags(Boolean isChecked)
    {
        ArrayList<ArrayList<Boolean>> tmpRegenFlagList;
        tmpRegenFlagList = regenFlags.getValue();

        for (int currentGroup = 0; currentGroup < tmpRegenFlagList.size(); currentGroup++)
        {
            tmpRegenFlagList.set(currentGroup, tmpRegenFlagList.get(currentGroup).stream().map(elem -> isChecked).collect(Collectors.toCollection(ArrayList::new)));
        }

        regenFlags.setValue(tmpRegenFlagList);
    }

    public void invertRegenFlags()
    {
        ArrayList<ArrayList<Boolean>> tmpRegenFlagList;
        tmpRegenFlagList = regenFlags.getValue();

        for (int currentGroup = 0; currentGroup < tmpRegenFlagList.size(); currentGroup++)
        {
            tmpRegenFlagList.set(currentGroup, tmpRegenFlagList.get(currentGroup).stream().map(elem -> !elem).collect(Collectors.toCollection(ArrayList::new)));
        }

        regenFlags.setValue(tmpRegenFlagList);
    }

    public void GenerateInitialWorkout()
    {
        ArrayList<ArrayList<Boolean>> tmpRegenFlagList;

        exercisesUsed = new ArrayList<>();
        selectedExercises.setValue(new ArrayList<>());
        tmpRegenFlagList = new ArrayList<>();

        for (int i = 0; i < selectedMuscleGroups.getValue().size(); i++)
        {
            tmpRegenFlagList.add(new ArrayList<>());
            selectedExercises.getValue().add(generateExercises(selectedMuscleGroups.getValue().get(i), ExercisesPerGroup));
            for (int j = 0; j < ExercisesPerGroup; j++) {
                tmpRegenFlagList.get(i).add(true);
            }
        }

        regenFlags.setValue(tmpRegenFlagList);
    }

    public void RemixWorkout()
    {
        int numNewEx;
        ArrayList<ArrayList<Integer>> tmpExercisesMasterList;
        ArrayList<Integer> tmpGroupExercises;
        ArrayList<ArrayList<Boolean>> tmpRegenFlagList;

        tmpExercisesMasterList = selectedExercises.getValue();
        tmpRegenFlagList = regenFlags.getValue();

        // Remove unwanted exercises
        for (int i = 0; i < selectedMuscleGroups.getValue().size(); i++)
        {
            tmpGroupExercises = tmpExercisesMasterList.get(i);
            int currentGroupSize = tmpGroupExercises.size();

            for (int j = currentGroupSize-1; j >= 0; j--)
            {
                if (tmpRegenFlagList.get(i).get(j))
                {
                    tmpRegenFlagList.get(i).remove(j);
                    releaseUsedExercise(tmpGroupExercises.get(j));
                    tmpGroupExercises.remove(j);
                }
            }

            tmpExercisesMasterList.set(i, tmpGroupExercises);
        }

        // Replace them with new exercises
        for (int i = 0; i < selectedMuscleGroups.getValue().size(); i++)
        {
            tmpGroupExercises = tmpExercisesMasterList.get(i);

            // Number of exercises that need to be replaced
            numNewEx = ExercisesPerGroup - tmpGroupExercises.size();

            tmpGroupExercises.addAll(generateExercises(selectedMuscleGroups.getValue().get(i), numNewEx));
            for (int j = 0; j < numNewEx; j++) {
                tmpRegenFlagList.get(i).add(true);
            }

            tmpExercisesMasterList.set(i, tmpGroupExercises);
        }

        selectedExercises.setValue(tmpExercisesMasterList);
        regenFlags.setValue(tmpRegenFlagList);
    }


    private ArrayList<Integer> generateExercises(int muscleGroup, int exPerGroup)
    {
        ArrayList<Integer> exListTmp;

        exListTmp = new ArrayList<Integer>();
        exListTmp.addAll(mDatabase.mExerciseTypeList.values().stream()
                .filter(i -> ((i.getMuscleGroups().contains(muscleGroup)
                        && (!Collections.disjoint(selectedEquipmentTypes.getValue(), i.getEquipmentTypesOnly()))
                        && !exercisesUsed.contains(i.getId()))))
                .map(ExerciseSummary::getId).collect(Collectors.toList()));
        Collections.shuffle(exListTmp);
        if (exListTmp.size() > exPerGroup) {exListTmp.subList(exPerGroup, exListTmp.size()).clear();}

        exercisesUsed.addAll(exListTmp.stream().collect(Collectors.toCollection(ArrayList::new)));

        return exListTmp;
    }

    private void releaseUsedExercise(int exerciseId)
    {
        exercisesUsed.removeIf(i -> (i == exerciseId));
    }
}