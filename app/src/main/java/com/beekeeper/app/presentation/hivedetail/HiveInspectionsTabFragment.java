package com.beekeeper.app.presentation.hivedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.beekeeper.app.databinding.TabHiveListBinding;
import com.beekeeper.app.presentation.inspection.InspectionAdapter;
import com.beekeeper.app.presentation.inspection.InspectionViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HiveInspectionsTabFragment extends Fragment {

    private static final String ARG_HIVE_ID = "hive_id";

    private TabHiveListBinding binding;
    private String hiveId;
    private InspectionViewModel viewModel;
    private InspectionAdapter adapter;

    public static HiveInspectionsTabFragment newInstance(String hiveId) {
        HiveInspectionsTabFragment fragment = new HiveInspectionsTabFragment();
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
        binding = TabHiveListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InspectionViewModel.class);

        setupRecyclerView();
        observeData();
        loadData();
    }

    private void setupRecyclerView() {
        adapter = new InspectionAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnInspectionClickListener(new InspectionAdapter.OnInspectionClickListener() {
            @Override
            public void onInspectionClick(com.beekeeper.app.data.local.entity.Inspection inspection) {
                // TODO: Show inspection detail
            }

            @Override
            public void onInspectionEdit(com.beekeeper.app.data.local.entity.Inspection inspection) {
                // TODO: Navigate to edit form
            }

            @Override
            public void onInspectionDelete(com.beekeeper.app.data.local.entity.Inspection inspection) {
                showDeleteConfirmation(inspection);
            }
        });
    }

    private void observeData() {
        viewModel.getInspections().observe(getViewLifecycleOwner(), inspections -> {
            adapter.submitList(inspections);
            binding.textEmptyState.setVisibility(
                inspections == null || inspections.isEmpty() ? View.VISIBLE : View.GONE
            );
            binding.recyclerView.setVisibility(
                inspections != null && !inspections.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });
    }

    private void loadData() {
        if (hiveId != null) {
            viewModel.loadInspectionsByHiveId(hiveId);
        }
    }

    private void showDeleteConfirmation(com.beekeeper.app.data.local.entity.Inspection inspection) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Zmazať prehliadku?")
            .setMessage("Naozaj chcete zmazať túto prehliadku?")
            .setPositiveButton("Zmazať", (dialog, which) -> {
                viewModel.deleteInspection(inspection);
            })
            .setNegativeButton("Zrušiť", null)
            .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
