package com.remixtrainer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Workout {
    private final String mName;
    private final String mOwner;
    private final ArrayList<Integer> mMuscleGroupList;
    private final ArrayList<Integer> mEquipmentTypeList;
    private final ArrayList<ArrayList<Integer>> mSelectedExercises;
    private final Integer numReps;
    private final Integer repTimeIndex;
    private final Integer numSets;
    private final Integer restTimeIndex;
    private final Boolean useReps;
    private final Date mTimeCreated;
    private final Date mTimeUpdated;
    private final Date mTimeAccessed;

    public Workout()
    {
        mName = "";
        mOwner = "";
        mMuscleGroupList = new ArrayList<>();
        mEquipmentTypeList = new ArrayList<>();
        mSelectedExercises = new ArrayList<>();
        numReps = -1;
        repTimeIndex = -1;
        numSets = -1;
        restTimeIndex =-1;
        useReps = true;
        mTimeCreated = new Date();
        mTimeUpdated = new Date();
        mTimeAccessed = new Date();
    }

    public Workout(String newName, String ownerUid, List<Integer> muscleGroups,
                   List<Integer> equipmentTypes, List<List<Integer>> selectedExercises,
                   Integer reps, Integer repTime, Integer sets, Integer restTime, Boolean useRepsFlag) {
        mName = newName;
        mOwner = ownerUid;
        mMuscleGroupList = new ArrayList<>(muscleGroups);
        mEquipmentTypeList = new ArrayList<>(equipmentTypes);
        mSelectedExercises = new ArrayList(selectedExercises);
        numReps = reps;
        repTimeIndex = repTime;
        numSets = sets;
        restTimeIndex = restTime;
        useReps = useRepsFlag;

        mTimeCreated = new Date();
        mTimeUpdated = new Date();
        mTimeAccessed = new Date();
    }

    public Workout(String newName, String ownerUid, List<Integer> muscleGroups,
                   List<Integer> equipmentTypes, List<List<Integer>> selectedExercises,
                   Integer reps, Integer repTime, Integer sets, Integer restTime, Boolean useRepsFlag,
                   Long createdTimestamp, Long updateTimestamp, Long accessedTimestamp) {
        mName = newName;
        mOwner = ownerUid;
        mMuscleGroupList = new ArrayList<>(muscleGroups);
        mEquipmentTypeList = new ArrayList<>(equipmentTypes);
        mSelectedExercises = new ArrayList(selectedExercises);
        numReps = reps;
        repTimeIndex = repTime;
        numSets = sets;
        restTimeIndex = restTime;
        useReps = useRepsFlag;

        mTimeCreated = new Date(createdTimestamp);
        mTimeUpdated = new Date(updateTimestamp);
        mTimeAccessed = new Date(accessedTimestamp);
    }

    public String getName() {
        return mName;
    }

    public List<Integer> getMuscleGroups() {
        return mMuscleGroupList;
    }

    public List<Integer> getEquipmentTypes() {
        return mEquipmentTypeList;
    }

    public List<ArrayList<Integer>> getSelectedExercises() {
        return mSelectedExercises;
    }

    public Integer getNumReps() {return numReps;}
    public Integer getRepTimeIndex() {return repTimeIndex;}
    public Integer getNumSets() {return numSets;}
    public Integer getRestTimeIndex() {return restTimeIndex;}
    public Boolean getUseReps() {return useReps;}

    public Date getTimeCreated() {
        return mTimeCreated;
    }

    public Date getTimeUpdated() {
        return mTimeUpdated;
    }

    public Date getTimeAccessed() {
        return mTimeAccessed;
    }
}
