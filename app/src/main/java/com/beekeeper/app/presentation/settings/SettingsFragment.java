package com.beekeeper.app.presentation.settings;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beekeeper.app.databinding.FragmentSettingsBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> {

    @Override
    protected FragmentSettingsBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.textSettings.setText("Nastavenia - Coming Soon");
    }

    @Override
    protected void observeData() {
        // To be implemented
    }
}
