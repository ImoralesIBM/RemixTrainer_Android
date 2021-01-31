package com.remixtrainer;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class FinalWorkoutActivity extends ToolbarActivityTemplate {
    private FinalWorkoutViewModel mViewModel;
    private Integer mNumReps, mRepTimeIndex, mNumSets, mRestTimeIndex;
    private Boolean mUseReps;
    private TextView mRepString;
    private Button mRegenerateButton, mRestartButton, mStoreButton;
    private RecyclerView mMuscleGroupList;

    private String mWorkoutKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ArrayList<Integer>> exListTmp = new ArrayList<>();

        Bundle inboundOptionValues = getIntent().getExtras();
        mViewModel = ViewModelProviders.of(this).get(FinalWorkoutViewModel.class);
        mViewModel.setSelectedMuscleGroups(inboundOptionValues.getIntegerArrayList("muscleGroups"));
        mViewModel.setSelectedEquipmentTypes(inboundOptionValues.getIntegerArrayList("equipmentTypes"));

        mNumReps = inboundOptionValues.getInt("numReps");
        mRepTimeIndex = inboundOptionValues.getInt("repTimeIndex");
        mNumSets = inboundOptionValues.getInt("numSets");
        mRestTimeIndex = inboundOptionValues.getInt("restTimeIndex");
        mUseReps = inboundOptionValues.getBoolean("useReps");

        if (inboundOptionValues.containsKey("workoutKey")) {
            mWorkoutKey = inboundOptionValues.getString("workoutKey");
        }

        for (int i = 0; i < mViewModel.getSelectedMuscleGroups().size(); i++)
        {
            exListTmp.add(inboundOptionValues.getIntegerArrayList("exerciseList" + i));
        }

        mViewModel.setExercises(exListTmp);

        mMuscleGroupList = (RecyclerView) findViewById(R.id.final_muscle_group_list);
        mMuscleGroupList.setAdapter(new FinalMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups(),
                mViewModel.getAllExercises(),
                new FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener() {
                    @Override
                    public void onPlayVideo(String videoLink) {
                        // The user requests to play a video

                        FragmentManager fm = getSupportFragmentManager();
                        YouTubeVideoDialogFragment playerDialog = YouTubeVideoDialogFragment.newInstance(videoLink);
                        playerDialog.show(fm, "fragment_you_tube_video");
                    }
                },
                mViewModel));

        mRepString = (TextView) findViewById(R.id.rep_string);
        mRepString.setText(inboundOptionValues.getString("repString"));

        mRegenerateButton = findViewById(R.id.regenerate_workout_button);
        mRegenerateButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;

                finish();
            });

        mRestartButton = findViewById(R.id.restart_workout_button);
        mRestartButton.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;

            Intent intent = new Intent(FinalWorkoutActivity.this, InitialParamsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        });

        mStoreButton = findViewById(R.id.store_workout_button);
        mStoreButton.setOnClickListener(v -> {
            FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
            SaveWorkoutDialogFragment saveWorkoutDialog =
                    SaveWorkoutDialogFragment.newInstance(mViewModel.getSelectedMuscleGroups(),
                                                          mViewModel.getSelectedEquipmentTypes(),
                                                          mViewModel.getAllExercises(),
                                                          mNumReps, mRepTimeIndex, mNumSets,
                                                          mRestTimeIndex, mUseReps);
            saveWorkoutDialog.show(fm, "fragment_save_workout_dialog");
        });
        mStoreButton.setEnabled((mViewModel.getWorkoutKey().length() > 0) ||
                                    mDatabase.mIsAdmin ||
                                        (getResources().getInteger(R.integer.max_workouts) > mDatabase.mSavedWorkoutList.size()));

    }
}
