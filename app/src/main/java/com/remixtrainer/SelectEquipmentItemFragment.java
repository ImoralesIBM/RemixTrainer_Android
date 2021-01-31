package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class SelectEquipmentItemFragment extends Fragment {

  private OnListFragmentInteractionListener mListener;
  private ArrayList<Boolean> mCheckboxesSelected;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public SelectEquipmentItemFragment() {
  }

  public static SelectEquipmentItemFragment newInstance(List<Boolean> checkboxList) {
    SelectEquipmentItemFragment fragment = new SelectEquipmentItemFragment();
    fragment.setCheckboxes(checkboxList);

    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public void setCheckboxes(List<Boolean> checkboxList) {
    mCheckboxesSelected = new ArrayList<>(checkboxList);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnListFragmentInteractionListener) {
      mListener = (OnListFragmentInteractionListener) context;
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
    void onEquipmentItemSelected(int position, boolean isChecked);
  }
}
