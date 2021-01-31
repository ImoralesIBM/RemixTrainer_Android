package com.remixtrainer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminMainActivity extends ToolbarActivityTemplate {

    private Intent nextIntent;

    private View mProgressView;
    private View mButtonPanelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.mAdminScreen = true;
        setContentView(R.layout.activity_admin_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mButtonPanelView = findViewById(R.id.pre_admin_main_form);
        mProgressView = findViewById(R.id.revert_progress);
    }

    public void onEditMuscleGroups(View v)
    {
        Intent nextIntent = new Intent(AdminMainActivity.this, AdminMuscleGroupsActivity.class);
        startActivity(nextIntent);
    }

    public void onEditEquipmentTypes(View v)
    {
        Intent nextIntent = new Intent(AdminMainActivity.this, AdminEquipmentTypesActivity.class);
        startActivity(nextIntent);
    }

    public void onEditExercises(View v)
    {
        Intent nextIntent = new Intent(AdminMainActivity.this, AdminExercisesActivity.class);
        startActivity(nextIntent);
    }

    public void onEditQuickWorkout(View v)
    {
        Intent nextIntent = new Intent(AdminMainActivity.this, AdminQuickWorkoutActivity.class);
        startActivity(nextIntent);
    }

    public void onRevert(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Revert to Defaults");
        builder.setMessage("You are about to revert to the default exercise database. All your customizations will be lost. This cannot be undone. Are you sure you want to proceed?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
                DatabaseLoadCompleteListener loadCompleteListener = () -> { showProgress(false); };

                showProgress(true);
                mDatabase.reloadUserData(loadCompleteListener, FirebaseAuth.getInstance().getCurrentUser().getUid());
            });

        builder.setNegativeButton("No", (dialog, which) -> {});

        builder.show();
    }

    public void onReturn(View v)
    {
        finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mButtonPanelView.setVisibility(show ? View.GONE : View.VISIBLE);
            mButtonPanelView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mButtonPanelView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mButtonPanelView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
