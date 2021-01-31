package com.remixtrainer;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.remixtrainer.RemixTrainerApplication.mDatabase;


public class AdminExercisesActivity extends ToolbarActivityTemplate implements AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener {

    private EditText mExerciseNameQuery;
    private Button mSetFiltersButton, mClearFiltersButton;
    private RecyclerView mExerciseList;
    private Button mAddNewButton, mReturnButton;

    private final MutableLiveData<String> exerciseQuery = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Boolean>> muscleGroupSelections =
            new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Boolean>> equipmentTypeSelections =
            new MutableLiveData<>();

    // Tick counter that'll be incremented every time a filter value is changed
    private final MediatorLiveData<Integer> changeMonitor = new MediatorLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.mAdminScreen = true;
        setContentView(R.layout.activity_admin_exercises);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExerciseNameQuery = findViewById(R.id.exercise_name_query);
        mExerciseNameQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                exerciseQuery.setValue(s.toString());
            }
        });

        onClearFilters();

        changeMonitor.setValue(0);
        changeMonitor.addSource(exerciseQuery, notUsed -> {
                changeMonitor.setValue(changeMonitor.getValue()+1);
            });
        changeMonitor.addSource(muscleGroupSelections, notUsed -> {
                changeMonitor.setValue(changeMonitor.getValue()+1);
            });
        changeMonitor.addSource(equipmentTypeSelections, new Observer<Map<Integer, Boolean>>() {
            @Override
            public void onChanged(Map<Integer, Boolean> map) {
                changeMonitor.setValue(changeMonitor.getValue()+1);
            }
        });

        mExerciseList = (RecyclerView) findViewById(R.id.selected_exercise_list);
        changeMonitor.observe(this, notUsed -> {
            mExerciseList.setAdapter(
                    new AdminExerciseItemRecyclerViewAdapter(mDatabase.mExerciseTypeList
                            .values().stream().filter(ex -> ex.getDescription().toLowerCase()
                                    .contains(exerciseQuery.getValue().toLowerCase()))
                            .filter(ex -> ex.getMuscleGroups().stream().anyMatch(gr -> muscleGroupSelections.getValue().getOrDefault(gr, false)))
                            .filter(ex -> ex.getEquipmentTypesOnly().stream().anyMatch(eq -> equipmentTypeSelections.getValue().getOrDefault(eq, false)))
                            .map(ex -> ex.getId())
                            .collect(Collectors.toList()), new AdminExerciseEquipmentTypeViewItemFragment.OnListFragmentInteractionListener() {
                                                                @Override
                                                                public void onPlayVideo(String videoLink) {
                                                                    onPlayVideo(videoLink);
                                                                }
                                                            }));
        });

        mSetFiltersButton = (Button) findViewById(R.id.set_filters_button);
        mSetFiltersButton.setOnClickListener(v -> {
            FragmentManager fm = getSupportFragmentManager();
            AdminSetExerciseFiltersDialogFragment filterDialog =
                    AdminSetExerciseFiltersDialogFragment
                            .newInstance(muscleGroupSelections.getValue(),
                                    equipmentTypeSelections.getValue(),
                                    new AdminFilterMuscleGroupItemFragment.OnListFragmentInteractionListener() {
                                        @Override
                                        public void onMuscleGroupItemSelected(int itemId, boolean isChecked) {
                                            Map<Integer, Boolean> tempMap = new ArrayMap<>();
                                            tempMap.putAll(muscleGroupSelections.getValue());
                                            tempMap.put(itemId, isChecked);
                                            muscleGroupSelections.setValue(tempMap);
                                        }
                                    },
                                    new AdminFilterEquipmentTypeItemFragment.OnListFragmentInteractionListener() {
                                        @Override
                                        public void onEquipmentTypeItemSelected(int itemId, boolean isChecked) {
                                            Map<Integer, Boolean> tempMap = new ArrayMap<>();
                                            tempMap.putAll(equipmentTypeSelections.getValue());
                                            tempMap.put(itemId, isChecked);
                                            equipmentTypeSelections.setValue(tempMap);
                                        }
                                    });
            filterDialog.show(fm, "fragment_select_filters");
        });

        mClearFiltersButton = (Button) findViewById(R.id.clear_filters_button);
        mClearFiltersButton.setOnClickListener(v -> {
            onClearFilters();
        });

        mAddNewButton = (Button) findViewById(R.id.add_button);
        mAddNewButton.setOnClickListener(v -> {
                FragmentManager fm = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                AdminEditExerciseDialogFragment editorDialog = AdminEditExerciseDialogFragment.newInstance(-1);
                editorDialog.show(fm, "fragment_admin_edit_exercise_dialog");
            });

        mReturnButton = findViewById(R.id.return_button);
        mReturnButton.setOnClickListener(v -> { finish(); });
    }

    public void onClearFilters() {
        mExerciseNameQuery.setText("");
        muscleGroupSelections.setValue(mDatabase.mMuscleGroupList.keySet()
                .stream().collect(Collectors.toMap(Function.identity(), notused -> true)));
        equipmentTypeSelections.setValue(mDatabase.mEquipmentTypeList.keySet()
                .stream().collect(Collectors.toMap(Function.identity(), notused -> true)));
    }

    public void onPlayVideo(String videoId) {
        // The user requests to play a video

        FragmentManager fm = getSupportFragmentManager();
        YouTubeVideoDialogFragment playerDialog = YouTubeVideoDialogFragment.newInstance(videoId);
        playerDialog.show(fm, "fragment_you_tube_video");
    }
}
