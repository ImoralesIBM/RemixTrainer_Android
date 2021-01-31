package com.remixtrainer;

import android.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminMuscleGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminMuscleGroupItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Integer> mValues;

    public AdminMuscleGroupItemRecyclerViewAdapter(List<Integer> items) {
        mValues = new ArrayList<>(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_muscle_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idGroup = mValues.get(position);
        holder.mContentView.setText(mDatabase.mMuscleGroupList.get(holder.idGroup));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        private final ImageButton mEditButton;
        private final ImageButton mDeleteButton;

        public int idGroup;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = view.findViewById(R.id.content);
            mEditButton = view.findViewById(R.id.edit_btn);
            mEditButton.setOnClickListener(v -> {
                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                AdminMuscleGroupDialogFragment editorDialog = AdminMuscleGroupDialogFragment.newInstance(idGroup);
                editorDialog.show(fm, "fragment_admin_muscle_group_dialog");
            });

            mDeleteButton = view.findViewById(R.id.delete_btn);
            mDeleteButton.setOnClickListener(v -> {
                                                     AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                     builder.setTitle("Confirm Delete Muscle Group");
                                                     builder.setMessage("You are about to delete this muscle group. Are you sure you want to proceed?");
                                                     builder.setCancelable(false);
                                                     builder.setPositiveButton("Yes",
                                                             (dialog, which) -> {
                                                                mDatabase.deleteMuscleGroupFromDB(idGroup);
                                                     });

                                                     builder.setNegativeButton("No",
                                                             (dialog, which) -> { });

                                                     builder.show();
                                                 });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}