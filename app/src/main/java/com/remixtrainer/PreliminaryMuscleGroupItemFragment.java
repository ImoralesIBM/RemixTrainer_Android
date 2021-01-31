package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


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
