package com.remixtrainer;

import androidx.recyclerview.widget.RecyclerView;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;


import com.remixtrainer.FinalEquipmentTypeVideoItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.Map;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class FinalEquipmentTypeVideoItemRecyclerViewAdapter extends RecyclerView.Adapter<FinalEquipmentTypeVideoItemRecyclerViewAdapter.ViewHolder> {

    private final FinalWorkoutViewModel mViewModel;

    private final Map<Integer, String> mValues;
    private String mRepString;

    private final OnListFragmentInteractionListener mListener;


    public FinalEquipmentTypeVideoItemRecyclerViewAdapter(Map<Integer, String> items, FinalWorkoutViewModel fwvm, OnListFragmentInteractionListener listener) {
        mValues = new ArrayMap<>();
        mValues.putAll(items);

        mViewModel = fwvm;

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_final_equipment_type_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Integer localEquipmentId;
        String localVideoLink;

        localEquipmentId = (new ArrayList<>(mValues.keySet())).get(position);

        holder.mEquipmentDescription.setText(mDatabase.mEquipmentTypeList.get(localEquipmentId).description);
        holder.mEquipmentCode.setText(mDatabase.mEquipmentTypeList.get(localEquipmentId).code);
        localVideoLink = mValues.get(localEquipmentId);

        if (localVideoLink.length() > 0) {
            holder.mYouTubeFrame.setVisibility(View.VISIBLE);
            holder.mYouTubeThumbnail.initialize(holder.itemView.getContext().getResources().getString(R.string.YouTubeAPI_Key),
                    new YouTubeThumbnailView.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(localVideoLink);
                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(
                                    new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {

                                        @Override
                                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                            youTubeThumbnailLoader.release();
                                        }

                                        @Override
                                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                                        }
                                    });
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });

            holder.mYouTubePlayButton.setOnClickListener(v -> { mListener.onPlayVideo(localVideoLink); });
        }


        holder.mView.setOnClickListener(v -> {});
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mEquipmentDescription, mEquipmentCode;
        public final FrameLayout mYouTubeFrame;
        public final YouTubeThumbnailView mYouTubeThumbnail;
        public final ImageView mYouTubePlayButton;
        public int mIdExercise;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mEquipmentDescription = view.findViewById(R.id.equipment_description);
            mEquipmentCode = view.findViewById(R.id.equipment_code);
            mYouTubeFrame = view.findViewById(R.id.exercise_vid_frame);
            mYouTubeThumbnail = view.findViewById(R.id.exercise_vid_thumb);
            mYouTubePlayButton = view.findViewById(R.id.exercise_vid_play_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mEquipmentDescription.getText() + "'";
        }
    }
}
