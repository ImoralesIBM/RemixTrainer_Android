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


public class SelectEquipmentDialogFragment extends DialogFragment
{
    private RecyclerView mEquipmentTypeList;
    private ImageView mCloseButton;

    public ArrayList<Boolean> mCheckboxesSelected;
    public SelectEquipmentItemFragment.OnListFragmentInteractionListener mListener;

    public SelectEquipmentDialogFragment() {
        // Required empty public constructor
    }

    public static SelectEquipmentDialogFragment newInstance(List<Boolean> checkboxList,
                                                            SelectEquipmentItemFragment.OnListFragmentInteractionListener listener)
    {
        SelectEquipmentDialogFragment fragment = new SelectEquipmentDialogFragment();
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
        return inflater.inflate(R.layout.fragment_select_equipment_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mEquipmentTypeList = getDialog().findViewById(R.id.equipment_type_list);
        mEquipmentTypeList.setAdapter(new SelectEquipmentItemRecyclerViewAdapter(mDatabase.mEquipmentTypeList, mCheckboxesSelected, mListener));

        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(v -> { dismiss(); });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("key",5);
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
