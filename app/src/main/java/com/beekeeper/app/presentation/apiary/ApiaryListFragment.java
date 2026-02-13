package com.beekeeper.app.presentation.apiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.databinding.FragmentApiaryListBinding;
import com.beekeeper.app.presentation.base.BaseFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ApiaryListFragment extends BaseFragment<FragmentApiaryListBinding> {

    private ApiaryViewModel viewModel;
    private ApiaryAdapter adapter;

    @Override
    protected FragmentApiaryListBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentApiaryListBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        viewModel = new ViewModelProvider(this).get(ApiaryViewModel.class);

        // Setup RecyclerView
        adapter = new ApiaryAdapter();
        binding.recyclerApiaries.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerApiaries.setAdapter(adapter);

        // Setup adapter click listeners
        adapter.setOnApiaryClickListener(new ApiaryAdapter.OnApiaryClickListener() {
            @Override
            public void onApiaryClick(Apiary apiary) {
                // TODO: Navigate to hive list for this apiary
                showToast("Otvorenie včelnice: " + apiary.getName());
            }

            @Override
            public void onApiaryEdit(Apiary apiary) {
                showEditDialog(apiary);
            }

            @Override
            public void onApiaryDelete(Apiary apiary) {
                showDeleteConfirmation(apiary);
            }
        });

        // Setup FAB
        binding.fabAddApiary.setOnClickListener(v -> showCreateDialog());

        // Load data
        viewModel.loadApiaries();
    }

    @Override
    protected void observeData() {
        viewModel.getApiaries().observe(getViewLifecycleOwner(), apiaries -> {
            adapter.submitList(apiaries);
            binding.textEmptyState.setVisibility(
                apiaries == null || apiaries.isEmpty() ? View.VISIBLE : View.GONE
            );
            binding.recyclerApiaries.setVisibility(
                apiaries != null && !apiaries.isEmpty() ? View.VISIBLE : View.GONE
            );
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                showToast(error);
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && !success.isEmpty()) {
                showToast(success);
            }
        });
    }

    private void showCreateDialog() {
        ApiaryDialogFragment dialog = ApiaryDialogFragment.newInstance(
            ApiaryDialogFragment.MODE_CREATE, null
        );
        dialog.setOnApiaryDialogListener(new ApiaryDialogFragment.OnApiaryDialogListener() {
            @Override
            public void onApiaryCreate(String name, String location) {
                viewModel.createApiary(name, location, 0, 0);
            }

            @Override
            public void onApiaryUpdate(Apiary apiary) {
                // Not used in create mode
            }
        });
        dialog.show(getParentFragmentManager(), "create_apiary");
    }

    private void showEditDialog(Apiary apiary) {
        ApiaryDialogFragment dialog = ApiaryDialogFragment.newInstance(
            ApiaryDialogFragment.MODE_EDIT, apiary
        );
        dialog.setOnApiaryDialogListener(new ApiaryDialogFragment.OnApiaryDialogListener() {
            @Override
            public void onApiaryCreate(String name, String location) {
                // Not used in edit mode
            }

            @Override
            public void onApiaryUpdate(Apiary apiary) {
                viewModel.updateApiary(apiary);
            }
        });
        dialog.show(getParentFragmentManager(), "edit_apiary");
    }

    private void showDeleteConfirmation(Apiary apiary) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Zmazať včelnicu?")
            .setMessage("Naozaj chcete zmazať včelnicu \"" + apiary.getName() + "\"? " +
                       "Všetky úle a dáta v tejto včelnici budú taktiež zmazané.")
            .setPositiveButton("Zmazať", (dialog, which) -> {
                viewModel.deleteApiary(apiary);
            })
            .setNegativeButton("Zrušiť", null)
            .show();
    }
}
