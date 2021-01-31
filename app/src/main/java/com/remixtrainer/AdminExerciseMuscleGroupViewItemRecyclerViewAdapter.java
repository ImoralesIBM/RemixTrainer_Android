package com.remixtrainer;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;


public class AdminExerciseMuscleGroupViewItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminExerciseMuscleGroupViewItemRecyclerViewAdapter.ViewHolder> {

    private final Map<Integer, Boolean> mValues;

    public AdminExerciseMuscleGroupViewItemRecyclerViewAdapter(Map<Integer, Boolean> items) {
        mValues = new ArrayMap<>();
        mValues.putAll(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_exercise_muscle_group_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdGroup = (new ArrayList<>(mValues.keySet())).get(position);
        holder.mContentView.setText(mDatabase.mMuscleGroupList.get(holder.mIdGroup));
        if (mValues.get(holder.mIdGroup)) {
            holder.mContentView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(holder.mView.getContext().getResources(),
                            R.drawable.ic_checkmark, holder.mView.getContext().getTheme()), null,
                    null, null);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;

        public int mIdGroup;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mContentView = view.findViewById(R.id.muscle_group_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
