package com.beekeeper.shared.viewmodel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Base ViewModel for shared business logic.
 * Platform-agnostic - no Android dependencies.
 *
 * Manages RxJava disposables lifecycle.
 * Subclasses should call dispose() when ViewModel is no longer needed.
 */
public abstract class BaseViewModel {

    protected CompositeDisposable disposables = new CompositeDisposable();

    /**
     * Add a disposable to be managed by this ViewModel.
     * All disposables will be cleared when dispose() is called.
     */
    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    /**
     * Clear all disposables and clean up resources.
     * Platform implementations should call this when ViewModel is destroyed:
     * - Android: Override onCleared()
     * - Desktop: Call manually when closing window/view
     */
    public void dispose() {
        disposables.clear();
    }

    /**
     * Check if ViewModel has been disposed.
     */
    public boolean isDisposed() {
        return disposables.isDisposed();
    }
}
