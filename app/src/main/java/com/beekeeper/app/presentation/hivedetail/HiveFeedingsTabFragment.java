package com.beekeeper.app.presentation.hivedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.beekeeper.app.databinding.TabHiveListBinding;

public class HiveFeedingsTabFragment extends Fragment {

    private static final String ARG_HIVE_ID = "hive_id";

    private TabHiveListBinding binding;
    private String hiveId;

    public static HiveFeedingsTabFragment newInstance(String hiveId) {
        HiveFeedingsTabFragment fragment = new HiveFeedingsTabFragment();
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

        // Setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show empty state
        binding.textEmptyState.setText("Žiadne krmenia");
        binding.textEmptyState.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);

        // TODO: Implement FeedingAdapter and ViewModel
        // TODO: Load feeding data from database
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
