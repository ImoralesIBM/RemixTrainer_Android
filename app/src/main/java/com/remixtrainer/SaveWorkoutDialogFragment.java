package com.remixtrainer;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveWorkoutDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveWorkoutDialogFragment extends DialogFragment {

    private ImageView mCloseButton;
    private Button mSaveButton, mCancelButton;
    private EditText mWorkoutName;

    private List<Integer> mMuscleGroupList;
    private List<Integer> mEquipmentTypeList;
    private List<ArrayList<Integer>> mExerciseList;
    private Integer mNumReps, mRepTimeIndex, mNumSets, mRestTimeIndex;
    private Boolean mUseReps;

    public SaveWorkoutDialogFragment() {
        // Required empty public constructor
    }

    public static SaveWorkoutDialogFragment newInstance(List<Integer> muscleGroups,
                                                        List<Integer> equipmentTypes,
                                                        List<ArrayList<Integer>> selectedExercises,
                                                        Integer numReps, Integer repTimeIndex,
                                                        Integer numSets, Integer restTimeIndex,
                                                        Boolean useReps) {
        SaveWorkoutDialogFragment fragment = new SaveWorkoutDialogFragment();

        fragment.mMuscleGroupList = new ArrayList<>(muscleGroups);
        fragment.mEquipmentTypeList = new ArrayList<>(equipmentTypes);
        fragment.mExerciseList = new ArrayList<>(selectedExercises);
        fragment.mNumReps = numReps;
        fragment.mRepTimeIndex = repTimeIndex;
        fragment.mNumSets = numSets;
        fragment.mRestTimeIndex = restTimeIndex;
        fragment.mUseReps = useReps;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_workout_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSaveButton = getDialog().findViewById(R.id.save_muscle_group_button);
        mCancelButton = getDialog().findViewById(R.id.cancel_edit_button);
        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mWorkoutName = getDialog().findViewById(R.id.workout_name);

        mSaveButton.setOnClickListener(v -> {
            // Reset errors.
            mWorkoutName.setError(null);

            // Store values at the time of the call.
            String description = mWorkoutName.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            // Ensure that the form is complete.
            if (TextUtils.isEmpty(description))
            {
                mWorkoutName.setError(getString(R.string.workout_name_required));
                focusView = mWorkoutName;
                cancel = true;
            }
            if (!cancel && (mDatabase.mSavedWorkoutList.entrySet().stream()
                    .anyMatch(i -> ((i.getValue().getName().equals(description)))))) {
                mWorkoutName.setError(getString(R.string.workout_name_duplicate));
                focusView = mWorkoutName;
                cancel = true;
            }

            if (!cancel)
            {
                mDatabase.saveNewWorkoutToDB(description,
                                             mMuscleGroupList,
                                             mEquipmentTypeList,
                                             mExerciseList,
                                             mNumReps, mRepTimeIndex, mNumSets, mRestTimeIndex,
                                             mUseReps);
                dismiss();
            }
            focusView.requestFocus();
        });

        mCancelButton.setOnClickListener(v -> { dismiss(); });

        mCloseButton.setOnClickListener(v -> { dismiss(); });
    }
}