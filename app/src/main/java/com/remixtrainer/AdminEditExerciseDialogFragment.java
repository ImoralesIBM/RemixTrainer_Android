package com.remixtrainer;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;


public class AdminEditExerciseDialogFragment extends DialogFragment {

    private AdminEditExerciseViewModel mViewModel;

    private ImageView mCloseButton;
    private Button mSaveButton, mCancelButton;
    private EditText mExerciseName;

    private TextView mMuscleGroupError, mEquipmentTypeError;

    private OnFragmentInteractionListener mListener;

    public Integer mIdExercise;
    public ArrayList<Boolean> mMuscleGroupsSelected, mEquipmentTypesSelected;


    public AdminEditExerciseDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idExercise is the Exercise ID.
     * @return A new instance of fragment AdminEquipmentTypeDialogFragment.
     */
    public static AdminEditExerciseDialogFragment newInstance(Integer idExercise) {
        AdminEditExerciseDialogFragment fragment = new AdminEditExerciseDialogFragment();
        fragment.mIdExercise = idExercise;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(AdminEditExerciseViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_edit_exercise_dialog, container, false);

        mViewModel = ViewModelProviders.of(getActivity()).get(AdminEditExerciseViewModel.class);
        mViewModel.setExerciseId(mIdExercise);

        EditExerciseMuscleGroupItemFragment muscleGroupFragment = new EditExerciseMuscleGroupItemFragment();
        FragmentTransaction ft_mg = getChildFragmentManager().beginTransaction();
        ft_mg.replace(R.id.muscle_group_list_placeholder, muscleGroupFragment);
        ft_mg.commit();

        EditExerciseEquipmentItemFragment equipmentViewFragment = new EditExerciseEquipmentItemFragment();
        FragmentTransaction ft_eq = getChildFragmentManager().beginTransaction();
        ft_eq.replace(R.id.equipment_type_list_placeholder, equipmentViewFragment);
        ft_eq.commit();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mExerciseName = getDialog().findViewById(R.id.exercise_name);

        if (mIdExercise > -1)
        {
            mExerciseName.setText(mDatabase.mExerciseTypeList.get(mIdExercise).getDescription());
        }

        mSaveButton = getDialog().findViewById(R.id.save_exercise_button);
        mSaveButton.setOnClickListener(v -> {
                //Get the focus
                v.requestFocusFromTouch();

                // Reset errors.
                mExerciseName.setError(null);

                mMuscleGroupError.setVisibility(View.GONE);
                mEquipmentTypeError.setVisibility(View.GONE);

                // Store values at the time of the call.
                String description = mExerciseName.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                // Ensure that the form is complete.
                if (TextUtils.isEmpty(description))
                {
                    mExerciseName.setError(getString(R.string.exercise_name_required));
                    focusView = mExerciseName;
                    cancel = true;
                }

                if (!cancel)
                {
                    if (mViewModel.getFinalMuscleGroupSelections().size() == 0)
                    {
                        mMuscleGroupError.setVisibility(View.VISIBLE);
                        cancel = true;
                    }
                    if ((mViewModel.getFinalEquipmentTypeSelections().size() == 0))
                    {
                        mEquipmentTypeError.setVisibility(View.VISIBLE);
                        cancel = true;
                    }
                }

                if (!cancel &&
                        (mDatabase.mExerciseTypeList.values().stream().anyMatch(i -> ((i.getDescription().equalsIgnoreCase(description)) && (i.getId() != mIdExercise))))) {
                    mExerciseName.setError(getString(R.string.exercise_name_duplicate));
                    focusView = mExerciseName;
                    cancel = true;
                }

                if (!cancel)
                {
                    if (mIdExercise > -1)
                    {
                        mDatabase.updateExercise(mIdExercise, description,
                                mViewModel.getFinalMuscleGroupSelections(),
                                mViewModel.getFinalEquipmentVideos());
                    }
                    else
                    {
                        mIdExercise = mDatabase.addExercise(description,
                                mViewModel.getFinalMuscleGroupSelections(),
                                mViewModel.getFinalEquipmentVideos());
                    }

                    dismiss();
                }
            });

        mCancelButton = getDialog().findViewById(R.id.cancel_edit_button);
        mCancelButton.setOnClickListener(v -> { dismiss(); });

        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(v -> { dismiss(); });

        mMuscleGroupError = getDialog().findViewById(R.id.muscle_group_error);
        mEquipmentTypeError = getDialog().findViewById(R.id.fitness_equipment_error);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
