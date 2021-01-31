package com.remixtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class RetrieveSavedWorkoutActivity extends AppCompatActivity {

    public RecyclerView mWorkoutList;
    public Button mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_saved_workout);

        mWorkoutList = findViewById(R.id.workout_list);
        mWorkoutList.setAdapter(new SavedWorkoutItemRecyclerViewAdapter(
                new ArrayList<>(mDatabase.mSavedWorkoutList.keySet()),
                new SavedWorkoutItemFragment.OnListFragmentInteractionListener() {
                    @Override
                    public void onWorkoutSelected(String key) {
                        Bundle optionValues = new Bundle();
                        optionValues.putString("workoutKey", key);

                        Intent intent = new Intent(RetrieveSavedWorkoutActivity.this, InitialParamsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(optionValues);
                        startActivity(intent);

                        finish();
                    }
                }));

        mReturnButton = findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(v -> {
            finish();
        });
    }
}