package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.repository.TaxationRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for Taxation management.
 * 100% platform-agnostic - shared between Android and Desktop.
 */
public class TaxationViewModel extends BaseViewModel {

    private final TaxationRepository repository;
    private final SchedulerProvider schedulerProvider;

    private final BehaviorRelay<List<Taxation>> taxations = BehaviorRelay.create();
    private final BehaviorRelay<List<TaxationFrame>> frames = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    // Track current context for reload after operations
    private String currentApiaryId;
    private String currentHiveId;

    public TaxationViewModel(TaxationRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<List<Taxation>> getTaxations() {
        return taxations;
    }

    public Observable<List<TaxationFrame>> getFrames() {
        return frames;
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

    public void loadTaxationsByHiveId(String hiveId) {
        this.currentHiveId = hiveId;
        this.currentApiaryId = null; // Clear apiary context

        loading.accept(true);
        addDisposable(
            repository.getTaxationsByHiveId(hiveId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    taxationList -> {
                        taxations.accept(taxationList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní taxácií: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void loadTaxationsByApiaryId(String apiaryId) {
        this.currentApiaryId = apiaryId;
        this.currentHiveId = null; // Clear hive context

        loading.accept(true);
        addDisposable(
            repository.getTaxationsByApiaryId(apiaryId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    taxationList -> {
                        taxations.accept(taxationList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní taxácií: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Reload taxations based on current context (apiary or hive).
     */
    private void reloadTaxations() {
        if (currentApiaryId != null) {
            loadTaxationsByApiaryId(currentApiaryId);
        } else if (currentHiveId != null) {
            loadTaxationsByHiveId(currentHiveId);
        }
    }

    public void loadFramesByTaxationId(String taxationId) {
        loading.accept(true);
        addDisposable(
            repository.getFramesByTaxationId(taxationId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    frameList -> {
                        frames.accept(frameList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní rámikov: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void createTaxationWithFrames(Taxation taxation, List<TaxationFrame> frameList) {
        if (taxation.getId() == null || taxation.getId().isEmpty()) {
            taxation.setId(UUID.randomUUID().toString());
        }
        if (taxation.getCreatedAt() == 0) {
            taxation.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        // Calculate aggregated values from all frames
        calculateFrameAggregates(taxation, frameList);

        loading.accept(true);
        addDisposable(
            repository.insertTaxationWithFrames(taxation, frameList)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Taxácia úspešne vytvorená");
                        loading.accept(false);
                        reloadTaxations(); // Reload based on current context
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní taxácie: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Calculate aggregated values from all frames and set them in taxation.
     */
    private void calculateFrameAggregates(Taxation taxation, List<TaxationFrame> frames) {
        int totalPollen = 0;
        int totalCappedStores = 0;
        int totalUncappedStores = 0;
        int totalCappedBrood = 0;
        int totalUncappedBrood = 0;
        int totalStarter = 0;

        for (TaxationFrame frame : frames) {
            totalPollen += frame.getPollenDm();
            totalCappedStores += frame.getCappedStoresDm();
            totalUncappedStores += frame.getUncappedStoresDm();
            totalCappedBrood += frame.getCappedBroodDm();
            totalUncappedBrood += frame.getUncappedBroodDm();
            if (frame.isStarter()) {
                totalStarter++;
            }
        }

        taxation.setTotalPollenDm(totalPollen);
        taxation.setTotalCappedStoresDm(totalCappedStores);
        taxation.setTotalUncappedStoresDm(totalUncappedStores);
        taxation.setTotalCappedBroodDm(totalCappedBrood);
        taxation.setTotalUncappedBroodDm(totalUncappedBrood);
        taxation.setTotalStarterFrames(totalStarter);
    }

    public void updateTaxation(Taxation taxation) {
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.updateTaxation(taxation)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Taxácia úspešne aktualizovaná");
                        loading.accept(false);
                        reloadTaxations(); // Reload based on current context
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii taxácie: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void updateTaxationWithFrames(Taxation taxation, List<TaxationFrame> frameList) {
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        // Calculate aggregated values from all frames
        calculateFrameAggregates(taxation, frameList);

        loading.accept(true);

        // First update the taxation header
        addDisposable(
            repository.updateTaxation(taxation)
                .andThen(repository.getFramesByTaxationIdOnce(taxation.getId()))
                .flatMapCompletable(oldFrames -> {
                    // Delete all old frames
                    List<io.reactivex.Completable> deletions = new java.util.ArrayList<>();
                    for (TaxationFrame oldFrame : oldFrames) {
                        deletions.add(repository.deleteFrame(oldFrame));
                    }
                    return io.reactivex.Completable.concat(deletions);
                })
                .andThen(repository.insertFrames(frameList))
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Taxácia úspešne aktualizovaná");
                        loading.accept(false);
                        reloadTaxations(); // Reload based on current context
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii taxácie: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void deleteTaxation(Taxation taxation) {
        loading.accept(true);
        addDisposable(
            repository.deleteTaxation(taxation)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Taxácia úspešne zmazaná");
                        loading.accept(false);
                        reloadTaxations(); // Reload based on current context
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní taxácie: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }
}
