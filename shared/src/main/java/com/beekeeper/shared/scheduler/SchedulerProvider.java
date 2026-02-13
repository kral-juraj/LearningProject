package com.beekeeper.shared.scheduler;

import io.reactivex.Scheduler;

/**
 * Abstraction for providing RxJava schedulers.
 * Platform-specific implementations handle threading differences:
 * - Android: Uses AndroidSchedulers.mainThread()
 * - Desktop: Uses JavaFxScheduler.platform()
 *
 * This allows ViewModels and business logic to be 100% platform-agnostic.
 */
public interface SchedulerProvider {

    /**
     * Scheduler for background/IO operations.
     * @return IO scheduler (typically Schedulers.io())
     */
    Scheduler io();

    /**
     * Scheduler for main/UI thread operations.
     * Platform-specific:
     * - Android: AndroidSchedulers.mainThread()
     * - Desktop: JavaFxScheduler.platform()
     * @return Main thread scheduler
     */
    Scheduler mainThread();

    /**
     * Scheduler for computation-intensive operations.
     * @return Computation scheduler (typically Schedulers.computation())
     */
    Scheduler computation();
}
