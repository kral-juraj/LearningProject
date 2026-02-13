package com.beekeeper.app.presentation.analytics;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beekeeper.app.databinding.FragmentAnalyticsBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

public class AnalyticsFragment extends BaseFragment<FragmentAnalyticsBinding> {

    @Override
    protected FragmentAnalyticsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentAnalyticsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.textAnalytics.setText("Analýza - Coming Soon");
    }

    @Override
    protected void observeData() {
        // To be implemented
    }
}
