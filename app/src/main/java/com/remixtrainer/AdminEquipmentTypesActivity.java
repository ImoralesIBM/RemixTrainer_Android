package com.remixtrainer;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class AdminEquipmentTypesActivity extends ToolbarActivityTemplate {

    Button mAddNewButton, mReturnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_equipment_types);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdminEquipmentItemFragment fragment = new AdminEquipmentItemFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.selected_equipment_type_list_placeholder, fragment);
        ft.commit();

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
