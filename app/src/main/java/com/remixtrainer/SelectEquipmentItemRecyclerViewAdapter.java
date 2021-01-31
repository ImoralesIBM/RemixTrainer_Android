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

import com.remixtrainer.SelectEquipmentItemFragment.OnListFragmentInteractionListener;

public class SelectEquipmentItemRecyclerViewAdapter extends RecyclerView.Adapter<SelectEquipmentItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayMap<Integer, FitnessEquipment> mValues;
    private final ArrayList<Boolean> mCheckboxesSelected;

    private final OnListFragmentInteractionListener mListener;

    public SelectEquipmentItemRecyclerViewAdapter(Map<Integer, FitnessEquipment> items, List<Boolean> checkboxList, OnListFragmentInteractionListener listener) {
        mValues = new ArrayMap<>(items.size());
        mValues.putAll(items);

        mListener = listener;
        mCheckboxesSelected = new ArrayList<>(checkboxList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_select_equipment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdEquipment = Integer.parseInt(mValues.keyAt(position).toString());
        holder.mCheckBoxView.setTag("equipment"+holder.mIdEquipment);
        holder.mContentView.setText(mValues.get(holder.mIdEquipment).description + " (" + mValues.get(holder.mIdEquipment).code + ")");
        holder.mContentView.setContentDescription(mValues.get(holder.mIdEquipment).description);

        if (mCheckboxesSelected.size() > position) {
            holder.mCheckBoxView.setChecked(mCheckboxesSelected.get(position));
        }

        holder.mCheckBoxView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Send the event to the host activity
                mListener.onEquipmentItemSelected(position, buttonView.isChecked());
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
        public int mIdEquipment;

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
