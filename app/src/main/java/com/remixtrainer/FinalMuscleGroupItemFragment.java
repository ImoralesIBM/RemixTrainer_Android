package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class FinalMuscleGroupItemFragment extends Fragment {

  private FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener mListener;
  private FinalWorkoutViewModel mViewModel;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public FinalMuscleGroupItemFragment() {
  }


  public static FinalMuscleGroupItemFragment newInstance(int columnCount) {
    return new FinalMuscleGroupItemFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener) {
      mListener = (FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
              + " must implement OnListFragmentInteractionListener");
    }
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
