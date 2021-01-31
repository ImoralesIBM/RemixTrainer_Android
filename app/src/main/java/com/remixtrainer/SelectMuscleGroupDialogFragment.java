package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;


public class SelectMuscleGroupDialogFragment extends DialogFragment
{
    private RecyclerView mMuscleGroupList;
    private ImageView mCloseButton;

    public ArrayList<Boolean> mCheckboxesSelected;
    public SelectMuscleGroupItemFragment.OnListFragmentInteractionListener mListener;

    public SelectMuscleGroupDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectMuscleGroupDialogFragment.
     */
    public static SelectMuscleGroupDialogFragment newInstance(List<Boolean> checkboxList,
                                                              SelectMuscleGroupItemFragment.OnListFragmentInteractionListener listener) {
        SelectMuscleGroupDialogFragment fragment = new SelectMuscleGroupDialogFragment();
        fragment.mCheckboxesSelected = new ArrayList<>(checkboxList);
        fragment.mListener = listener;

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
        return inflater.inflate(R.layout.fragment_select_muscle_group_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mMuscleGroupList = getDialog().findViewById(R.id.muscle_group_list);
        mMuscleGroupList.setAdapter(new SelectMuscleGroupItemRecyclerViewAdapter(mDatabase.mMuscleGroupList, mCheckboxesSelected, mListener));

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
        mListener = null;
    }

    public interface OnItemsSelectedListener
    {

    }
}
