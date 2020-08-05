package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import com.remixtrainer.FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class FinalExerciseItemRecyclerViewAdapter extends RecyclerView.Adapter<FinalExerciseItemRecyclerViewAdapter.ViewHolder> {

    private FinalWorkoutViewModel mViewModel;
    private int mIdGroup, mPosGroup;

    private final List<Integer> mValues;
    private String mRepString;

    private final OnListFragmentInteractionListener mListener;


    public FinalExerciseItemRecyclerViewAdapter(List<Integer> items, int idGroup, int posGroup, String repString, FinalWorkoutViewModel fwvm, OnListFragmentInteractionListener listener) {
        mValues = items;
        mIdGroup = idGroup;
        mPosGroup = posGroup;
        mRepString = repString;
        mViewModel = fwvm;

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_final_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Map<Integer, String> localEquipmentVideoList;

        holder.mIdExercise = mValues.get(position);
        holder.mExerciseName.setText(mDatabase.mExerciseTypeList.get(holder.mIdExercise).getDescription().toString());
        holder.mExerciseEquipment.setText("(" + mDatabase.mExerciseTypeList.get(holder.mIdExercise).getEquipmentCodeList(mViewModel.getSelectedEquipmentTypes()) + ")");
        holder.mExerciseReps.setText("(" + mRepString + ")");

        localEquipmentVideoList = new ArrayMap<>();
        localEquipmentVideoList
                .putAll(mDatabase.mExerciseTypeList.get(holder.mIdExercise)
                        .getEquipmentAndVideos().entrySet().stream()
                        .filter(i -> mViewModel.getSelectedEquipmentTypes().contains(i.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        holder.mVideoList.setAdapter(
                new FinalEquipmentTypeVideoItemRecyclerViewAdapter(localEquipmentVideoList,
                        mViewModel, mListener));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mExerciseName, mExerciseEquipment, mExerciseReps;
        public final RecyclerView mVideoList;
        public int mIdExercise;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mExerciseName = view.findViewById(R.id.exercise_name);
            mExerciseEquipment = view.findViewById(R.id.exercise_equipment);
            mExerciseReps = view.findViewById(R.id.exercise_reps);
            mVideoList = view.findViewById(R.id.video_list);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mExerciseName.getText() + "'";
        }
    }
}
