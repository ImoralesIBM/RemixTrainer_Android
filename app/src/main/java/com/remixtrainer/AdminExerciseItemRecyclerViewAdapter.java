package com.remixtrainer;

import android.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminExerciseItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminExerciseItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Integer> mValues;
    private final AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener mListener;

    public AdminExerciseItemRecyclerViewAdapter(List<Integer> items, AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ExerciseSummary localExercise = mDatabase.mExerciseTypeList.get(mValues.get(position));

        ArrayMap<Integer, Boolean> localMuscleGroups =
                new ArrayMap<>();
        ArrayMap<Integer, Boolean> localEquipmentTypes = new ArrayMap<>();

        localMuscleGroups.putAll(mDatabase.mMuscleGroupList.keySet().stream().collect(Collectors.toMap(i -> i, i -> localExercise.getMuscleGroups().contains(i))));
        localEquipmentTypes.putAll(mDatabase.mEquipmentTypeList.keySet().stream().collect(Collectors.toMap(i -> i, i -> localExercise.getEquipmentTypesOnly().contains(i))));

        long numVideos = localExercise.getEquipmentAndVideos().values().stream().filter(i -> (i.length() > 0)).count();

        holder.idExercise = localExercise.getId();

        if (numVideos == 0) {
            holder.mHasVideoIcon.setVisibility(View.INVISIBLE);
        }
        if (numVideos > 0) {
            holder.mHasVideoIcon.setVisibility(View.VISIBLE);
            holder.mHasVideoIcon.setAlpha(0.5F);
        }
        if (numVideos == localExercise.getEquipmentAndVideos().entrySet().size()) {
            holder.mHasVideoIcon.setVisibility(View.VISIBLE);
            holder.mHasVideoIcon.setAlpha(1.0F);
        }


        holder.mContentView.setText(localExercise.getDescription());
        holder.mMuscleGroupList.setAdapter(
                new AdminExerciseMuscleGroupViewItemRecyclerViewAdapter(localMuscleGroups)
        );
        holder.mEquipmentTypeList.setAdapter(
                new AdminExerciseEquipmentTypeViewItemRecyclerViewAdapter(localEquipmentTypes,
                        localExercise.getEquipmentAndVideos(), mListener)
        );
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final RecyclerView mMuscleGroupList, mEquipmentTypeList;

        private final ImageButton mExpandButton;
        private final ImageButton mEditButton, mDeleteButton;
        private final ImageView mHasVideoIcon;

        private final View mExerciseDetails;

        public int idExercise;

        private Integer expandState = 0;

        private final Integer[] expandButtonIcon = {android.R.drawable.ic_menu_more, android.R.drawable.btn_minus};
        private final Integer[] detailVisibility = {View.GONE, View.VISIBLE};

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.exercise_name);

            mExerciseDetails = view.findViewById(R.id.exercise_details);

            mExpandButton = view.findViewById(R.id.expand_button);
            mExpandButton.setOnClickListener(v -> {
                    expandState = 1 - expandState;
                    mExpandButton.setImageResource(expandButtonIcon[expandState]);
                    mExerciseDetails.setVisibility(detailVisibility[expandState]);
                });

            mHasVideoIcon = view.findViewById(R.id.has_video);

            mEditButton = view.findViewById(R.id.edit_btn);
            mEditButton.setOnClickListener(v -> {
                    FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    AdminEditExerciseDialogFragment editorDialog = AdminEditExerciseDialogFragment.newInstance(idExercise);
                    editorDialog.show(fm, "fragment_admin_edit_exercise_dialog");
                });

            mDeleteButton = view.findViewById(R.id.delete_btn);
            mDeleteButton.setOnClickListener(v -> {
                                                     AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                     builder.setTitle("Confirm Delete Exercise");
                                                     builder.setMessage("You are about to delete this exercise. Are you sure you want to proceed?");
                                                     builder.setCancelable(false);
                                                     builder.setPositiveButton("Yes", (dialog, which) -> {
                                                             mDatabase.deleteExerciseType(idExercise);
                                                         });

                                                     builder.setNegativeButton("No",
                                                             (dialog, which) -> {});

                                                     builder.show();
                                                 });

            mMuscleGroupList = view.findViewById(R.id.admin_muscle_group_list);
            mEquipmentTypeList = view.findViewById(R.id.admin_equipment_type_list);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
