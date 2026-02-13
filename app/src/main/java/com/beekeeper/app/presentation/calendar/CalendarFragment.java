package com.beekeeper.app.presentation.calendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.beekeeper.app.databinding.FragmentCalendarBinding;
import com.beekeeper.app.presentation.base.BaseFragment;

public class CalendarFragment extends BaseFragment<FragmentCalendarBinding> {

    @Override
    protected FragmentCalendarBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCalendarBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupViews() {
        binding.textCalendar.setText("Kalendár - Coming Soon");
    }

    @Override
    protected void observeData() {
        // To be implemented
    }
}
