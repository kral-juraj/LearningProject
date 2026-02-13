package com.beekeeper.app.presentation.feeding;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;

import com.beekeeper.app.R;
import com.beekeeper.app.databinding.FragmentFeedingEntryBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FeedingEntryFragment extends BaseFragment<FragmentFeedingEntryBinding> {

    private static final String ARG_HIVE_ID = "hive_id";
    private static final String ARG_HIVE_NAME = "hive_name";

    private FeedingViewModel viewModel;
    private String hiveId;
    private String hiveName;

    private Calendar selectedDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));

    private String[] feedTypes = {"Sirup 1:1", "Sirup 3:2", "Fondant", "Peľový koláč"};
    private String[] feedTypeValues = {"SYRUP_1_1", "SYRUP_3_2", "FONDANT", "POLLEN_PATTY"};

    public static FeedingEntryFragment newInstance(String hiveId, String hiveName) {
        FeedingEntryFragment fragment = new FeedingEntryFragment();
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
        selectedDate = Calendar.getInstance();
    }

    @Override
    protected FragmentFeedingEntryBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFeedingEntryBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        if (hiveName != null) {
            getActivity().setTitle("Krmenie - " + hiveName);
        }

        viewModel = new ViewModelProvider(this).get(FeedingViewModel.class);

        // Initialize date display
        updateDateDisplay();

        // Date picker
        binding.buttonSelectDate.setOnClickListener(v -> showDatePicker());

        // Setup feed type spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            feedTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFeedType.setAdapter(adapter);

        // Weight difference calculator
        setupWeightDifferenceCalculator();

        // Save button
        binding.buttonSave.setOnClickListener(v -> saveFeeding());
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
                // Navigate back
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
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateDisplay();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateDateDisplay() {
        String dateStr = dateFormat.format(selectedDate.getTime());
        binding.textSelectedDate.setText(dateStr);
    }

    private void setupWeightDifferenceCalculator() {
        TextWatcher weightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateWeightDifference();
            }
        };

        binding.inputWeightBefore.addTextChangedListener(weightWatcher);
        binding.inputWeightAfter.addTextChangedListener(weightWatcher);
    }

    private void calculateWeightDifference() {
        String beforeStr = binding.inputWeightBefore.getText().toString().trim();
        String afterStr = binding.inputWeightAfter.getText().toString().trim();

        if (!beforeStr.isEmpty() && !afterStr.isEmpty()) {
            try {
                double before = Double.parseDouble(beforeStr);
                double after = Double.parseDouble(afterStr);
                double diff = after - before;

                String sign = diff >= 0 ? "+" : "";
                binding.textWeightDifference.setText(
                    String.format(Locale.getDefault(), "Rozdiel: %s%.1f kg", sign, diff)
                );
                binding.textWeightDifference.setVisibility(View.VISIBLE);

                // Color based on positive/negative
                if (diff > 0) {
                    binding.textWeightDifference.setTextColor(getResources().getColor(R.color.success, null));
                } else if (diff < 0) {
                    binding.textWeightDifference.setTextColor(getResources().getColor(R.color.error, null));
                } else {
                    binding.textWeightDifference.setTextColor(getResources().getColor(R.color.text_secondary, null));
                }
            } catch (NumberFormatException e) {
                binding.textWeightDifference.setVisibility(View.GONE);
            }
        } else {
            binding.textWeightDifference.setVisibility(View.GONE);
        }
    }

    private void saveFeeding() {
        // Get selected feed type
        int selectedPosition = binding.spinnerFeedType.getSelectedItemPosition();
        String feedType = feedTypeValues[selectedPosition];

        // Parse numeric values
        String weightBeforeStr = binding.inputWeightBefore.getText().toString().trim();
        String weightAfterStr = binding.inputWeightAfter.getText().toString().trim();
        String amountStr = binding.inputAmount.getText().toString().trim();

        double weightBefore = 0.0;
        if (!weightBeforeStr.isEmpty()) {
            try {
                weightBefore = Double.parseDouble(weightBeforeStr);
            } catch (NumberFormatException e) {
                showToast("Neplatná hmotnosť pred krmením");
                return;
            }
        }

        double weightAfter = 0.0;
        if (!weightAfterStr.isEmpty()) {
            try {
                weightAfter = Double.parseDouble(weightAfterStr);
            } catch (NumberFormatException e) {
                showToast("Neplatná hmotnosť po krmení");
                return;
            }
        }

        double amount = 0.0;
        if (!amountStr.isEmpty()) {
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                showToast("Neplatné množstvo");
                return;
            }
        }

        String notes = binding.inputNotes.getText().toString().trim();

        // Create feeding
        viewModel.createFeeding(
            hiveId,
            selectedDate.getTimeInMillis(),
            weightBefore,
            weightAfter,
            feedType,
            amount,
            notes
        );
    }
}
