
package com.remixtrainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
 * {@link AdminMuscleGroupDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminMuscleGroupDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminMuscleGroupDialogFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;

    private ImageView mCloseButton;
    private Button mSaveButton, mCancelButton;
    private EditText mGroupName;

    public Integer mIdGroup;

    public AdminMuscleGroupDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idGroup Muscle Group ID.
     * @return A new instance of fragment AdminMuscleGroupDialogFragment.
     */
    public static AdminMuscleGroupDialogFragment newInstance(int idGroup) {
        AdminMuscleGroupDialogFragment fragment = new AdminMuscleGroupDialogFragment();
        fragment.mIdGroup = idGroup;

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
        return inflater.inflate(R.layout.fragment_admin_muscle_group_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSaveButton = getDialog().findViewById(R.id.save_muscle_group_button);
        mCancelButton = getDialog().findViewById(R.id.cancel_edit_button);
        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mGroupName = getDialog().findViewById(R.id.muscle_group_name);

        if (mIdGroup > -1)
        {
            mGroupName.setText(mDatabase.mMuscleGroupList.get(mIdGroup));
        }

        mSaveButton.setOnClickListener(v -> {
                // Reset errors.
                mGroupName.setError(null);

                // Store values at the time of the call.
                String description = mGroupName.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                // Ensure that the form is complete.
                if (TextUtils.isEmpty(description))
                {
                    mGroupName.setError(getString(R.string.muscle_group_name_required));
                    focusView = mGroupName;
                    cancel = true;
                }
                if (!cancel && (mDatabase.mMuscleGroupList.entrySet().stream()
                        .anyMatch(i -> ((i.getValue().equalsIgnoreCase(description)) && !i.getKey().equals(mIdGroup))))) {
                    mGroupName.setError(getString(R.string.muscle_group_name_duplicate));
                    focusView = mGroupName;
                    cancel = true;
                }

                if (!cancel)
                {
                    if (mIdGroup > -1)
                    {
                        mDatabase.updateMuscleGroupInDB(mIdGroup, description);

                    }
                    else
                    {
                        mDatabase.addMuscleGroupToDB(description);
                    }
                    dismiss();
                }
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
