package com.beekeeper.app.presentation.dashboard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beekeeper.app.databinding.FragmentDashboardBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

public class DashboardFragment extends BaseFragment<FragmentDashboardBinding> {

    @Override
    protected FragmentDashboardBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentDashboardBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.textDashboard.setText("Dashboard - Coming Soon");
    }

    @Override
    protected void observeData() {
        // To be implemented
    }
}
