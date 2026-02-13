package com.beekeeper.desktop.scheduler;

import com.beekeeper.shared.scheduler.SchedulerProvider;
import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Desktop (JavaFX) implementation of SchedulerProvider.
 * Uses JavaFxScheduler.platform() for main thread operations.
 */
public class DesktopSchedulerProvider implements SchedulerProvider {

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler mainThread() {
        return JavaFxScheduler.platform();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }
}
