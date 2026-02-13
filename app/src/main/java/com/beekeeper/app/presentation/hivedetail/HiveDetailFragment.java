package com.beekeeper.app.presentation.hivedetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.beekeeper.app.databinding.FragmentHiveDetailBinding;
import com.beekeeper.app.presentation.base.BaseFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class HiveDetailFragment extends BaseFragment<FragmentHiveDetailBinding> {

    private static final String ARG_HIVE_ID = "hive_id";
    private static final String ARG_HIVE_NAME = "hive_name";
    private static final String ARG_HIVE_TYPE = "hive_type";
    private static final String ARG_QUEEN_INFO = "queen_info";

    private String hiveId;
    private String hiveName;
    private String hiveType;
    private String queenInfo;

    private int currentTabPosition = 0;

    public static HiveDetailFragment newInstance(String hiveId, String hiveName, String hiveType, String queenInfo) {
        HiveDetailFragment fragment = new HiveDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HIVE_ID, hiveId);
        args.putString(ARG_HIVE_NAME, hiveName);
        args.putString(ARG_HIVE_TYPE, hiveType);
        args.putString(ARG_QUEEN_INFO, queenInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hiveId = getArguments().getString(ARG_HIVE_ID);
            hiveName = getArguments().getString(ARG_HIVE_NAME);
            hiveType = getArguments().getString(ARG_HIVE_TYPE);
            queenInfo = getArguments().getString(ARG_QUEEN_INFO);
        }
    }

    @Override
    protected FragmentHiveDetailBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHiveDetailBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        if (hiveName != null) {
            binding.textHiveName.setText(hiveName);
            getActivity().setTitle(hiveName);
        }

        if (hiveType != null) {
            binding.textHiveType.setText(hiveType);
        }

        if (queenInfo != null && !queenInfo.isEmpty()) {
            binding.textHiveQueen.setText(queenInfo);
        } else {
            binding.textHiveQueen.setText("Bez matky");
        }

        // Setup ViewPager2 with tabs
        setupViewPager();

        // Setup FAB based on current tab
        binding.fabAdd.setOnClickListener(v -> handleFabClick());
    }

    @Override
    protected void observeData() {
        // No LiveData to observe in this fragment
        // Data will be loaded in tab fragments
    }

    private void setupViewPager() {
        HiveDetailPagerAdapter adapter = new HiveDetailPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
            (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("Prehľad");
                        break;
                    case 1:
                        tab.setText("Prehliadky");
                        break;
                    case 2:
                        tab.setText("Krmenie");
                        break;
                    case 3:
                        tab.setText("Taxácie");
                        break;
                }
            }
        ).attach();

        // Track current tab position
        binding.viewPager.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentTabPosition = position;
            }
        });
    }

    private void handleFabClick() {
        switch (currentTabPosition) {
            case 1: // Inspections
                showToast("Otvorenie formuláru prehliadky");
                // TODO: Navigate to InspectionEntryFragment
                break;
            case 2: // Feedings
                showToast("Otvorenie formuláru krmenia (TODO)");
                // TODO: Navigate to FeedingEntryFragment
                break;
            case 3: // Taxations
                showToast("Otvorenie formuláru taxácie (TODO)");
                // TODO: Navigate to TaxationEntryFragment
                break;
            default:
                // Overview tab - no action
                break;
        }
    }

    // ViewPager2 Adapter
    private class HiveDetailPagerAdapter extends FragmentStateAdapter {

        public HiveDetailPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return HiveOverviewTabFragment.newInstance(hiveId);
                case 1:
                    return HiveInspectionsTabFragment.newInstance(hiveId);
                case 2:
                    return HiveFeedingsTabFragment.newInstance(hiveId);
                case 3:
                    return HiveTaxationsTabFragment.newInstance(hiveId);
                default:
                    return HiveOverviewTabFragment.newInstance(hiveId);
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
