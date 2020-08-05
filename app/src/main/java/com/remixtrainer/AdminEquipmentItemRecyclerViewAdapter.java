package com.remixtrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.remixtrainer.AdminEquipmentItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminEquipmentItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminEquipmentItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Integer> mValues;
    private final OnListFragmentInteractionListener mListener;

    public AdminEquipmentItemRecyclerViewAdapter(List<Integer> items, OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_equipment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.idEquipment = mValues.get(position);
        holder.mContentView.setText(mDatabase.mEquipmentTypeList.get(holder.idEquipment).description);
        holder.mCodeView.setText(mDatabase.mEquipmentTypeList.get(holder.idEquipment).code);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mCodeView;
        private final ImageButton mEditButton;
        private final ImageButton mDeleteButton;

        public int idEquipment;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = view.findViewById(R.id.content);
            mCodeView = view.findViewById(R.id.code);
            mEditButton = view.findViewById(R.id.edit_btn);
            mEditButton.setOnClickListener(v -> {
                    FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    AdminEquipmentTypeDialogFragment editorDialog = AdminEquipmentTypeDialogFragment.newInstance(idEquipment);
                    editorDialog.show(fm, "fragment_admin_equipment_type_dialog");
                });

            mDeleteButton = view.findViewById(R.id.delete_btn);
            mDeleteButton.setOnClickListener(v -> {
                                                     AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                                     builder.setTitle("Confirm Delete Equipment Type");
                                                     builder.setMessage("You are about to delete this equipment type. Are you sure you want to proceed?");
                                                     builder.setCancelable(false);
                                                     builder.setPositiveButton("Yes", (dialog, which) -> {
                                                             mDatabase.deleteEquipmentTypeFromDB(idEquipment);
                                                         });

                                                     builder.setNegativeButton("No", (dialog, which) -> {});

                                                     builder.show();
                                                 });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
