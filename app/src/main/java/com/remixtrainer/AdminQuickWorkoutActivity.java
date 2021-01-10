package com.remixtrainer;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminQuickWorkoutActivity extends ToolbarActivityTemplate {

    private final String CONFIG_KEY = "quick_workout_settings";

    private Button mSaveButton, mCancelButton;
    private NumberPicker mNumReps, mSets, mRestTime, mTimeReps;

    private ArrayMap<String, Object> mDefaultSettings = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quick_workout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDefaultSettings.putAll(mDatabase.mConfigurationSettings.get(CONFIG_KEY));

        String[] repValues = new String[getResources().getInteger(R.integer.reps_max) - getResources().getInteger(R.integer.reps_min) + 1];
        String[] timeRepValues = new String[getResources().getInteger(R.integer.time_reps_max) - getResources().getInteger(R.integer.time_reps_min) + 1];
        String[] setValues = new String[getResources().getInteger(R.integer.sets_max) - getResources().getInteger(R.integer.sets_min) + 1];
        String[] restValues = new String[getResources().getInteger(R.integer.rest_max) - getResources().getInteger(R.integer.rest_min) + 1];

        int minTmp, secTmp, totalSecTmp;

        mNumReps = findViewById(R.id.num_reps);
        mSets = findViewById(R.id.num_sets);
        mRestTime = findViewById(R.id.rest_time);

        mTimeReps = findViewById(R.id.time_reps);

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
        mNumReps.setValue(Integer.parseInt(mDefaultSettings.get("num_reps").toString()));

        mTimeReps.setDisplayedValues(timeRepValues);
        mTimeReps.setMinValue(getResources().getInteger(R.integer.time_reps_min));
        mTimeReps.setMaxValue(getResources().getInteger(R.integer.time_reps_max));
        mTimeReps.setValue(Integer.parseInt(mDefaultSettings.get("time_reps").toString()));

        for (int i = 0; i < setValues.length; i++)
        {
            setValues[i] = String.valueOf((i + getResources().getInteger(R.integer.sets_min)) * getResources().getInteger(R.integer.sets_step));
        }

        mSets.setDisplayedValues(setValues);
        mSets.setMinValue(getResources().getInteger(R.integer.sets_min));
        mSets.setMaxValue(getResources().getInteger(R.integer.sets_max));
        mSets.setValue(Integer.parseInt(mDefaultSettings.get("num_sets").toString()));

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
        mRestTime.setValue(Integer.parseInt(mDefaultSettings.get("rest_time").toString()));

        mSaveButton = findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(v -> {
                mDefaultSettings.put("num_reps", mNumReps.getValue());
                mDefaultSettings.put("time_reps", mTimeReps.getValue());
                mDefaultSettings.put("num_sets", mSets.getValue());
                mDefaultSettings.put("rest_time", mRestTime.getValue());

                mDatabase.updateConfigSettingsInDB(CONFIG_KEY, mDefaultSettings);

                finish();
            });

        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(v -> { finish(); });
    }

}
