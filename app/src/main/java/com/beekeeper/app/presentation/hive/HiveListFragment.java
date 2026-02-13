package com.beekeeper.app.presentation.hive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.databinding.FragmentHiveListBinding;
import com.beekeeper.app.presentation.base.BaseFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HiveListFragment extends BaseFragment<FragmentHiveListBinding> {

    private static final String ARG_APIARY_ID = "apiary_id";
    private static final String ARG_APIARY_NAME = "apiary_name";

    private HiveViewModel viewModel;
    private HiveAdapter adapter;
    private String apiaryId;
    private String apiaryName;

    public static HiveListFragment newInstance(String apiaryId, String apiaryName) {
        HiveListFragment fragment = new HiveListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_APIARY_ID, apiaryId);
        args.putString(ARG_APIARY_NAME, apiaryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apiaryId = getArguments().getString(ARG_APIARY_ID);
            apiaryName = getArguments().getString(ARG_APIARY_NAME);
        }
    }

    @Override
    protected FragmentHiveListBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHiveListBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        if (apiaryName != null) {
            getActivity().setTitle(apiaryName);
        }

        viewModel = new ViewModelProvider(this).get(HiveViewModel.class);

        // Setup RecyclerView
        adapter = new HiveAdapter();
        binding.recyclerHives.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerHives.setAdapter(adapter);

        // Setup adapter click listeners
        adapter.setOnHiveClickListener(new HiveAdapter.OnHiveClickListener() {
            @Override
            public void onHiveClick(Hive hive) {
                // TODO: Navigate to hive detail
                showToast("Detail úľa: " + hive.getName());
            }

            @Override
            public void onHiveEdit(Hive hive) {
                showEditDialog(hive);
            }

            @Override
            public void onHiveDelete(Hive hive) {
                showDeleteConfirmation(hive);
            }
        });

        // Setup FAB
        binding.fabAddHive.setOnClickListener(v -> showCreateDialog());

        // Load data
        if (apiaryId != null) {
            viewModel.loadHivesByApiaryId(apiaryId);
        }
    }

    @Override
    protected void observeData() {
        viewModel.getHives().observe(getViewLifecycleOwner(), hives -> {
            adapter.submitList(hives);
            binding.textEmptyState.setVisibility(
                hives == null || hives.isEmpty() ? View.VISIBLE : View.GONE
            );
            binding.recyclerHives.setVisibility(
                hives != null && !hives.isEmpty() ? View.VISIBLE : View.GONE
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
        HiveDialogFragment dialog = HiveDialogFragment.newInstance(
            HiveDialogFragment.MODE_CREATE, apiaryId, null
        );
        dialog.setOnHiveDialogListener(new HiveDialogFragment.OnHiveDialogListener() {
            @Override
            public void onHiveCreate(String name, String type, String queenId, int queenYear) {
                viewModel.createHive(apiaryId, name, type, queenId, queenYear);
            }

            @Override
            public void onHiveUpdate(Hive hive) {
                // Not used in create mode
            }
        });
        dialog.show(getParentFragmentManager(), "create_hive");
    }

    private void showEditDialog(Hive hive) {
        HiveDialogFragment dialog = HiveDialogFragment.newInstance(
            HiveDialogFragment.MODE_EDIT, apiaryId, hive
        );
        dialog.setOnHiveDialogListener(new HiveDialogFragment.OnHiveDialogListener() {
            @Override
            public void onHiveCreate(String name, String type, String queenId, int queenYear) {
                // Not used in edit mode
            }

            @Override
            public void onHiveUpdate(Hive hive) {
                viewModel.updateHive(hive);
            }
        });
        dialog.show(getParentFragmentManager(), "edit_hive");
    }

    private void showDeleteConfirmation(Hive hive) {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Zmazať úľ?")
            .setMessage("Naozaj chcete zmazať úľ \"" + hive.getName() + "\"? " +
                       "Všetky prehliadky a dáta v tomto úle budú taktiež zmazané.")
            .setPositiveButton("Zmazať", (dialog, which) -> {
                viewModel.deleteHive(hive);
            })
            .setNegativeButton("Zrušiť", null)
            .show();
    }
}
