package com.remixtrainer;

import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FinalWorkoutActivity extends ToolbarActivityTemplate implements FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener {
    private FinalWorkoutViewModel mViewModel;
    private TextView mRepString;
    private Button mRegenerateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<ArrayList<Integer>> exListTmp = new ArrayList<>();

        FinalMuscleGroupItemFragment fragment = new FinalMuscleGroupItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.selected_muscle_group_list_placeholder, fragment);
        ft.commit();

        Bundle inboundOptionValues = getIntent().getExtras();
        mViewModel = ViewModelProviders.of(this).get(FinalWorkoutViewModel.class);
        mViewModel.setSelectedMuscleGroups(inboundOptionValues.getIntegerArrayList("muscleGroups"));
        mViewModel.setSelectedEquipmentTypes(inboundOptionValues.getIntegerArrayList("equipmentTypes"));

        for (int i = 0; i < mViewModel.getSelectedMuscleGroups().size(); i++)
        {
            exListTmp.add(inboundOptionValues.getIntegerArrayList("exerciseList" + Integer.toString(i)));
        }

        mViewModel.setExercises(exListTmp);

        mRepString = (TextView) findViewById(R.id.rep_string);
        mRepString.setText(inboundOptionValues.getString("repString"));

        mRegenerateButton = findViewById(R.id.regenerate_workout_button);
        mRegenerateButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;

                finish();
            });
    }

    public void onPlayVideo(String videoId) {
        // The user requests to play a video

        FragmentManager fm = getSupportFragmentManager();
        YouTubeVideoDialogFragment playerDialog = YouTubeVideoDialogFragment.newInstance(videoId);
        playerDialog.show(fm, "fragment_you_tube_video");
    }
}
