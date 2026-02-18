package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for Apiary management.
 * 100% platform-agnostic - shared between Android and Desktop.
 *
 * Uses BehaviorRelay instead of LiveData for reactive state management.
 * Injects SchedulerProvider for platform-specific threading.
 */
public class ApiaryViewModel extends BaseViewModel {

    private final ApiaryRepository repository;
    private final SchedulerProvider schedulerProvider;

    // State using BehaviorRelay (replacement for LiveData)
    private final BehaviorRelay<List<Apiary>> apiaries = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    /**
     * Constructor with dependency injection.
     * @param repository ApiaryRepository for data operations
     * @param schedulerProvider Platform-specific scheduler provider
     */
    public ApiaryViewModel(ApiaryRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    // Observables for UI binding (read-only)
    public Observable<List<Apiary>> getApiaries() {
        return apiaries;
    }

    public Observable<Boolean> getLoading() {
        return loading;
    }

    public Observable<String> getError() {
        return error;
    }

    public Observable<String> getSuccess() {
        return success;
    }

    /**
     * Load all apiaries from repository.
     */
    public void loadApiaries() {
        loading.accept(true);
        addDisposable(
            repository.getAllApiaries()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    apiaryList -> {
                        apiaries.accept(apiaryList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní včelníc: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Create a new apiary.
     * @param name Apiary name (required)
     * @param location Apiary location
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     */
    public void createApiary(String name, String location, double latitude, double longitude) {
        createApiary(name, location, latitude, longitude, null, null, null);
    }

    /**
     * Create a new apiary with all fields.
     * @param name Apiary name (required)
     * @param location Apiary location
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     * @param registrationNumber Registration number (optional)
     * @param address Detailed address (optional)
     * @param description Description/notes (optional)
     */
    public void createApiary(String name, String location, double latitude, double longitude,
                            String registrationNumber, String address, String description) {
        if (name == null || name.trim().isEmpty()) {
            error.accept("Názov včelnice nemôže byť prázdny");
            return;
        }

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName(name.trim());
        apiary.setLocation(location != null ? location.trim() : "");
        apiary.setLatitude(latitude);
        apiary.setLongitude(longitude);
        apiary.setRegistrationNumber(registrationNumber != null && !registrationNumber.trim().isEmpty() ? registrationNumber.trim() : null);
        apiary.setAddress(address != null && !address.trim().isEmpty() ? address.trim() : null);
        apiary.setDescription(description != null && !description.trim().isEmpty() ? description.trim() : null);
        apiary.setCreatedAt(DateUtils.getCurrentTimestamp());
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.insertApiary(apiary)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Včelnica úspešne vytvorená");
                        loading.accept(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní včelnice: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Update existing apiary.
     * @param apiary Apiary to update
     */
    public void updateApiary(Apiary apiary) {
        if (apiary.getName() == null || apiary.getName().trim().isEmpty()) {
            error.accept("Názov včelnice nemôže byť prázdny");
            return;
        }

        apiary.setName(apiary.getName().trim());
        apiary.setLocation(apiary.getLocation() != null ? apiary.getLocation().trim() : "");
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.updateApiary(apiary)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Včelnica úspešne aktualizovaná");
                        loading.accept(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii včelnice: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Delete an apiary.
     * @param apiary Apiary to delete
     */
    public void deleteApiary(Apiary apiary) {
        loading.accept(true);
        addDisposable(
            repository.deleteApiary(apiary)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Včelnica úspešne zmazaná");
                        loading.accept(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní včelnice: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Update display order for multiple apiaries (drag-and-drop reordering).
     * Silent update - no success message shown to avoid spam during drag operations.
     *
     * Use case: User drags apiary to new position in list, all display orders updated in batch.
     *
     * @param apiaries List of apiaries with updated displayOrder values
     */
    public void updateApiaryOrder(List<Apiary> apiaries) {
        if (apiaries == null || apiaries.isEmpty()) {
            return;
        }

        addDisposable(
            repository.updateApiaryOrder(apiaries)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        // Silent update - no success message
                        // Optionally refresh list if needed
                        loadApiaries();
                    },
                    throwable -> {
                        error.accept("Chyba pri zmene poradia včelníc: " + throwable.getMessage());
                    }
                )
        );
    }
}
