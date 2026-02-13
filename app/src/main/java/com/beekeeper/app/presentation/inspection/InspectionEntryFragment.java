package com.beekeeper.app.presentation.inspection;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.lifecycle.ViewModelProvider;

import com.beekeeper.app.databinding.FragmentInspectionEntryBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InspectionEntryFragment extends BaseFragment<FragmentInspectionEntryBinding> {

    private static final String ARG_HIVE_ID = "hive_id";
    private static final String ARG_HIVE_NAME = "hive_name";

    private InspectionViewModel viewModel;
    private String hiveId;
    private String hiveName;

    private Calendar selectedDateTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public static InspectionEntryFragment newInstance(String hiveId, String hiveName) {
        InspectionEntryFragment fragment = new InspectionEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HIVE_ID, hiveId);
        args.putString(ARG_HIVE_NAME, hiveName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hiveId = getArguments().getString(ARG_HIVE_ID);
            hiveName = getArguments().getString(ARG_HIVE_NAME);
        }
        selectedDateTime = Calendar.getInstance();
    }

    @Override
    protected FragmentInspectionEntryBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentInspectionEntryBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        if (hiveName != null) {
            getActivity().setTitle("Prehliadka - " + hiveName);
        }

        viewModel = new ViewModelProvider(this).get(InspectionViewModel.class);

        // Initialize datetime display
        updateDateTimeDisplay();

        // Date picker
        binding.buttonSelectDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        binding.buttonSelectTime.setOnClickListener(v -> showTimePicker());

        // Strength SeekBar
        binding.seekbarStrength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textStrengthValue.setText("Hodnota: " + progress + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Aggression SeekBar
        binding.seekbarAggression.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textAggressionValue.setText("Hodnota: " + progress + "/5");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Varroa checkbox - show/hide count input
        binding.checkboxVarroa.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.layoutVarroaCount.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Save button
        binding.buttonSave.setOnClickListener(v -> saveInspection());
    }

    @Override
    protected void observeData() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.buttonSave.setEnabled(!loading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && !success.isEmpty()) {
                showToast(success);
                // Navigate back or clear form
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, month);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTimeDisplay();
            },
            selectedDateTime.get(Calendar.YEAR),
            selectedDateTime.get(Calendar.MONTH),
            selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            requireContext(),
            (view, hourOfDay, minute) -> {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);
                updateDateTimeDisplay();
            },
            selectedDateTime.get(Calendar.HOUR_OF_DAY),
            selectedDateTime.get(Calendar.MINUTE),
            true
        );
        timePickerDialog.show();
    }

    private void updateDateTimeDisplay() {
        String dateStr = dateFormat.format(selectedDateTime.getTime());
        String timeStr = timeFormat.format(selectedDateTime.getTime());
        binding.textSelectedDatetime.setText(dateStr + " o " + timeStr);
    }

    private void saveInspection() {
        // Validate and collect data
        String temperatureStr = binding.inputTemperature.getText().toString().trim();
        String foodStoresStr = binding.inputFoodStores.getText().toString().trim();
        String broodFramesStr = binding.inputBroodFrames.getText().toString().trim();
        String cappedBroodStr = binding.inputCappedBrood.getText().toString().trim();
        String uncappedBroodStr = binding.inputUncappedBrood.getText().toString().trim();
        String pollenFramesStr = binding.inputPollenFrames.getText().toString().trim();
        String totalFramesStr = binding.inputTotalFrames.getText().toString().trim();
        String varroaCountStr = binding.inputVarroaCount.getText().toString().trim();

        String queenNote = binding.inputQueenNote.getText().toString().trim();
        String behavior = binding.inputBehavior.getText().toString().trim();
        String notes = binding.inputNotes.getText().toString().trim();

        // Parse numeric values
        double temperature = 0.0;
        if (!temperatureStr.isEmpty()) {
            try {
                temperature = Double.parseDouble(temperatureStr);
            } catch (NumberFormatException e) {
                showToast("Neplatná teplota");
                return;
            }
        }

        double foodStores = 0.0;
        if (!foodStoresStr.isEmpty()) {
            try {
                foodStores = Double.parseDouble(foodStoresStr);
            } catch (NumberFormatException e) {
                showToast("Neplatná hodnota zásob");
                return;
            }
        }

        int broodFrames = parseIntOrZero(broodFramesStr);
        int cappedBrood = parseIntOrZero(cappedBroodStr);
        int uncappedBrood = parseIntOrZero(uncappedBroodStr);
        int pollenFrames = parseIntOrZero(pollenFramesStr);
        int totalFrames = parseIntOrZero(totalFramesStr);

        int varroaCount = 0;
        if (binding.checkboxVarroa.isChecked()) {
            varroaCount = parseIntOrZero(varroaCountStr);
        }

        int strengthEstimate = binding.seekbarStrength.getProgress();
        int aggression = binding.seekbarAggression.getProgress();
        boolean queenSeen = binding.checkboxQueenSeen.isChecked();
        boolean varroa = binding.checkboxVarroa.isChecked();

        // Create inspection
        viewModel.createInspection(
            hiveId,
            selectedDateTime.getTimeInMillis(),
            temperature,
            strengthEstimate,
            foodStores,
            broodFrames,
            cappedBrood,
            uncappedBrood,
            pollenFrames,
            totalFrames,
            queenSeen,
            queenNote,
            varroa,
            varroaCount,
            aggression,
            behavior,
            notes
        );
    }

    private int parseIntOrZero(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
