package com.remixtrainer;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import java.util.ArrayList;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class AdminEquipmentTypesActivity extends ToolbarActivityTemplate {

    private RecyclerView mEquipmentTypeList;
    private Button mAddNewButton, mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.mAdminScreen = true;
        setContentView(R.layout.activity_admin_equipment_types);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEquipmentTypeList = findViewById(R.id.eq_list);
        mEquipmentTypeList.setAdapter(new AdminEquipmentItemRecyclerViewAdapter(
                        new ArrayList<>(mDatabase.mEquipmentTypeList.keySet())
        ));

        mAddNewButton = findViewById(R.id.add_button);
        mAddNewButton.setOnClickListener(v -> {
                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                AdminEquipmentTypeDialogFragment editorDialog = AdminEquipmentTypeDialogFragment.newInstance(-1);
                editorDialog.show(fm, "fragment_admin_equipment_type_dialog");
            });

        mReturnButton = findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(v -> { finish(); });
    }
}
