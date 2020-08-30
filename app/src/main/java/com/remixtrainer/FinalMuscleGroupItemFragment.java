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

import com.remixtrainer.FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener;

import java.util.List;

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
    FinalMuscleGroupItemFragment fragment = new FinalMuscleGroupItemFragment();

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_final_muscle_group_item_list, container, false);
    mViewModel = ViewModelProviders.of(getActivity()).get(FinalWorkoutViewModel.class);

    // Set the adapter
    if (view instanceof RecyclerView) {
      Context context = view.getContext();
      RecyclerView recyclerView = (RecyclerView) view;
      recyclerView.setLayoutManager(new LinearLayoutManager(context));

      recyclerView.setAdapter(new FinalMuscleGroupItemRecyclerViewAdapter(mViewModel.getSelectedMuscleGroups(), mViewModel.getAllExercises(), mViewModel.mRepString, mListener, mViewModel));
    }
    return view;
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