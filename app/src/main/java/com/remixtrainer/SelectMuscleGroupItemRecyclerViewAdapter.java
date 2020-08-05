package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;
import androidx.collection.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.remixtrainer.SelectMuscleGroupItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectMuscleGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<SelectMuscleGroupItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayMap<Integer, String> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final ArrayList<Boolean> mCheckboxesSelected;

    public SelectMuscleGroupItemRecyclerViewAdapter(Map<Integer, String> items, List<Boolean> checkboxList, OnListFragmentInteractionListener listener) {
        mValues = new ArrayMap<>(items.size());
        mValues.putAll(items);

        mListener = listener;
        mCheckboxesSelected = new ArrayList<>(checkboxList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_muscle_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdGroup = Integer.parseInt(mValues.keyAt(position).toString());
        holder.mCheckBoxView.setTag("group"+holder.mIdGroup);
        holder.mContentView.setText(mValues.get(holder.mIdGroup));
        holder.mContentView.setContentDescription(mValues.get(holder.mIdGroup));

        if (mCheckboxesSelected.size() > position) {
            holder.mCheckBoxView.setChecked(mCheckboxesSelected.get(position));
        }

        holder.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Send the event to the host activity
                mListener.onMuscleGroupItemSelected(position, buttonView.isChecked());
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
