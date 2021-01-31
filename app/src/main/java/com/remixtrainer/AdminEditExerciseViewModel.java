package com.remixtrainer;

import androidx.lifecycle.ViewModel;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminEditExerciseViewModel extends ViewModel {

    private Integer mIdExercise;
    private final ArrayMap<Integer, Boolean> mMuscleGroupsSelected = new ArrayMap<>();
    private final ArrayMap<Integer, Boolean> mEquipmentTypesSelected = new ArrayMap<>();
    private final ArrayMap<Integer, String> mEquipmentVideoList = new ArrayMap<>();

    public void setExerciseId(Integer idExercise)
    {
        mIdExercise = idExercise;

        mMuscleGroupsSelected.clear();
        mEquipmentTypesSelected.clear();
        mEquipmentVideoList.clear();

        for (Integer muscleGroup:
             mDatabase.mMuscleGroupList.keySet()) {
            mMuscleGroupsSelected.put(muscleGroup, false);
        }

        for (Integer fitnessEquipment:
                mDatabase.mEquipmentTypeList.keySet()) {
            mEquipmentTypesSelected.put(fitnessEquipment, false);
            mEquipmentVideoList.put(fitnessEquipment, "");
        }

        if (idExercise > -1)
        {
            ArrayList<Integer> muscleGroupsTmp = mDatabase.mExerciseTypeList.get(idExercise).getMuscleGroups();
            ArrayMap<Integer, String> equipmentsVideosTmp = mDatabase.mExerciseTypeList.get(idExercise).getEquipmentAndVideos();

            for (Integer i: muscleGroupsTmp)
            {
                mMuscleGroupsSelected.put(i, true);
            }

            for (Integer i: equipmentsVideosTmp.keySet())
            {
                mEquipmentTypesSelected.put(i, true);
                mEquipmentVideoList.put(i, equipmentsVideosTmp.get(i));
            }
        }
    }

    public Integer getExerciseId()
    {
        return mIdExercise;
    }

    public Boolean getMuscleGroupSelection(Integer idGroup)
    {
        return mMuscleGroupsSelected.get(idGroup);
    }

    public void setMuscleGroupSelection(Integer idGroup, Boolean checkedStatus)
    {
        mMuscleGroupsSelected.remove(idGroup);
        mMuscleGroupsSelected.put(idGroup, checkedStatus);
    }

    public Boolean getEquipmentTypeSelection(Integer idEquipment)
    {
        return mEquipmentTypesSelected.get(idEquipment);
    }

    public void setEquipmentTypeSelection(Integer idEquipment, Boolean checkedStatus)
    {
        mEquipmentTypesSelected.remove(idEquipment);
        mEquipmentTypesSelected.put(idEquipment, checkedStatus);
    }

    public String getEquipmentVideo(Integer idEquipment)
    {
        if (!mEquipmentVideoList.containsKey(idEquipment)) {return "";}

        return mEquipmentVideoList.get(idEquipment);
    }

    public void setEquipmentVideo(Integer idEquipment, String videoLink)
    {
        mEquipmentVideoList.put(idEquipment, videoLink);
    }

    public List<Integer> getFinalMuscleGroupSelections()
    {
        return mMuscleGroupsSelected.entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Integer> getFinalEquipmentTypeSelections()
    {
        return mEquipmentTypesSelected.entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
    }

    public Boolean hasGroupAndEquipment()
    {
        return ((getFinalMuscleGroupSelections().size() > 0) && (getFinalEquipmentTypeSelections().size() > 0));
    }

    public Map<Integer, String> getFinalEquipmentVideos()
    {
        return mEquipmentVideoList.entrySet().stream()
                .filter(i -> (mEquipmentTypesSelected.get(i.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
