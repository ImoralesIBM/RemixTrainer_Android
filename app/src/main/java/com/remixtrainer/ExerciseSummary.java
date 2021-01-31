package com.remixtrainer;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class ExerciseSummary {
    private final int mExerciseId;
    private final String mExerciseDescription;
    private final String mExerciseVideoLink;
    private final ArrayList<Integer> mMuscleGroupList;
    private final ArrayMap<Integer, String> mEquipmentTypeList;

    public ExerciseSummary()
    {
        mExerciseId = 0;
        mExerciseDescription = "";
        mExerciseVideoLink = "";
        mMuscleGroupList = new ArrayList<>();
        mEquipmentTypeList = new ArrayMap<>();
    }

    public ExerciseSummary(int idExercise)
    {
        mExerciseId = idExercise;
        mExerciseDescription = "";
        mExerciseVideoLink = "";
        mMuscleGroupList = new ArrayList<>();
        mEquipmentTypeList = new ArrayMap<>();
    }

    public ExerciseSummary(int idExercise, String exDescription)
    {
        mExerciseId = idExercise;
        mExerciseDescription = exDescription;
        mExerciseVideoLink = "";
        mMuscleGroupList = new ArrayList<>();
        mEquipmentTypeList = new ArrayMap<>();
    }

    public ExerciseSummary(int idExercise, String exDescription, String exVideoLink)
    {
        mExerciseId = idExercise;
        mExerciseDescription = exDescription;
        mExerciseVideoLink = exVideoLink;
        mMuscleGroupList = new ArrayList<>();
        mEquipmentTypeList = new ArrayMap<>();
    }

    public ExerciseSummary(int idExercise, String exDescription, List<Integer> muscleGroupList)
    {
        mExerciseId = idExercise;
        mExerciseDescription = exDescription;
        mExerciseVideoLink = "";
        mMuscleGroupList = new ArrayList<>(muscleGroupList);
        mEquipmentTypeList = new ArrayMap<>();
    }

    public ExerciseSummary(int idExercise, String exDescription, List<Integer> muscleGroupList,
                           Map<Integer, String> equipmentTypeList)
    {
        mExerciseId = idExercise;
        mExerciseDescription = exDescription;
        mExerciseVideoLink = "";
        mMuscleGroupList = new ArrayList<>(muscleGroupList);
        mEquipmentTypeList = new ArrayMap<>();
        mEquipmentTypeList.putAll(equipmentTypeList);
    }

    public void addMuscleGroup(int idGroup)
    {
        mMuscleGroupList.add(idGroup);
    }

    public void removeMuscleGroup(int idGroup)
    {
        mMuscleGroupList.removeIf(i -> (i == idGroup));
    }

    public void removeAllMuscleGroups()
    {
        mMuscleGroupList.clear();
    }

    public void addEquipmentType(int idEquipment, String videoLink)
    {
        mEquipmentTypeList.put(idEquipment, videoLink);
    }

    public void removeEquipmentType(int idEquipment)
    {
        mEquipmentTypeList.remove(idEquipment);
    }

    public void updateEquipmentTypeVideoLink(int idEquipment, String videoLink)
    {
        mEquipmentTypeList.remove(idEquipment);
        mEquipmentTypeList.put(idEquipment, videoLink);
    }

    public void removeAllEquipmentTypes()
    {
        mEquipmentTypeList.clear();
    }


    public int getId()
    {
        return mExerciseId;
    }

    public String getDescription()
    {
        return mExerciseDescription;
    }

    public String getVideoLink()
    {
        return mExerciseVideoLink;
    }

    public ArrayList<Integer> getMuscleGroups()
    {
        return mMuscleGroupList;
    }

    public ArrayList<Integer> getEquipmentTypesOnly()
    {
        return new ArrayList<>(mEquipmentTypeList.keySet());
    }

    public ArrayMap<Integer, String> getEquipmentAndVideos()
    {
        return mEquipmentTypeList;
    }

    public String getEquipmentCodeList()
    {
        return mEquipmentTypeList.keySet().stream().map(i -> mDatabase.mEquipmentTypeList.get(i).code).collect(Collectors.joining(", "));
    }

    public String getEquipmentCodeList(List<Integer> selectedEquipmentIds)
    {
        return mEquipmentTypeList.keySet().stream().filter(selectedEquipmentIds::contains).map(i -> mDatabase.mEquipmentTypeList.get(i).code).collect(Collectors.joining(", "));
    }
}