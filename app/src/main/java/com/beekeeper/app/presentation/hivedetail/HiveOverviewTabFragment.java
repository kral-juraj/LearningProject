package com.beekeeper.app.presentation.hivedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beekeeper.app.databinding.TabHiveOverviewBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HiveOverviewTabFragment extends Fragment {

    private static final String ARG_HIVE_ID = "hive_id";

    private TabHiveOverviewBinding binding;
    private String hiveId;

    public static HiveOverviewTabFragment newInstance(String hiveId) {
        HiveOverviewTabFragment fragment = new HiveOverviewTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HIVE_ID, hiveId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hiveId = getArguments().getString(ARG_HIVE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabHiveOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadOverviewData();
    }

    private void loadOverviewData() {
        // TODO: Load actual data from database
        // For now, show placeholder data

        // Last inspection
        SimpleDateFormat dateFormat = new SimpleDateFormat("d. MMMM yyyy", new Locale("sk"));
        binding.textLastInspectionDate.setText(dateFormat.format(new Date()));
        binding.textLastStrength.setText("7/10");
        binding.textLastBrood.setText("8");
        binding.textLastTemperature.setText("22°C");

        // Statistics
        binding.textInspectionCount.setText("0");
        binding.textFeedingCount.setText("0");
        binding.textTaxationCount.setText("0");

        // Hive notes
        binding.textHiveNotes.setText("Žiadne poznámky");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
