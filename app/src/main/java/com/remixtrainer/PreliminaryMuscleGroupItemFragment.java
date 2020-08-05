package com.remixtrainer;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class PreliminaryMuscleGroupItemFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private PreliminaryWorkoutViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PreliminaryMuscleGroupItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PreliminaryMuscleGroupItemFragment newInstance(int columnCount) {
        PreliminaryMuscleGroupItemFragment fragment = new PreliminaryMuscleGroupItemFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preliminary_muscle_group_item_list, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(PreliminaryWorkoutViewModel.class);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(), mViewModel.getAllExercises().getValue(), mViewModel.getAllRegenFlags().getValue(), mListener, mViewModel));
            mViewModel.getAllExercises().observe(this, exList -> {
                recyclerView.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(), exList, mViewModel.getAllRegenFlags().getValue(), mListener, mViewModel));
            });
            mViewModel.getAllRegenFlags().observe(this, flagList -> {
                recyclerView.setAdapter(new PreliminaryMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups().getValue(), mViewModel.getAllExercises().getValue(), flagList, mListener, mViewModel));
            });
        }
        return view;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
    }
}
