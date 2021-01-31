package com.remixtrainer;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class YouTubeVideoDialogFragment extends DialogFragment implements YouTubePlayer.OnInitializedListener
{
    private ImageView mCloseButton;

    private YouTubePlayerSupportFragment f;
    public String mVideoId;

    public YouTubeVideoDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment YouTubeVideoDialogFragment.
     */
    public static YouTubeVideoDialogFragment newInstance(String videoId) {
        YouTubeVideoDialogFragment fragment = new YouTubeVideoDialogFragment();
        fragment.mVideoId = videoId;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_you_tube_video_dialog, container, false);
        YouTubePlayerSupportFragment youTubePlayerFragmentHandle;

        youTubePlayerFragmentHandle = new YouTubePlayerSupportFragment();
        youTubePlayerFragmentHandle.initialize(v.getContext().getResources().getString(R.string.YouTubeAPI_Key), this);
        getChildFragmentManager().beginTransaction().replace(R.id.youtube_player_placeholder,
                youTubePlayerFragmentHandle).commit();

        return v;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored)
    {
        youTubePlayer.loadVideo(mVideoId);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult result)
    {
        if (result.isUserRecoverableError()) {
            result.getErrorDialog(this.getActivity(),1).show();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCloseButton = getDialog().findViewById(R.id.close_btn);
        mCloseButton.setOnClickListener(v -> { dismiss(); });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}