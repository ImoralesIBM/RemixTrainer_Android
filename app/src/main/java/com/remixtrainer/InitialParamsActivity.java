package com.remixtrainer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Map;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class InitialParamsActivity extends ToolbarActivityTemplate {

    private Button mNextButton;
    private NumberPicker mNumReps, mSets, mRestTime, mTimeReps;
    private LinearLayout mRepsEnvelope, mRepsTimeEnvelope;
    private SwitchCompat mSwitchRepsTime;

    private String mWorkoutKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_params);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] repValues = new String[getResources().getInteger(R.integer.reps_max) - getResources().getInteger(R.integer.reps_min) + 1];
        String[] timeRepValues = new String[getResources().getInteger(R.integer.time_reps_max) - getResources().getInteger(R.integer.time_reps_min) + 1];
        String[] setValues = new String[getResources().getInteger(R.integer.sets_max) - getResources().getInteger(R.integer.sets_min) + 1];
        String[] restValues = new String[getResources().getInteger(R.integer.rest_max) - getResources().getInteger(R.integer.rest_min) + 1];

        int minTmp, secTmp, totalSecTmp;

        mNumReps = findViewById(R.id.num_reps);
        mSets = findViewById(R.id.num_sets);
        mRestTime = findViewById(R.id.rest_time);

        mTimeReps = findViewById(R.id.time_reps);

        mRepsEnvelope = findViewById(R.id.reps_input);
        mRepsTimeEnvelope = findViewById(R.id.reps_time_input);

        mSwitchRepsTime = findViewById(R.id.switch_reps_time);

        mNextButton = findViewById(R.id.init_params_next_button);

        for (int i = 0; i < repValues.length; i++)
        {
            repValues[i] = String.valueOf((i + getResources().getInteger(R.integer.reps_min)) * getResources().getInteger(R.integer.reps_step));
        }

        for (int i = 0; i < timeRepValues.length; i++)
        {
            totalSecTmp = (i + getResources().getInteger(R.integer.time_reps_min)) * getResources().getInteger(R.integer.time_reps_step);
            secTmp = totalSecTmp % 60;
            minTmp = (totalSecTmp - secTmp) / 60;
            timeRepValues[i] = String.valueOf(100+minTmp).substring(1)+":"+String.valueOf(100+secTmp).substring(1);
        }

        mNumReps.setDisplayedValues(repValues);
        mNumReps.setMinValue(getResources().getInteger(R.integer.reps_min));
        mNumReps.setMaxValue(getResources().getInteger(R.integer.reps_max));
        mNumReps.setValue(getResources().getInteger(R.integer.reps_default));

        mTimeReps.setDisplayedValues(timeRepValues);
        mTimeReps.setMinValue(getResources().getInteger(R.integer.time_reps_min));
        mTimeReps.setMaxValue(getResources().getInteger(R.integer.time_reps_max));
        mTimeReps.setValue(getResources().getInteger(R.integer.time_reps_default));

        for (int i = 0; i < setValues.length; i++)
        {
            setValues[i] = String.valueOf((i + getResources().getInteger(R.integer.sets_min)) * getResources().getInteger(R.integer.sets_step));
        }

        mSets.setDisplayedValues(setValues);
        mSets.setMinValue(getResources().getInteger(R.integer.sets_min));
        mSets.setMaxValue(getResources().getInteger(R.integer.sets_max));
        mSets.setValue(getResources().getInteger(R.integer.sets_default));

        for (int i = 0; i < restValues.length; i++)
        {
            totalSecTmp = (i + getResources().getInteger(R.integer.rest_min)) * getResources().getInteger(R.integer.rest_step);
            secTmp = totalSecTmp % 60;
            minTmp = (totalSecTmp - secTmp) / 60;
            restValues[i] = String.valueOf(100+minTmp).substring(1)+":"+String.valueOf(100+secTmp).substring(1);
        }

        mRestTime.setDisplayedValues(restValues);
        mRestTime.setMinValue(getResources().getInteger(R.integer.rest_min));
        mRestTime.setMaxValue(getResources().getInteger(R.integer.rest_max));
        mRestTime.setValue(getResources().getInteger(R.integer.rest_default));

        mSwitchRepsTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mRepsEnvelope.setVisibility(View.GONE);
                    mRepsTimeEnvelope.setVisibility(View.VISIBLE);
                }
                else {
                    mRepsEnvelope.setVisibility(View.VISIBLE);
                    mRepsTimeEnvelope.setVisibility(View.GONE);
                }
            });

        mNextButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;

                Bundle outgoingOptionValues = new Bundle();
                Intent nextIntent = new Intent(InitialParamsActivity.this, SelectGroupExerciseActivity.class);

                outgoingOptionValues.putInt("numReps", mNumReps.getValue());
                outgoingOptionValues.putString("repTime", mTimeReps.getDisplayedValues()[mTimeReps.getValue() - 1]);
                outgoingOptionValues.putInt("repTimeIndex", mTimeReps.getValue());
                outgoingOptionValues.putInt("numSets", mSets.getValue());
                outgoingOptionValues.putString("restTime", mRestTime.getDisplayedValues()[mRestTime.getValue() - 1]);
                outgoingOptionValues.putInt("restTimeIndex", mRestTime.getValue());
                outgoingOptionValues.putBoolean("useReps", mSwitchRepsTime.isChecked());

                nextIntent.putExtras(outgoingOptionValues);

                startActivity(nextIntent);
            });

        mNextButton.setOnLongClickListener(v -> {
                //Select body weight for the equipment type and all muscle groups for a quick workout
                //TODO: Allow customizing this in Admin console?

                boolean cancel = false;
                View focusView = null;

                ArrayMap<String, Object> defaultSettings = new ArrayMap<>();
                defaultSettings.putAll(mDatabase.mConfigurationSettings.get("quick_workout_settings"));

                ArrayList<Integer> muscleGroupListTmp;


                Bundle optionValues = new Bundle();
                Intent nextIntent = new Intent(InitialParamsActivity.this, PreliminaryWorkout.class);

                optionValues.putInt("numReps", Integer.parseInt(defaultSettings.get("num_reps").toString()));
                optionValues.putString("repTime", mTimeReps.getDisplayedValues()[Integer.parseInt(defaultSettings.get("time_reps").toString()) - 1]);
                optionValues.putInt("numSets", Integer.parseInt(defaultSettings.get("num_sets").toString()));
                optionValues.putString("restTime", mRestTime.getDisplayedValues()[Integer.parseInt(defaultSettings.get("rest_time").toString()) - 1]);
                optionValues.putBoolean("useReps", mSwitchRepsTime.isChecked());

                muscleGroupListTmp = new ArrayList<>(mDatabase.mMuscleGroupList.keySet());
                Collections.shuffle(muscleGroupListTmp);

                optionValues.putIntegerArrayList("equipmentTypes",
                        new ArrayList<>(mDatabase.mEquipmentTypeList.entrySet().stream().filter(i -> (i.getValue().code.equalsIgnoreCase("BW")))
                                .map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new))));
                optionValues.putIntegerArrayList("muscleGroups",
                        new ArrayList<>(muscleGroupListTmp.stream().limit(4).collect(Collectors.toCollection(ArrayList::new))));

                nextIntent.putExtras(optionValues);

                startActivity(nextIntent);

                return true;
            });

            Bundle incomingOptionValues = getIntent().getExtras();
            if (incomingOptionValues.containsKey("workoutKey")) {
                mWorkoutKey = incomingOptionValues.getString("workoutKey");
                Workout currentWorkout = mDatabase.mSavedWorkoutList.get(mWorkoutKey);

                mNumReps.setValue(currentWorkout.getNumReps());
                mTimeReps.setValue(currentWorkout.getRepTimeIndex());
                mSets.setValue(currentWorkout.getNumSets());
                mRestTime.setValue(currentWorkout.getRestTimeIndex());
                mSwitchRepsTime.setChecked(currentWorkout.getUseReps());
                mNextButton.callOnClick();
            } else {
                mWorkoutKey = "";
            }
    }

}
