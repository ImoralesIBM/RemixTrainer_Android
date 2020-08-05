package com.remixtrainer;

import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DatabaseLocal {
    private FirebaseDatabase mDatabase;

    private String mUserId;
    private DatabaseLoadCompleteListener mLoadCompleteListener;
    private Boolean mMuscleGroupsReady, mEquipmentTypesReady, mExercisesReady, mConfigurationReady;

    private DatabaseReference mDatabaseUserRootRef;
    private DatabaseReference mMuscleGroupListRootDefaultRef, mEquipmentTypeListRootDefaultRef, mExerciseTypeListRootDefaultRef;
    private DatabaseReference mConfigurationRootDefaultRef;

    public Map<String, Map<String, Object>> mConfigurationSettings = new ArrayMap<>();

    public Map<Integer, String> mMuscleGroupList = new ArrayMap<>();
    public Map<Integer, FitnessEquipment> mEquipmentTypeList = new ArrayMap<>();
    public Map<Integer, ExerciseSummary> mExerciseTypeList = new ArrayMap<>();


    public DatabaseLocal()
    {
        mDatabase = FirebaseDatabase.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //No lists are ready in the beginning
        mMuscleGroupsReady = false;
        mEquipmentTypesReady = false;
        mExercisesReady = false;
        mConfigurationReady = false;

        //We don't know who will log in yet
        mUserId = "";

        mMuscleGroupListRootDefaultRef = mDatabase.getReference("muscle_groups_default");
        mEquipmentTypeListRootDefaultRef = mDatabase.getReference("fitness_equipment_default");
        mExerciseTypeListRootDefaultRef = mDatabase.getReference("exercise_types_default");

        mConfigurationRootDefaultRef = mDatabase.getReference("configuration_default");
    }

    public void setLoadCompleteListenerAndUid(DatabaseLoadCompleteListener listener, String userId, Boolean isNewUser)
    {
        mLoadCompleteListener = listener;
        mUserId = userId;

        retrieveData(mUserId, isNewUser);
    }

    public void setLoadCompleteListenerAndUid(DatabaseLoadCompleteListener listener, String userId)
    {
        mLoadCompleteListener = listener;
        mUserId = userId;

        retrieveData(mUserId, false);
    }

    public void reloadUserData(DatabaseLoadCompleteListener listener, String userId)
    {
        mLoadCompleteListener = listener;
        mUserId = userId;

        mMuscleGroupsReady = false;
        mEquipmentTypesReady = false;
        mExercisesReady = false;
        mConfigurationReady = false;

        mMuscleGroupList.clear();
        mEquipmentTypeList.clear();
        mExerciseTypeList.clear();
        mConfigurationSettings.clear();

        mDatabaseUserRootRef.removeValue();

        retrieveData(mUserId, true);
    }

    private void retrieveData(String userId, Boolean isNewUser)
    {
        DatabaseReference muscleGroup, equipmentType, exerciseType, configSettings;

        mDatabaseUserRootRef = mDatabase.getReference("user_data").child(userId);
        mDatabaseUserRootRef.keepSynced(true);

        if (isNewUser) {
            muscleGroup = mMuscleGroupListRootDefaultRef;
            equipmentType = mEquipmentTypeListRootDefaultRef;
            exerciseType = mExerciseTypeListRootDefaultRef;
            configSettings = mConfigurationRootDefaultRef;
        }
        else
        {
            muscleGroup = mDatabaseUserRootRef.child("muscle_groups");
            equipmentType = mDatabaseUserRootRef.child("fitness_equipment");
            exerciseType = mDatabaseUserRootRef.child("exercise_types");
            configSettings = mDatabaseUserRootRef.child("configuration");
        }

        muscleGroup.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren())
                {
                    addMuscleGroup(Integer.parseInt(uniqueKeySnapshot.child("id_group").getValue().toString()), uniqueKeySnapshot.child("description").getValue().toString());
                    if (isNewUser)
                    {
                        DatabaseReference newChildRef = mDatabaseUserRootRef.child("muscle_groups").push();
                        newChildRef.setValue(uniqueKeySnapshot.getValue());
                    }
                }

                mMuscleGroupsReady = true;
                if ((mLoadCompleteListener != null) && allListsReady())
                {
                    mLoadCompleteListener.onLoadComplete();
                    mLoadCompleteListener = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        muscleGroup.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mMuscleGroupList.remove(Integer.parseInt(dataSnapshot.child("id_group").getValue().toString()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        equipmentType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren())
                {
                    addEquipmentType(Integer.parseInt(uniqueKeySnapshot.child("id_equipment").getValue().toString()), uniqueKeySnapshot.child("description").getValue().toString(), uniqueKeySnapshot.child("code").getValue().toString());
                    if (isNewUser)
                    {
                        DatabaseReference newChildRef = mDatabaseUserRootRef.child("fitness_equipment").push();
                        newChildRef.setValue(uniqueKeySnapshot.getValue());
                    }
                }

                mEquipmentTypesReady = true;
                if ((mLoadCompleteListener != null) && allListsReady())
                {
                    mLoadCompleteListener.onLoadComplete();
                    mLoadCompleteListener = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        equipmentType.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mEquipmentTypeList.remove(Integer.parseInt(dataSnapshot.child("id_equipment").getValue().toString()));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        exerciseType.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren())
                {
                    addExerciseType(Integer.parseInt(uniqueKeySnapshot.child("id_exercise").getValue().toString()),
                            uniqueKeySnapshot.child("description").getValue().toString(),
                            StreamSupport.stream(uniqueKeySnapshot.child("muscle_groups").getChildren().spliterator(), false).map(i -> Integer.parseInt(i.child("id_group").getValue().toString())).collect(Collectors.toCollection(ArrayList::new)),
                            StreamSupport.stream(uniqueKeySnapshot.child("equipment_types").getChildren().spliterator(), false).collect(Collectors.toMap(i -> Integer.parseInt(i.child("id_equipment").getValue().toString()), i -> i.child("video_link").getValue().toString())));
                    if (isNewUser)
                    {
                        DatabaseReference newChildRef = mDatabaseUserRootRef.child("exercise_types").push();
                        newChildRef.setValue(uniqueKeySnapshot.getValue());
                    }
                }

                mExercisesReady = true;
                if ((mLoadCompleteListener != null) && allListsReady())
                {
                    mLoadCompleteListener.onLoadComplete();
                    mLoadCompleteListener = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        exerciseType.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int currentExerciseId = Integer.parseInt(dataSnapshot.child("id_exercise").getValue().toString());

                mExerciseTypeList.remove(currentExerciseId);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        configSettings.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren())
                {
                    addConfigSettings(uniqueKeySnapshot.getKey(),
                            (HashMap<String, Object>) uniqueKeySnapshot.getValue());
                    if (isNewUser)
                    {
                        mDatabaseUserRootRef.child("configuration")
                                .child(uniqueKeySnapshot.getKey()).setValue(uniqueKeySnapshot.getValue());
                    }
                }

                mConfigurationReady = true;
                if ((mLoadCompleteListener != null) && allListsReady())
                {
                    mLoadCompleteListener.onLoadComplete();
                    mLoadCompleteListener = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public Boolean allListsReady()
    {
        return (mMuscleGroupsReady && mEquipmentTypesReady && mExercisesReady && mConfigurationReady);
    }

    public void addMuscleGroup(Integer idGroup, String groupDescription)
    {
        mMuscleGroupList.put(idGroup, groupDescription);
    }

    public void addEquipmentType(Integer idEquipment, String equipDescription, String equipCode)
    {
        mEquipmentTypeList.put(idEquipment, new FitnessEquipment(equipDescription, equipCode));
    }

    public void addExerciseType(Integer idExercise, String exDescription, List<Integer> muscleGroupList, Map<Integer, String> equipmentTypeList)
    {
        mExerciseTypeList.put(idExercise, new ExerciseSummary(idExercise, exDescription, muscleGroupList, equipmentTypeList));
    }

    public void addConfigSettings(String configKey, Map<String, Object> configValues)
    {
        mConfigurationSettings.put(configKey, configValues);
    }



    /*** DATABASE CONTROLS ***/

    // Config settings
    public void updateConfigSettingsInDB(String configKey, Map<String, Object> configValues)
    {
        mDatabaseUserRootRef.child("configuration").child(configKey).updateChildren(configValues);
    }

    // Muscle Groups

    public void addMuscleGroupToDB(String description)
    {
        DatabaseReference newChildRef = mDatabaseUserRootRef.child("muscle_groups").push();
        Integer newIdGroup = Collections.max(mMuscleGroupList.keySet())+1;
        Map<String, Object> newGroup = new ArrayMap<>();

        newGroup.put("id_group", newIdGroup);
        newGroup.put("description", description);

        newChildRef.updateChildren(newGroup);
    }

    public void updateMuscleGroupInDB(Integer idGroup, String description)
    {
        mDatabaseUserRootRef.child("muscle_groups").orderByChild("id_group").equalTo(idGroup).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void deleteMuscleGroupFromDB(Integer idGroup)
    {
        mDatabaseUserRootRef.child("muscle_groups").orderByChild("id_group").equalTo(idGroup).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        mDatabaseUserRootRef.child("exercises").orderByChild("muscle_groups").orderByChild("id_group").equalTo(idGroup).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    // Equipment Types

    public void addEquipmentTypeToDB(String code, String description)
    {
        DatabaseReference newChildRef = mDatabaseUserRootRef.child("fitness_equipment").push();

        Integer newIdEquipment = Collections.max(mEquipmentTypeList.keySet())+1;
        Map<String, Object> newType = new ArrayMap<>();

        newType.put("id_equipment", newIdEquipment);
        newType.put("code", code);
        newType.put("description", description);

        newChildRef.updateChildren(newType);
    }

    public void updateEquipmentTypeInDB(Integer idEquipment, String code, String description)
    {
        mDatabaseUserRootRef.child("fitness_equipment").orderByChild("id_equipment").equalTo(idEquipment).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("code").getRef().setValue(code);
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void deleteEquipmentTypeFromDB(Integer idEquipment)
    {
        mDatabaseUserRootRef.child("fitness_equipment").orderByChild("id_equipment").equalTo(idEquipment).getRef().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        mDatabaseUserRootRef.child("exercises").orderByChild("equipment_types").orderByChild("id_equipment").equalTo(idEquipment).getRef().addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }


    // Exercises

    public int addExercise(String description, List<Integer> muscleGroupList, Map<Integer, String> equipmentTypeList)
    {
        DatabaseReference newChildRef = mDatabaseUserRootRef.child("exercise_types").push();
        Integer newIdExercise = Collections.max(mExerciseTypeList.keySet())+1;

        ArrayMap<String, Object> newExProperties = new ArrayMap<>();

        newExProperties.put("id_exercise", newIdExercise.toString());
        newExProperties.put("description", description);
        newExProperties.put("muscle_groups", muscleGroupList.stream()
                .map(i -> Stream.of(new AbstractMap.SimpleEntry("id_group", i))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(Collectors.toList()));
        newExProperties.put("equipment_types", equipmentTypeList.entrySet().stream()
                .map(i -> Stream.of(new AbstractMap.SimpleEntry("id_equipment", i.getKey()),
                        new AbstractMap.SimpleEntry("video_link", i.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(Collectors.toList()));

        newChildRef.updateChildren(newExProperties);

        return newIdExercise;
    }

    public void updateExercise(Integer idExercise, String description, List<Integer> muscleGroupList, Map<Integer, String> equipmentTypeList)
    {
        mDatabaseUserRootRef.child("exercise_types").orderByChild("id_exercise").equalTo(idExercise).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                            uniqueKeySnapshot.child("muscle_groups").getRef()
                                    .setValue(muscleGroupList.stream()
                                    .map(i -> Stream.of(new AbstractMap.SimpleEntry("id_group", i))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                                    .collect(Collectors.toList()));

                            uniqueKeySnapshot.child("equipment_types").getRef()
                                    .setValue(equipmentTypeList.entrySet().stream()
                                    .map(i -> Stream.of(new AbstractMap.SimpleEntry("id_equipment", i.getKey()),
                                            new AbstractMap.SimpleEntry("video_link", i.getValue()))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                                    .collect(Collectors.toList()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void deleteExerciseType(Integer idExercise)
    {
        mDatabaseUserRootRef.child("exercise_types").orderByChild("id_exercise").equalTo(idExercise).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}