package com.remixtrainer;

import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminExercisesActivity extends ToolbarActivityTemplate implements AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener {

    private AdminEditExerciseViewModel mViewModel;
    Button mAddNewButton, mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_exercises);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewModel = ViewModelProviders.of(this).get(AdminEditExerciseViewModel.class);

        AdminExerciseItemFragment fragment = new AdminExerciseItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.selected_exercise_list_placeholder, fragment);
        ft.commit();

        mAddNewButton = findViewById(R.id.add_button);
        mAddNewButton.setOnClickListener(v -> {
                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                AdminEditExerciseDialogFragment editorDialog = AdminEditExerciseDialogFragment.newInstance(-1);
                editorDialog.show(fm, "fragment_admin_edit_exercise_dialog");
            });

        mReturnButton = findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(v -> { finish(); });
    }

    public void onPlayVideo(String videoId) {
        // The user requests to play a video

        FragmentManager fm = getSupportFragmentManager();
        YouTubeVideoDialogFragment playerDialog = YouTubeVideoDialogFragment.newInstance(videoId);
        playerDialog.show(fm, "fragment_you_tube_video");
    }
}
