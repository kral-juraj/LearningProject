package com.beekeeper.app.presentation.inspection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeper.app.BeekeeperApplication;
import com.beekeeper.app.data.local.entity.Inspection;
import com.beekeeper.app.data.repository.InspectionRepository;
import com.beekeeper.app.presentation.base.BaseViewModel;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class InspectionViewModel extends BaseViewModel {

    private final InspectionRepository repository;
    private final MutableLiveData<List<Inspection>> inspections = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public InspectionViewModel() {
        this.repository = new InspectionRepository(
            BeekeeperApplication.getInstance().getDatabase().inspectionDao()
        );
    }

    // Constructor for testing
    public InspectionViewModel(InspectionRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Inspection>> getInspections() {
        return inspections;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getSuccess() {
        return success;
    }

    public void loadInspectionsByHiveId(String hiveId) {
        loading.setValue(true);
        addDisposable(
            repository.getInspectionsByHiveId(hiveId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    inspectionList -> {
                        inspections.setValue(inspectionList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní prehliadok: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createInspection(
        String hiveId,
        long inspectionDate,
        double temperature,
        int strengthEstimate,
        double foodStoresKg,
        int broodFrames,
        int cappedBroodDm,
        int uncappedBroodDm,
        int pollenFrames,
        int totalFrames,
        boolean queenSeen,
        String queenNote,
        boolean varroa,
        int varroaCount,
        int aggression,
        String behavior,
        String notes
    ) {
        Inspection inspection = new Inspection();
        inspection.setId(UUID.randomUUID().toString());
        inspection.setHiveId(hiveId);
        inspection.setInspectionDate(inspectionDate);
        inspection.setTemperature(temperature);
        inspection.setStrengthEstimate(strengthEstimate);
        inspection.setFoodStoresKg(foodStoresKg);
        inspection.setBroodFrames(broodFrames);
        inspection.setCappedBroodDm(cappedBroodDm);
        inspection.setUncappedBroodDm(uncappedBroodDm);
        inspection.setPollenFrames(pollenFrames);
        inspection.setTotalFrames(totalFrames);
        inspection.setQueenSeen(queenSeen);
        inspection.setQueenNote(queenNote != null ? queenNote.trim() : "");
        inspection.setVarroa(varroa);
        inspection.setVarroaCount(varroaCount);
        inspection.setAggression(aggression);
        inspection.setBehavior(behavior != null ? behavior.trim() : "");
        inspection.setNotes(notes != null ? notes.trim() : "");
        inspection.setRecordingId("");
        inspection.setExtractedFromAudio(false);
        inspection.setCreatedAt(DateUtils.getCurrentTimestamp());
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.insertInspection(inspection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Prehliadka úspešne vytvorená");
                        loading.setValue(false);
                        loadInspectionsByHiveId(hiveId);
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní prehliadky: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateInspection(Inspection inspection) {
        inspection.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.updateInspection(inspection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Prehliadka úspešne aktualizovaná");
                        loading.setValue(false);
                        loadInspectionsByHiveId(inspection.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii prehliadky: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void deleteInspection(Inspection inspection) {
        loading.setValue(true);
        addDisposable(
            repository.deleteInspection(inspection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Prehliadka úspešne zmazaná");
                        loading.setValue(false);
                        loadInspectionsByHiveId(inspection.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri mazaní prehliadky: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }
}
