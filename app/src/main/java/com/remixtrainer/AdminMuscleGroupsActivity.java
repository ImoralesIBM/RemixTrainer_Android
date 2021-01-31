package com.remixtrainer;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import java.util.ArrayList;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminMuscleGroupsActivity extends ToolbarActivityTemplate {

    public RecyclerView mMuscleGroupList;
    public Button mAddNewButton, mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.mAdminScreen = true;
        setContentView(R.layout.activity_admin_muscle_group);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMuscleGroupList = findViewById(R.id.mg_list);
        mMuscleGroupList.setAdapter(new AdminMuscleGroupItemRecyclerViewAdapter(new ArrayList<>(mDatabase.mMuscleGroupList.keySet())));

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
