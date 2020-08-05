package com.remixtrainer;

import androidx.core.content.res.ResourcesCompat;
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
import com.remixtrainer.AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminExerciseEquipmentTypeViewItemRecyclerViewAdapter extends RecyclerView.Adapter<AdminExerciseEquipmentTypeViewItemRecyclerViewAdapter.ViewHolder> {

    private final Map<Integer, Boolean> mValues;
    private final Map<Integer, String> mVideoLinks;

    private final OnListFragmentInteractionListener mListener;

    public AdminExerciseEquipmentTypeViewItemRecyclerViewAdapter(Map<Integer, Boolean> items, Map<Integer, String> videos, OnListFragmentInteractionListener listener) {
        mValues = new ArrayMap<>();
        mValues.putAll(items);

        mVideoLinks = new ArrayMap<>();
        mVideoLinks.putAll(videos);

        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_admin_exercise_equipment_type_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdEquipment = (new ArrayList<>(mValues.keySet())).get(position);
        holder.mContentView.setText(mDatabase.mEquipmentTypeList.get(holder.mIdEquipment).description);
        if (mValues.get(holder.mIdEquipment)) {
            holder.mContentView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ResourcesCompat.getDrawable(holder.mView.getContext().getResources(),
                            R.drawable.ic_checkmark, holder.mView.getContext().getTheme()), null,
                    null, null);
        }
        holder.mContentCode.setText("("+mDatabase.mEquipmentTypeList.get(holder.mIdEquipment).code+")");

        if (mValues.containsKey(holder.mIdEquipment)) {
            holder.mYouTubeFrame.setVisibility(View.VISIBLE);
            holder.mYouTubeThumbnail.initialize(holder.itemView.getContext().getResources().getString(R.string.YouTubeAPI_Key),
                    new YouTubeThumbnailView.OnInitializedListener() {

                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(mVideoLinks.get(holder.mIdEquipment));
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

            holder.mYouTubePlayButton.setOnClickListener(v -> { mListener.onPlayVideo(mVideoLinks.get(holder.mIdEquipment)); });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mContentView;
        public final TextView mContentCode;

        public final FrameLayout mYouTubeFrame;
        public final YouTubeThumbnailView mYouTubeThumbnail;
        public final ImageView mYouTubePlayButton;

        public int mIdEquipment;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mContentView = view.findViewById(R.id.equipment_name);
            mContentCode = view.findViewById(R.id.equipment_code);

            mYouTubeFrame = view.findViewById(R.id.exercise_vid_frame);
            mYouTubeThumbnail = view.findViewById(R.id.exercise_vid_thumb);
            mYouTubePlayButton = view.findViewById(R.id.exercise_vid_play_btn);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
