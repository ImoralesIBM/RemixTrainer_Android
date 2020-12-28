package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminSetExerciseFiltersDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminSetExerciseFiltersDialogFragment extends DialogFragment {

    private AdminFilterMuscleGroupItemFragment.OnListFragmentInteractionListener mMuscleGroupListener;
    private AdminFilterEquipmentTypeItemFragment.OnListFragmentInteractionListener mEquipmentTypeListener;

    private RecyclerView mMuscleGroupList, mEquipmentTypeList;
    private ImageView mCloseButton;

    public ArrayMap<Integer, Boolean> mMuscleGroupsSelected, mEquipmentTypesSelected;

    public AdminSetExerciseFiltersDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param muscleGroupsSelected List of Muscle Groups selected.
     * @param equipmentTypesSelected List of Equipment Types selected.
     * @return A new instance of fragment AdminSetExerciseFiltersFragment.
     */
    public static AdminSetExerciseFiltersDialogFragment newInstance(Map<Integer, Boolean> muscleGroupsSelected,
                                                                    Map<Integer, Boolean> equipmentTypesSelected,
                                                                    AdminFilterMuscleGroupItemFragment.OnListFragmentInteractionListener muscleGroupListener,
                                                                    AdminFilterEquipmentTypeItemFragment.OnListFragmentInteractionListener equipmentTypeListener) {
        AdminSetExerciseFiltersDialogFragment fragment = new AdminSetExerciseFiltersDialogFragment();

        fragment.mMuscleGroupsSelected = new ArrayMap<>();
        fragment.mMuscleGroupsSelected.putAll(muscleGroupsSelected);
        fragment.mEquipmentTypesSelected = new ArrayMap<>();
        fragment.mEquipmentTypesSelected.putAll(equipmentTypesSelected);

        fragment.mMuscleGroupListener = muscleGroupListener;
        fragment.mEquipmentTypeListener = equipmentTypeListener;

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
        return inflater.inflate(R.layout.fragment_admin_set_exercise_filters_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mMuscleGroupList = getDialog().findViewById(R.id.mg_list);
        mMuscleGroupList.setAdapter(
                new AdminFilterMuscleGroupItemRecyclerViewAdapter(mDatabase.mMuscleGroupList,
                        mMuscleGroupsSelected, mMuscleGroupListener)
        );

        mEquipmentTypeList = getDialog().findViewById(R.id.eq_list);
        mEquipmentTypeList.setAdapter(
                new AdminFilterEquipmentTypeItemRecyclerViewAdapter(mDatabase.mEquipmentTypeList,
                        mEquipmentTypesSelected, mEquipmentTypeListener)
        );

        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(v -> { dismiss(); });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}