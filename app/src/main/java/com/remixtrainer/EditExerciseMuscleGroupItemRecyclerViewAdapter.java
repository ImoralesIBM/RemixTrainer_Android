package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;
import androidx.collection.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.remixtrainer.SelectMuscleGroupItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditExerciseMuscleGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<EditExerciseMuscleGroupItemRecyclerViewAdapter.ViewHolder> {

    private final AdminEditExerciseViewModel mViewModel;
    private final ArrayMap<Integer, String> mValues;

    public EditExerciseMuscleGroupItemRecyclerViewAdapter(Map<Integer, String> items, AdminEditExerciseViewModel aeevm) {
        mValues = new ArrayMap<>(items.size());
        mValues.putAll(items);

        mViewModel = aeevm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_edit_exercise_muscle_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdGroup = Integer.parseInt(mValues.keyAt(position).toString());
        holder.mCheckBoxView.setTag("group"+holder.mIdGroup);
        holder.mContentView.setText(mValues.get(holder.mIdGroup));
        holder.mContentView.setContentDescription(mValues.get(holder.mIdGroup));

        holder.mCheckBoxView.setChecked(mViewModel.getMuscleGroupSelection(holder.mIdGroup));
        holder.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mViewModel.setMuscleGroupSelection(holder.mIdGroup, buttonView.isChecked());
            });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mCheckBoxView;
        public final TextView mContentView;

        public int mIdGroup;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBoxView = view.findViewById(R.id.checkBox);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
