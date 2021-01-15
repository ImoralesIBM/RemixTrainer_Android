package com.remixtrainer;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.remixtrainer.DatabaseLocal;

import java.util.ArrayList;
import java.util.List;

public class PreliminaryWorkout extends ToolbarActivityTemplate {

    private Button mBackButton, mRemixButton, mNextButton;
    private ImageButton mSelAllButton, mDiscAllButton, mInvertButton;
    private PreliminaryWorkoutViewModel mViewModel;
    private String mRepString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preliminary_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreliminaryMuscleGroupItemFragment fragment = new PreliminaryMuscleGroupItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.preliminary_muscle_group_list_placeholder, fragment);
        ft.commit();

        Bundle inboundOptionValues = getIntent().getExtras();

        mViewModel = ViewModelProviders.of(this).get(PreliminaryWorkoutViewModel.class);
        mViewModel.setSelectedEquipmentTypes(inboundOptionValues.getIntegerArrayList("equipmentTypes"));
        mViewModel.setSelectedMuscleGroups(inboundOptionValues.getIntegerArrayList("muscleGroups"));

        if (!inboundOptionValues.getBoolean("useReps"))
        {
            mRepString = String.valueOf(inboundOptionValues.getInt("numReps")) + " reps";
        }
        else {
            mRepString = inboundOptionValues.getString("repTime");
        }
        mRepString += ("; " + String.valueOf(inboundOptionValues.getInt("numSets")*getResources().getInteger(R.integer.sets_step)) + " sets");
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


                Bundle optionValues = new Bundle();
                Intent nextIntent = new Intent(PreliminaryWorkout.this, FinalWorkoutActivity.class);

                optionValues.putString("repString", mRepString);
                optionValues.putIntegerArrayList("muscleGroups", mViewModel.getSelectedMuscleGroups().getValue());
                optionValues.putIntegerArrayList("equipmentTypes", mViewModel.getSelectedEquipmentTypes().getValue());

                for (int i = 0; i < finalExercises.size(); i++)
                {
                    optionValues.putIntegerArrayList("exerciseList"+Integer.toString(i), finalExercises.get(i) );
                }

                nextIntent.putExtras(optionValues);

                startActivity(nextIntent);
            });
    }
}