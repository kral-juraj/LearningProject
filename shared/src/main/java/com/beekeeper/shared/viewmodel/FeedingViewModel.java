package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Feeding;
import com.beekeeper.shared.repository.FeedingRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for Feeding management.
 * 100% platform-agnostic - shared between Android and Desktop.
 */
public class FeedingViewModel extends BaseViewModel {

    private final FeedingRepository repository;
    private final SchedulerProvider schedulerProvider;

    private final BehaviorRelay<List<Feeding>> feedings = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    public FeedingViewModel(FeedingRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<List<Feeding>> getFeedings() {
        return feedings;
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

    public void loadFeedingsByHiveId(String hiveId) {
        loading.accept(true);
        addDisposable(
            repository.getFeedingsByHiveId(hiveId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    feedingList -> {
                        feedings.accept(feedingList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní krmenia: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void createFeeding(Feeding feeding) {
        if (feeding.getId() == null || feeding.getId().isEmpty()) {
            feeding.setId(UUID.randomUUID().toString());
        }
        if (feeding.getCreatedAt() == 0) {
            feeding.setCreatedAt(DateUtils.getCurrentTimestamp());
        }

        loading.accept(true);
        addDisposable(
            repository.insertFeeding(feeding)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Krmenie úspešne vytvorené");
                        loading.accept(false);
                        loadFeedingsByHiveId(feeding.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní krmenia: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void updateFeeding(Feeding feeding) {
        loading.accept(true);
        addDisposable(
            repository.updateFeeding(feeding)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Krmenie úspešne aktualizované");
                        loading.accept(false);
                        loadFeedingsByHiveId(feeding.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii krmenia: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void deleteFeeding(Feeding feeding) {
        loading.accept(true);
        addDisposable(
            repository.deleteFeeding(feeding)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Krmenie úspešne zmazané");
                        loading.accept(false);
                        loadFeedingsByHiveId(feeding.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní krmenia: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }
}
