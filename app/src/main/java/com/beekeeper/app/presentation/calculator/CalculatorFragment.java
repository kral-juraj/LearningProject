package com.beekeeper.app.presentation.calculator;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beekeeper.app.databinding.FragmentCalculatorBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

public class CalculatorFragment extends BaseFragment<FragmentCalculatorBinding> {

    @Override
    protected FragmentCalculatorBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCalculatorBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.textCalculator.setText("Kalkulačky - Coming Soon");
    }

    @Override
    protected void observeData() {
        // To be implemented
    }
}
