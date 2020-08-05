package com.remixtrainer;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;

public class SelectGroupExerciseActivity extends ToolbarActivityTemplate
    implements SelectEquipmentItemFragment.OnListFragmentInteractionListener, SelectMuscleGroupItemFragment.OnListFragmentInteractionListener
{
    private ImageButton mEquipmentTypeRing, mMuscleGroupRing;
    private Button mNextButton;

    private List<Boolean> mMuscleGroupSelectedList = new ArrayList<>();
    private List<Boolean> mEquipmentTypesSelectedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEquipmentTypeRing = findViewById(R.id.equipment_type_selector_ring);
        mEquipmentTypeRing.setOnClickListener(v -> {
                FragmentManager fm = getSupportFragmentManager();
                SelectEquipmentDialogFragment selectorDialog = SelectEquipmentDialogFragment.newInstance(mEquipmentTypesSelectedList);
                selectorDialog.show(fm, "fragment_select_equipment_type");
            });

        mMuscleGroupRing = findViewById(R.id.muscle_group_selector_ring);
        mMuscleGroupRing.setOnClickListener(v -> {
                FragmentManager fm = getSupportFragmentManager();
                SelectMuscleGroupDialogFragment selectorDialog = SelectMuscleGroupDialogFragment.newInstance(mMuscleGroupSelectedList);
                selectorDialog.show(fm, "fragment_select_muscle_group");
            });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setEnabled(false);
        mNextButton.setOnClickListener(v -> {
                boolean cancel = false;
                View focusView = null;

                Bundle optionValues = getIntent().getExtras();
                Intent nextIntent = new Intent(SelectGroupExerciseActivity.this, PreliminaryWorkout.class);

                optionValues.putIntegerArrayList("equipmentTypes",
                        IntStream.range(0, mEquipmentTypesSelectedList.size())
                                .filter(i -> mEquipmentTypesSelectedList.get(i))
                                .map(i -> new ArrayList<>(mDatabase.mEquipmentTypeList.keySet()).get(i)).boxed()
                                .collect(Collectors.toCollection(ArrayList::new)));
                optionValues.putIntegerArrayList("muscleGroups",
                        IntStream.range(0, mMuscleGroupSelectedList.size())
                                .filter(i -> mMuscleGroupSelectedList.get(i))
                                .map(i -> new ArrayList<>(mDatabase.mMuscleGroupList.keySet()).get(i)).boxed()
                                .collect(Collectors.toCollection(ArrayList::new)));

                nextIntent.putExtras(optionValues);

                startActivity(nextIntent);
                finish();
            });
        mNextButton.setOnLongClickListener(v -> { return true; });
    }

    public void onEquipmentItemSelected(int position, boolean isChecked) {
        // The user selected/unselected an equipment type item

        if (mEquipmentTypesSelectedList.size() <= position) {
            for (int i = mEquipmentTypesSelectedList.size(); i <= position; i++) {
                mEquipmentTypesSelectedList.add(false);
            }
        }
        mEquipmentTypesSelectedList.set(position, isChecked);

        mEquipmentTypeRing.setActivated(mEquipmentTypesSelectedList.stream().anyMatch(x -> x));

        onSelectionsUpdated();
    }

    public void onMuscleGroupItemSelected(int position, boolean isChecked) {
        // The user selected/unselected a muscle group

        if (mMuscleGroupSelectedList.size() <= position) {
            for (int i = mMuscleGroupSelectedList.size(); i <= position; i++) {
                mMuscleGroupSelectedList.add(false);
            }
        }

        mMuscleGroupSelectedList.set(position, isChecked);

        mMuscleGroupRing.setActivated(mMuscleGroupSelectedList.stream().anyMatch(x -> x));

        onSelectionsUpdated();
    }

    private void onSelectionsUpdated()
    {
        mNextButton.setEnabled(mEquipmentTypeRing.isActivated() && mMuscleGroupRing.isActivated());
    }
}
