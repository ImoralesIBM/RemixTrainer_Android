package com.remixtrainer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.ArrayMap;

public class DatabaseLocal {
    private final FirebaseDatabase mDatabase;

    private String mUserId;
    private DatabaseLoadCompleteListener mLoadCompleteListener;
    private Boolean mMuscleGroupsReady, mEquipmentTypesReady, mExercisesReady, mConfigurationReady;

    private DatabaseReference mDatabaseUserRootRef;
    private final DatabaseReference mMuscleGroupListRootDefaultRef;
    private final DatabaseReference mEquipmentTypeListRootDefaultRef;
    private final DatabaseReference mExerciseTypeListRootDefaultRef;
    private final DatabaseReference mConfigurationRootDefaultRef;

    public Boolean mIsAdmin;

    public Map<String, Map<String, Object>> mConfigurationSettings = new ArrayMap<>();

    public ArrayMap<Integer, String> mMuscleGroupList = new ArrayMap<>();
    public ArrayMap<Integer, FitnessEquipment> mEquipmentTypeList = new ArrayMap<>();
    public Map<Integer, ExerciseSummary> mExerciseTypeList = new ArrayMap<>();
    public Map<String, Workout> mSavedWorkoutList = new ArrayMap<>();


    public DatabaseLocal()
    {
        mDatabase = FirebaseDatabase.getInstance();
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // By default, no user is an admin
        mIsAdmin = false;

        // No lists are ready in the beginning
        mMuscleGroupsReady = false;
        mEquipmentTypesReady = false;
        mExercisesReady = false;
        mConfigurationReady = false;

        // We don't know who will log in yet
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
        mDatabase.getReference("user_data").child(userId)
                .child("is_admin").setValue(mIsAdmin);

        retrieveData(mUserId, true);
    }

