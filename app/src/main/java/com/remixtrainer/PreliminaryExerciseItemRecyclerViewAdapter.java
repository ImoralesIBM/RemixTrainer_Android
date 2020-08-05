package com.remixtrainer;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.remixtrainer.PreliminaryExerciseItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class PreliminaryExerciseItemRecyclerViewAdapter extends RecyclerView.Adapter<PreliminaryExerciseItemRecyclerViewAdapter.ViewHolder> {

    private PreliminaryWorkoutViewModel mViewModel;
    private int mIdGroup, mPosGroup;

    private final List<Integer> mValues;
    private final List<Boolean> mRegenFlags;

    public PreliminaryExerciseItemRecyclerViewAdapter(List<Integer> items, List<Boolean> regenItems, int idGroup, int posGroup, PreliminaryWorkoutViewModel pvwm) {
        mValues = items;
        mRegenFlags = regenItems;
        mIdGroup = idGroup;
        mPosGroup = posGroup;
        mViewModel = pvwm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_preliminary_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdExercise = mValues.get(position);
        holder.mExerciseName.setText(mDatabase.mExerciseTypeList.get(holder.mIdExercise).getDescription());
        holder.mExerciseEquipment.setText("(" + mDatabase.mExerciseTypeList.get(holder.mIdExercise).getEquipmentCodeList(mViewModel.getSelectedEquipmentTypes().getValue()) + ")");
        holder.mSelectorButton.setChecked(mRegenFlags.get(position));
        holder.mSelectorButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Send the event to the host activity
                mViewModel.setRegenFlag(mPosGroup, position, buttonView.isChecked());
            });

        holder.mView.setOnClickListener(v -> {});
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mSelectorButton;
        public final TextView mExerciseName;
        public final TextView mExerciseEquipment;
        public int mIdExercise;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSelectorButton = view.findViewById(R.id.keep_or_discard_selector);
            mExerciseName = view.findViewById(R.id.exercise_name);
            mExerciseEquipment = view.findViewById(R.id.exercise_equipment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mExerciseName.getText() + "'";
        }
    }
}
