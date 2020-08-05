package com.remixtrainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class SelectMuscleGroupDialogFragment extends DialogFragment
{
    private ImageView mCloseButton;
    private OnItemsSelectedListener mListener;

    public ArrayList<Boolean> mCheckboxesSelected;

    public SelectMuscleGroupDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectMuscleGroupDialogFragment.
     */
    public static SelectMuscleGroupDialogFragment newInstance(List<Boolean> checkboxList) {
        SelectMuscleGroupDialogFragment fragment = new SelectMuscleGroupDialogFragment();
        fragment.mCheckboxesSelected = new ArrayList<>(checkboxList);

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
        View v = inflater.inflate(R.layout.fragment_select_muscle_group_dialog, container, false);

        SelectMuscleGroupItemFragment fragment = new SelectMuscleGroupItemFragment();
        fragment.setCheckboxes(mCheckboxesSelected);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.muscle_group_list_placeholder, fragment);
        ft.commit();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
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
