package com.remixtrainer;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class PreliminaryWorkout extends ToolbarActivityTemplate {

    private Button mBackButton, mRemixButton, mNextButton;
    private ImageButton mSelAllButton, mDiscAllButton, mInvertButton;
    private PreliminaryWorkoutViewModel mViewModel;
    private Integer mNumReps, mRepTimeIndex, mNumSets, mRestTimeIndex;
    private Boolean mUseReps;
    private String mRepString;
    private RecyclerView mMuscleGroupList;

    private String mWorkoutKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preliminary_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle inboundOptionValues = getIntent().getExtras();

        mViewModel = ViewModelProviders.of(this).get(PreliminaryWorkoutViewModel.class);
        mViewModel.setSelectedEquipmentTypes(inboundOptionValues.getIntegerArrayList("equipmentTypes"));
        mViewModel.setSelectedMuscleGroups(inboundOptionValues.getIntegerArrayList("muscleGroups"));

        mNumReps = inboundOptionValues.getInt("numReps");
        mRepTimeIndex = inboundOptionValues.getInt("repTimeIndex");
        mNumSets = inboundOptionValues.getInt("numSets");
        mRestTimeIndex = inboundOptionValues.getInt("restTimeIndex");
        mUseReps = inboundOptionValues.getBoolean("useReps");

        if (inboundOptionValues.containsKey("workoutKey")) {
            mWorkoutKey = inboundOptionValues.getString("workoutKey");
        }

        mMuscleGroupList = (RecyclerView) findViewById(R.id.preliminary_muscle_group_list);
        mMuscleGroupList.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(),
                mViewModel.getAllExercises().getValue(),
                mViewModel.getAllRegenFlags().getValue(), mViewModel));
        mViewModel.getAllExercises().observe(this, exList -> {
            mMuscleGroupList.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(), exList, mViewModel.getAllRegenFlags().getValue(), mViewModel));
        });
        mViewModel.getAllRegenFlags().observe(this, flagList -> {
            mMuscleGroupList.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(), mViewModel.getAllExercises().getValue(), flagList, mViewModel));
        });

        if (!inboundOptionValues.getBoolean("useReps"))
        {
            mRepString = inboundOptionValues.getInt("numReps") + " reps";
        }
        else {
            mRepString = inboundOptionValues.getString("repTime");
        }
        mRepString += ("; " + inboundOptionValues.getInt("numSets") * getResources().getInteger(R.integer.sets_step) + " sets");
        mRepString += ("; " + inboundOptionValues.getString("restTime") + " sec rest");

        mViewModel.GenerateInitialWorkout();

        mSelAllButton = findViewById(R.id.keep_all);
        mSelAllButton.setOnClickListener(v -> { mViewModel.setAllRegenFlags(Boolean.FALSE); });

        mDiscAllButton = findViewById(R.id.discard_all);
        mDiscAllButton.setOnClickListener(v -> { mViewModel.setAllRegenFlags(Boolean.TRUE); });

        mInvertButton = findViewById(R.id.invert_sel);
        mInvertButton.setOnClickListener(v -> { mViewModel.invertRegenFlags(); });

        mBackButton = findViewById(R.id.back_button);
        mBackButton.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;

            finish();
        });

        mRemixButton = findViewById(R.id.remix_workout_button);
        mRemixButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;

                mViewModel.RemixWorkout();
            });

        mNextButton = findViewById(R.id.start_workout_button);
        mNextButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;
                ArrayList<ArrayList<Integer>> finalExercises;

                finalExercises = new ArrayList<>(mViewModel.getAllExercises().getValue());


                Bundle outgoingOptionValues = new Bundle();
                Intent nextIntent = new Intent(PreliminaryWorkout.this, FinalWorkoutActivity.class);

                if (mWorkoutKey.length() > 0) {
                    outgoingOptionValues.putString("workoutKey", mWorkoutKey);
                }
                outgoingOptionValues.putInt("numReps", mNumReps);
                outgoingOptionValues.putInt("repTimeIndex", mRepTimeIndex);
                outgoingOptionValues.putInt("numSets", mNumSets);
                outgoingOptionValues.putInt("restTimeIndex", mRestTimeIndex);
                outgoingOptionValues.putBoolean("useReps", mUseReps);
                outgoingOptionValues.putString("repString", mRepString);
                outgoingOptionValues.putIntegerArrayList("muscleGroups", mViewModel.getSelectedMuscleGroups().getValue());
                outgoingOptionValues.putIntegerArrayList("equipmentTypes", mViewModel.getSelectedEquipmentTypes().getValue());

                for (int i = 0; i < finalExercises.size(); i++)
                {
                    outgoingOptionValues.putIntegerArrayList("exerciseList"+ i, finalExercises.get(i) );
                }

                nextIntent.putExtras(outgoingOptionValues);

                startActivity(nextIntent);
            });

        if (mWorkoutKey.length() > 0) {
            mNextButton.callOnClick();
        }
    }
}