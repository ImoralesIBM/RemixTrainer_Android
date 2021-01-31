package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class SavedWorkoutItemRecyclerViewAdapter extends RecyclerView.Adapter<SavedWorkoutItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    private final SavedWorkoutItemFragment.OnListFragmentInteractionListener mListener;

    public SavedWorkoutItemRecyclerViewAdapter(List<String> items,
                                               SavedWorkoutItemFragment.OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_saved_workout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.workoutKey = mValues.get(position);
        holder.mWorkoutName.setText(mDatabase.mSavedWorkoutList.get(holder.workoutKey).getName());
        holder.mAccessedDate.setText(mDatabase.mSavedWorkoutList.get(holder.workoutKey).getTimeAccessed().toString());
        holder.mUpdatedDate.setText(mDatabase.mSavedWorkoutList.get(holder.workoutKey).getTimeUpdated().toString());
        holder.mCreatedDate.setText(mDatabase.mSavedWorkoutList.get(holder.workoutKey).getTimeCreated().toString());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public String workoutKey;

        private final TextView mWorkoutName, mAccessedDate, mUpdatedDate, mCreatedDate;
        private final ImageButton mSelectButton;
        private final ImageButton mDeleteButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mWorkoutName = (TextView) view.findViewById(R.id.workout_name);
            mAccessedDate = (TextView) view.findViewById(R.id.accessed_date);
            mUpdatedDate = (TextView) view.findViewById(R.id.updated_date);
            mCreatedDate = (TextView) view.findViewById(R.id.created_date);

            mSelectButton = (ImageButton) view.findViewById(R.id.select_btn);
            mSelectButton.setOnClickListener(v -> {
                mListener.onWorkoutSelected(workoutKey);
            });

            mDeleteButton = (ImageButton) view.findViewById(R.id.delete_btn);
            mDeleteButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirm Delete Workout");
                builder.setMessage("You are about to delete this workout. Are you sure you want to proceed?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        (dialog, which) -> {
                            mDatabase.deleteWorkoutFromDB(workoutKey);
                        });

                builder.setNegativeButton("No",
                        (dialog, which) -> { });

                builder.show();
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + workoutKey + "'";
        }
    }
}