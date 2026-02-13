package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Hive;
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

    // State using BehaviorRelay
    private final BehaviorRelay<List<Hive>> hives = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    public HiveViewModel(HiveRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
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
                        error.accept("Chyba pri načítaní úľov: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void createHive(String apiaryId, String name, String type, String queenId, int queenYear) {
        if (name == null || name.trim().isEmpty()) {
            error.accept("Názov úľa nemôže byť prázdny");
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
                        success.accept("Úľ úspešne vytvorený");
                        loading.accept(false);
                        loadHivesByApiaryId(apiaryId);
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní úľa: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void updateHive(Hive hive) {
        if (hive.getName() == null || hive.getName().trim().isEmpty()) {
            error.accept("Názov úľa nemôže byť prázdny");
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
                        success.accept("Úľ úspešne aktualizovaný");
                        loading.accept(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii úľa: " + throwable.getMessage());
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
                        success.accept("Úľ úspešne zmazaný");
                        loading.accept(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní úľa: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void toggleHiveActive(Hive hive) {
        hive.setActive(!hive.isActive());
        updateHive(hive);
    }
}
