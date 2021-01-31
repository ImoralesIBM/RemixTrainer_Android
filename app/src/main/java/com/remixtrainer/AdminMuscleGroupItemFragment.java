package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class AdminMuscleGroupItemFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AdminMuscleGroupItemFragment() {
    }

    @SuppressWarnings("unused")
    public static AdminMuscleGroupItemFragment newInstance(int columnCount) {
        return new AdminMuscleGroupItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