    private void retrieveData(String userId, Boolean isNewUser)
    {
        DatabaseReference adminFlag;
        DatabaseReference muscleGroup, equipmentType, exerciseType, configSettings, savedWorkouts;

        mDatabaseUserRootRef = mDatabase.getReference("user_data").child(userId);
        mDatabaseUserRootRef.keepSynced(true);

        adminFlag = mDatabaseUserRootRef.child("is_admin");

        if (isNewUser) {
            adminFlag.setValue(false);
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
        savedWorkouts = mDatabaseUserRootRef.child("workouts");

        adminFlag.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object rawAdminValue = snapshot.getValue();
                if (rawAdminValue != null) {
                    mIsAdmin = Boolean.parseBoolean(rawAdminValue.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        muscleGroup.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
            public void onCancelled(@NonNull DatabaseError error) {

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savedWorkouts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot uniqueKeySnapshot : snapshot.getChildren())
                {
                    addSavedWorkout(uniqueKeySnapshot.getKey(),
                            uniqueKeySnapshot.child("name").getValue().toString(),
                            StreamSupport.stream(uniqueKeySnapshot.child("muscle_groups").getChildren().spliterator(), false)
                                    .map(i -> Integer.parseInt(i.getValue().toString()))
                                    .collect(Collectors.toList()),
                            StreamSupport.stream(uniqueKeySnapshot.child("equipment_types").getChildren().spliterator(), false)
                                    .map(i -> Integer.parseInt(i.getValue().toString()))
                                    .collect(Collectors.toList()),
                            StreamSupport.stream(uniqueKeySnapshot.child("exercises").getChildren().spliterator(), false)
                                    .map(i -> StreamSupport.stream(i.getChildren().spliterator(), false)
                                            .map(j -> Integer.parseInt(j.getValue().toString())).collect(Collectors.toList()))
                                    .collect(Collectors.toList()),
                            Integer.parseInt(uniqueKeySnapshot.child("num_reps").getValue().toString()),
                            Integer.parseInt(uniqueKeySnapshot.child("rep_time_index").getValue().toString()),
                            Integer.parseInt(uniqueKeySnapshot.child("num_sets").getValue().toString()),
                            Integer.parseInt(uniqueKeySnapshot.child("rest_time_index").getValue().toString()),
                            Boolean.parseBoolean(uniqueKeySnapshot.child("use_reps").getValue().toString()),
                            Long.parseLong(uniqueKeySnapshot.child("created").getValue().toString()),
                            Long.parseLong(uniqueKeySnapshot.child("updated").getValue().toString()),
                            Long.parseLong(uniqueKeySnapshot.child("accessed").getValue().toString())
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        savedWorkouts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                mSavedWorkoutList.remove(snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void addSavedWorkout(String workoutKey, String name,
                                List<Integer> muscleGroupList, List<Integer> equipmentTypeList,
                                List<List<Integer>> exerciseList,
                                Integer numReps, Integer repTimeIndex, Integer numSets,
                                Integer restTimeIndex, Boolean useReps,
                                Long created, Long updated, Long accessed)
    {
        mSavedWorkoutList.put(workoutKey,
                new Workout(name, mUserId,
                            muscleGroupList, equipmentTypeList, exerciseList,
                            numReps, repTimeIndex, numSets, restTimeIndex, useReps,
                            created, updated, accessed));
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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void deleteMuscleGroupFromDB(Integer idGroup)
    {
        mDatabaseUserRootRef.child("muscle_groups").orderByChild("id_group").equalTo(idGroup).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        mDatabaseUserRootRef.child("exercises").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot exerciseSnapshot: dataSnapshot.getChildren()) {
                            exerciseSnapshot.getRef().child("muscle_groups")
                                    .orderByChild("id_group").equalTo(idGroup)
                                    .addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot muscleGroupSnapshot) {
                                                    for (DataSnapshot uniqueKeySnapshot: muscleGroupSnapshot.getChildren()) {
                                                        uniqueKeySnapshot.getRef().removeValue();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            }
                                    );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("code").getRef().setValue(code);
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void deleteEquipmentTypeFromDB(Integer idEquipment)
    {
        mDatabaseUserRootRef.child("fitness_equipment").orderByChild("id_equipment").equalTo(idEquipment).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        mDatabaseUserRootRef.child("exercises").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot exerciseSnapshot: dataSnapshot.getChildren()) {
                            exerciseSnapshot.getRef().child("equipment_types")
                                    .orderByChild("id_equipment").equalTo(idEquipment)
                                    .addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot equipmentTypeSnapshot) {
                                                    for (DataSnapshot uniqueKeySnapshot: equipmentTypeSnapshot.getChildren()) {
                                                        uniqueKeySnapshot.getRef().removeValue();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            }
                                    );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


    // Exercises

    public int addExercise(String description, List<Integer> muscleGroupList, Map<Integer, String> equipmentTypeList)
    {
        DatabaseReference newChildRef = mDatabaseUserRootRef.child("exercise_types").push();
        Integer newIdExercise = Collections.max(mExerciseTypeList.keySet())+1;

        ArrayMap<String, Object> newExProperties = new ArrayMap<>();

        newExProperties.put("id_exercise", newIdExercise);
        newExProperties.put("description", description);
        newExProperties.put("muscle_groups", muscleGroupList.stream()
                .map(i -> Stream.of(new AbstractMap.SimpleEntry<>("id_group", i))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .collect(Collectors.toList()));
        newExProperties.put("equipment_types", equipmentTypeList.entrySet().stream()
                .map(i -> Stream.of(new AbstractMap.SimpleEntry<>("id_equipment", i.getKey()),
                        new AbstractMap.SimpleEntry<>("video_link", i.getValue()))
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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.child("description").getRef().setValue(description);
                            uniqueKeySnapshot.child("muscle_groups").getRef()
                                    .setValue(muscleGroupList.stream()
                                    .map(i -> Stream.of(new AbstractMap.SimpleEntry<>("id_group", i))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                                    .collect(Collectors.toList()));

                            uniqueKeySnapshot.child("equipment_types").getRef()
                                    .setValue(equipmentTypeList.entrySet().stream()
                                    .map(i -> Stream.of(new AbstractMap.SimpleEntry<>("id_equipment", i.getKey()),
                                            new AbstractMap.SimpleEntry<>("video_link", i.getValue()))
                                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                                    .collect(Collectors.toList()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public void deleteExerciseType(Integer idExercise)
    {
        mDatabaseUserRootRef.child("exercise_types").orderByChild("id_exercise").equalTo(idExercise).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {
                            uniqueKeySnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    // Saved workouts

    public String saveNewWorkoutToDB(String name,
                       List<Integer> muscleGroupList,
                       List<Integer> equipmentList,
                       List<ArrayList<Integer>> exerciseList,
                       Integer numReps,
                       Integer repTime,
                       Integer numSets,
                       Integer restTime,
                       Boolean useReps) {
        String newWorkoutKey = mDatabaseUserRootRef.child("workouts").push().getKey();

        Map<String, Object> newWorkout = new ArrayMap<>();
        newWorkout.put("name", name);
        newWorkout.put("muscle_groups", muscleGroupList);
        newWorkout.put("equipment_types", equipmentList);
        newWorkout.put("exercises", exerciseList);
        newWorkout.put("num_reps", numReps);
        newWorkout.put("rep_time_index", repTime);
        newWorkout.put("num_sets", numSets);
        newWorkout.put("rest_time_index", restTime);
        newWorkout.put("use_reps", useReps);
        newWorkout.put("created", new Date());
        newWorkout.put("updated", new Date());
        newWorkout.put("accessed", new Date());

        mDatabaseUserRootRef.child("workouts/"+newWorkoutKey).updateChildren(newWorkout);

        return newWorkoutKey;
    }

    public void accessWorkoutInDB(String key) {
        mDatabaseUserRootRef.child("workouts/"+key+"/accessed").setValue(new Date());
    }

    public void updateWorkoutInDB(String key,
                                  String name,
                                  List<Integer> muscleGroupList,
                                  List<Integer> equipmentList,
                                  List<ArrayList<Integer>> exerciseList,
                                  Integer numReps,
                                  Integer repTime,
                                  Integer numSets,
                                  Integer restTime,
                                  Boolean useReps) {
        Map<String, Object> newWorkout = new ArrayMap<>();
        newWorkout.put("name", name);
        newWorkout.put("muscle_groups", muscleGroupList);
        newWorkout.put("equipment_types", equipmentList);
        newWorkout.put("exercises", exerciseList);
        newWorkout.put("num_reps", numReps);
        newWorkout.put("rep_time_index", repTime);
        newWorkout.put("num_sets", numSets);
        newWorkout.put("rest_time_index", restTime);
        newWorkout.put("use_reps", useReps);
        newWorkout.put("created", mSavedWorkoutList.get(key).getTimeCreated());
        newWorkout.put("updated", new Date());
        newWorkout.put("accessed", new Date());

        mDatabaseUserRootRef.child("workouts/"+key).updateChildren(newWorkout);
    }

    public void deleteWorkoutFromDB(String workoutKey)
    {
        mDatabaseUserRootRef.child("workouts").child(workoutKey).removeValue();
    }


}