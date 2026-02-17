package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.Constants;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for Hive management.
 * 100% platform-agnostic - shared between Android and Desktop.
 */
public class HiveViewModel extends BaseViewModel {

    private final HiveRepository repository;
    private final SchedulerProvider schedulerProvider;
    private final TranslationManager tm;

    // State using BehaviorRelay
    private final BehaviorRelay<List<Hive>> hives = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    public HiveViewModel(HiveRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
        this.tm = TranslationManager.getInstance();
    }

    // Observables for UI binding
    public Observable<List<Hive>> getHives() {
        return hives;
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

    public void loadHivesByApiaryId(String apiaryId) {
        loading.accept(true);
        addDisposable(
            repository.getHivesByApiaryId(apiaryId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    hiveList -> {
                        hives.accept(hiveList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept(tm.get("error.loading_hives", throwable.getMessage()));
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Create a new hive with basic parameters (legacy method).
     * For compatibility with existing code.
     */
    public void createHive(String apiaryId, String name, String type, String queenId, int queenYear) {
        if (name == null || name.trim().isEmpty()) {
            error.accept(tm.get("validation.hive_name_required"));
            return;
        }

        Hive hive = new Hive();
        hive.setId(UUID.randomUUID().toString());
        hive.setApiaryId(apiaryId);
        hive.setName(name.trim());
        hive.setType(type != null ? type : Constants.HIVE_TYPE_VERTICAL);
        hive.setQueenId(queenId != null ? queenId.trim() : "");
        hive.setQueenYear(queenYear);
        hive.setActive(true);
        hive.setCreatedAt(DateUtils.getCurrentTimestamp());
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.insertHive(hive)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept(tm.get("success.hive_created"));
                        loading.accept(false);
                        loadHivesByApiaryId(apiaryId);
                    },
                    throwable -> {
                        error.accept(tm.get("error.creating_hive", throwable.getMessage()));
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Create a new hive from a Hive object (with all extended details).
     * Use this for creating hives with frame type, equipment, etc.
     *
     * @param hive Hive object with all details filled in
     */
    public void createHive(Hive hive) {
        if (hive.getName() == null || hive.getName().trim().isEmpty()) {
            error.accept(tm.get("validation.hive_name_required"));
            return;
        }

        // Generate ID if not present
        if (hive.getId() == null || hive.getId().isEmpty()) {
            hive.setId(UUID.randomUUID().toString());
        }

        // Set timestamps
        if (hive.getCreatedAt() == 0) {
            hive.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());

        // Trim name
        hive.setName(hive.getName().trim());

        final String apiaryId = hive.getApiaryId();

        loading.accept(true);
        addDisposable(
            repository.insertHive(hive)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept(tm.get("success.hive_created"));
                        loading.accept(false);
                        loadHivesByApiaryId(apiaryId);
                    },
                    throwable -> {
                        error.accept(tm.get("error.creating_hive", throwable.getMessage()));
                        loading.accept(false);
                    }
                )
        );
    }

    public void updateHive(Hive hive) {
        if (hive.getName() == null || hive.getName().trim().isEmpty()) {
            error.accept(tm.get("validation.hive_name_required"));
            return;
        }

        hive.setName(hive.getName().trim());
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.updateHive(hive)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept(tm.get("success.hive_updated"));
                        loading.accept(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.accept(tm.get("error.updating_hive", throwable.getMessage()));
                        loading.accept(false);
                    }
                )
        );
    }

    public void deleteHive(Hive hive) {
        loading.accept(true);
        addDisposable(
            repository.deleteHive(hive)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept(tm.get("success.hive_deleted"));
                        loading.accept(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.accept(tm.get("error.deleting_hive", throwable.getMessage()));
                        loading.accept(false);
                    }
                )
        );
    }

    public void toggleHiveActive(Hive hive) {
        hive.setActive(!hive.isActive());
        updateHive(hive);
    }

    /**
     * Update display order for multiple hives after drag-and-drop reordering.
     *
     * @param hives List of hives with updated displayOrder values
     */
    public void updateHiveOrder(List<Hive> hives) {
        addDisposable(
            repository.updateHiveOrder(hives)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        // Silently update order without showing success message
                    },
                    throwable -> {
                        error.accept(tm.get("error.updating_order", throwable.getMessage()));
                    }
                )
        );
    }
}
