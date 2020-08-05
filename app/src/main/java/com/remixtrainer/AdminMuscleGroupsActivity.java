package com.remixtrainer;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminMuscleGroupsActivity extends ToolbarActivityTemplate {

    public Button mAddNewButton, mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_muscle_group);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdminMuscleGroupItemFragment fragment = new AdminMuscleGroupItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.selected_muscle_group_list_placeholder, fragment);
        ft.commit();

        mAddNewButton = findViewById(R.id.add_button);
        mAddNewButton.setOnClickListener(v -> {
                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                AdminMuscleGroupDialogFragment editorDialog = AdminMuscleGroupDialogFragment.newInstance(-1);
                editorDialog.show(fm, "fragment_admin_muscle_group_dialog");
            });

        mReturnButton = findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(v -> {
                finish();
            });
    }
}
