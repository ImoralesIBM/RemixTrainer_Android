package com.remixtrainer;

import android.content.Context;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.remixtrainer.SelectEquipmentItemFragment.OnListFragmentInteractionListener;

public class EditExerciseEquipmentItemRecyclerViewAdapter extends RecyclerView.Adapter<EditExerciseEquipmentItemRecyclerViewAdapter.ViewHolder> {

    private final AdminEditExerciseViewModel mViewModel;
    private final ArrayMap<Integer, FitnessEquipment> mValues;

    public EditExerciseEquipmentItemRecyclerViewAdapter(Map<Integer, FitnessEquipment> items, AdminEditExerciseViewModel aeevm)
    {
        mValues = new ArrayMap<>(items.size());
        mValues.putAll(items);

        mViewModel = aeevm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_edit_exercise_equipment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdEquipment = Integer.parseInt(mValues.keyAt(position).toString());
        holder.mCheckBoxView.setTag("equipment"+holder.mIdEquipment);
        holder.mContentView.setText(mValues.get(holder.mIdEquipment).description + " (" + mValues.get(holder.mIdEquipment).code + ")");

        holder.mCheckBoxView.setChecked(mViewModel.getEquipmentTypeSelection(holder.mIdEquipment));
        holder.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mViewModel.setEquipmentTypeSelection(holder.mIdEquipment, buttonView.isChecked());
            });

        holder.mVideoLinkView.setText(mViewModel.getEquipmentVideo(holder.mIdEquipment));
        holder.mVideoLinkView.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    String cleanLink = ((EditText) v).getText().toString().trim();
                    if (cleanLink.contains("youtube.com/watch?v="))
                    {
                        cleanLink = cleanLink.split("youtube.com/watch?v=",2)[1];
                    }
                    if (cleanLink.contains("youtu.be/"))
                    {
                        cleanLink = cleanLink.split("youtu.be/",2)[1];
                    }
                    mViewModel.setEquipmentVideo(holder.mIdEquipment, cleanLink);
                }
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
        public final EditText mVideoLinkView;
        public int mIdEquipment;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBoxView = view.findViewById(R.id.checkBox);
            mContentView = view.findViewById(R.id.content);
            mVideoLinkView = view.findViewById(R.id.video_link);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
