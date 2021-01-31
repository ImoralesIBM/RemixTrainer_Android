package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class PreliminaryMuscleGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<PreliminaryMuscleGroupItemRecyclerViewAdapter.ViewHolder> {

    private final List<Integer> mParentValues;
    private final List<ArrayList<Integer>> mChildValues;
    private final List<ArrayList<Boolean>> mRegenFlags;
    private final PreliminaryWorkoutViewModel mViewModel;

    private List<ArrayList<Boolean>> mUpdatedRegenFlags;

    public PreliminaryMuscleGroupItemRecyclerViewAdapter(List<Integer> parentItems,
                                                         List<ArrayList<Integer>> childItems,
                                                         List<ArrayList<Boolean>> regenFlagItems,
                                                         PreliminaryWorkoutViewModel pwvm) {
        mParentValues = parentItems;
        mChildValues = childItems;
        mRegenFlags = regenFlagItems;
        mViewModel = pwvm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_preliminary_muscle_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mIdGroup = mParentValues.get(position);
        holder.mGroupHeading.setText(mDatabase.mMuscleGroupList.get(holder.mIdGroup));
        holder.mExerciseList.setAdapter(new PreliminaryExerciseItemRecyclerViewAdapter(mChildValues.get(position), mRegenFlags.get(position), holder.mIdGroup, position, mViewModel));
    }

    @Override
    public int getItemCount() {
        return mParentValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mGroupHeading;
        public final RecyclerView mExerciseList;
        public int mIdGroup;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mGroupHeading = view.findViewById(R.id.group_heading);
            mExerciseList = view.findViewById(R.id.preliminary_exercise_list);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mGroupHeading.getText() + "'";
        }
    }
}
