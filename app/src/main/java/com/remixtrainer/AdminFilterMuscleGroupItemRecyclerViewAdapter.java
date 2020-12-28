
package com.remixtrainer;

import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminFilterMuscleGroupItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminFilterMuscleGroupItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayMap<Integer, String> mValues;
    private final AdminFilterMuscleGroupItemFragment.OnListFragmentInteractionListener mListener;
    private final Map<Integer, Boolean> mCheckboxesSelected;

    public AdminFilterMuscleGroupItemRecyclerViewAdapter(Map<Integer, String> items,
                                                         Map<Integer, Boolean> checkboxMap,
                                                         AdminFilterMuscleGroupItemFragment.OnListFragmentInteractionListener listener) {
        mValues = new ArrayMap<>(items.size());
        mValues.putAll(items);

        mListener = listener;
        mCheckboxesSelected = new ArrayMap<>();
        mCheckboxesSelected.putAll(checkboxMap);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_filter_muscle_group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdGroup = Integer.parseInt(mValues.keyAt(position).toString());
        holder.mCheckBoxView.setTag("group"+holder.mIdGroup);
        holder.mContentView.setText(mValues.get(holder.mIdGroup));
        holder.mContentView.setContentDescription(mValues.get(holder.mIdGroup));

        if (mCheckboxesSelected.size() > position) {
            holder.mCheckBoxView.setChecked(mCheckboxesSelected.get(holder.mIdGroup));
        }

        holder.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Send the event to the host activity
            mListener.onMuscleGroupItemSelected(holder.mIdGroup, buttonView.isChecked());
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