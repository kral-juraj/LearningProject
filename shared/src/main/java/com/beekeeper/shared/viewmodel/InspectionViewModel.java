package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Inspection;
import com.beekeeper.shared.repository.InspectionRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for Inspection management.
 * 100% platform-agnostic - shared between Android and Desktop.
 */
public class InspectionViewModel extends BaseViewModel {

    private final InspectionRepository repository;
    private final SchedulerProvider schedulerProvider;

    private final BehaviorRelay<List<Inspection>> inspections = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    public InspectionViewModel(InspectionRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<List<Inspection>> getInspections() {
        return inspections;
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

    public void loadInspectionsByHiveId(String hiveId) {
        loading.accept(true);
        addDisposable(
            repository.getInspectionsByHiveId(hiveId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    inspectionList -> {
                        inspections.accept(inspectionList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní prehliadok: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void createInspection(Inspection inspection) {
        if (inspection.getId() == null || inspection.getId().isEmpty()) {
            inspection.setId(UUID.randomUUID().toString());
        }
        if (inspection.getCreatedAt() == 0) {
            inspection.setCreatedAt(DateUtils.getCurrentTimestamp());
        }
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.insertInspection(inspection)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Prehliadka úspešne vytvorená");
                        loading.accept(false);
                        loadInspectionsByHiveId(inspection.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní prehliadky: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void updateInspection(Inspection inspection) {
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.accept(true);
        addDisposable(
            repository.updateInspection(inspection)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Prehliadka úspešne aktualizovaná");
                        loading.accept(false);
                        loadInspectionsByHiveId(inspection.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii prehliadky: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    public void deleteInspection(Inspection inspection) {
        loading.accept(true);
        addDisposable(
            repository.deleteInspection(inspection)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Prehliadka úspešne zmazaná");
                        loading.accept(false);
                        loadInspectionsByHiveId(inspection.getHiveId());
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní prehliadky: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }
}
