package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminEquipmentTypeDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminEquipmentTypeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminEquipmentTypeDialogFragment extends DialogFragment {
    private OnFragmentInteractionListener mListener;

    private ImageView mCloseButton;
    private Button mSaveButton, mCancelButton;
    private EditText mEquipmentName, mEquipmentCode;

    public Integer mIdEquipment;

    public AdminEquipmentTypeDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idEquipment Fitness Equipment ID.
     * @return A new instance of fragment AdminEquipmentTypeDialogFragment.
     */
    public static AdminEquipmentTypeDialogFragment newInstance(int idEquipment) {
        AdminEquipmentTypeDialogFragment fragment = new AdminEquipmentTypeDialogFragment();
        fragment.mIdEquipment = idEquipment;

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
        return inflater.inflate(R.layout.fragment_admin_equipment_type_dialog, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        mSaveButton = getDialog().findViewById(R.id.save_equipment_type_button);
        mCancelButton = getDialog().findViewById(R.id.cancel_edit_button);
        mCloseButton = getDialog().findViewById(R.id.close_btn);

        mEquipmentName = getDialog().findViewById(R.id.equipment_type_name);
        mEquipmentCode = getDialog().findViewById(R.id.equipment_type_code);

        if (mIdEquipment > -1)
        {
            mEquipmentName.setText(mDatabase.mEquipmentTypeList.get(mIdEquipment).description);
            mEquipmentCode.setText(mDatabase.mEquipmentTypeList.get(mIdEquipment).code);
        }

        mSaveButton.setOnClickListener(v -> {
                // Reset errors.
                mEquipmentName.setError(null);
                mEquipmentCode.setError(null);

                // Store values at the time of the call.
                String description = mEquipmentName.getText().toString().trim();
                String code = mEquipmentCode.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                // Ensure that the form is complete.
                if (TextUtils.isEmpty(description))
                {
                    mEquipmentName.setError(getString(R.string.equipment_name_required));
                    focusView = mEquipmentName;
                    cancel = true;
                }
                if (TextUtils.isEmpty(code))
                {
                    mEquipmentCode.setError(getString(R.string.equipment_code_required));
                    focusView = mEquipmentCode;
                    cancel = true;
                }

                if (!cancel &&
                        (mDatabase.mEquipmentTypeList.entrySet().stream().anyMatch(i -> ((i.getValue().description.equalsIgnoreCase(description)) &&
                                !i.getKey().equals(mIdEquipment))))) {
                    mEquipmentName.setError(getString(R.string.equipment_name_duplicate));
                    focusView = mEquipmentName;
                    cancel = true;
                }

                if (!cancel &&
                        (mDatabase.mEquipmentTypeList.entrySet().stream().anyMatch(i -> ((i.getValue().code.equalsIgnoreCase(code)) &&
                                !i.getKey().equals(mIdEquipment))))) {
                    mEquipmentCode.setError(getString(R.string.equipment_code_duplicate));
                    focusView = mEquipmentCode;
                    cancel = true;
                }

                if (!cancel)
                {
                    if (mIdEquipment > -1)
                    {
                        mDatabase.updateEquipmentTypeInDB(mIdEquipment, code, description);
                    }
                    else
                    {
                        mDatabase.addEquipmentTypeToDB(code, description);
                    }
                    dismiss();
                }
                focusView.requestFocus();
            });

        mCancelButton.setOnClickListener(v -> { dismiss(); });

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
