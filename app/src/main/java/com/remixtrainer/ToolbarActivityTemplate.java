package com.remixtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class ToolbarActivityTemplate extends AppCompatActivity {
    protected FirebaseAuth mBaseAuth;
    protected Boolean mAdminScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        menu.findItem(R.id.admin_item).setVisible(mDatabase.mIsAdmin);
        menu.findItem(R.id.retrieve_workout_item).setVisible(!mAdminScreen);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextIntent;
        final int logout_item_id = R.id.logout_item;

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.admin_item:
                nextIntent = new Intent(ToolbarActivityTemplate.this, AdminMainActivity.class);
                startActivity(nextIntent);

                return true;
            case R.id.retrieve_workout_item:
                nextIntent = new Intent(ToolbarActivityTemplate.this, RetrieveSavedWorkoutActivity.class);
                startActivity(nextIntent);

                return true;
            case R.id.logout_item:
                mBaseAuth.signOut();

                nextIntent = new Intent(ToolbarActivityTemplate.this, PreLoginActivity.class);
                startActivity(nextIntent);
                finish();

                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
