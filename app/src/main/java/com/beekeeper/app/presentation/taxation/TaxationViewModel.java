package com.beekeeper.app.presentation.taxation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeper.app.BeekeeperApplication;
import com.beekeeper.app.data.local.entity.Taxation;
import com.beekeeper.app.data.local.entity.TaxationFrame;
import com.beekeeper.app.data.repository.TaxationRepository;
import com.beekeeper.app.presentation.base.BaseViewModel;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaxationViewModel extends BaseViewModel {

    private final TaxationRepository repository;
    private final MutableLiveData<List<Taxation>> taxations = new MutableLiveData<>();
    private final MutableLiveData<List<TaxationFrame>> frames = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public TaxationViewModel() {
        this.repository = new TaxationRepository(
            BeekeeperApplication.getInstance().getDatabase().taxationDao(),
            BeekeeperApplication.getInstance().getDatabase().taxationFrameDao()
        );
    }

    // Constructor for testing
    public TaxationViewModel(TaxationRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Taxation>> getTaxations() {
        return taxations;
    }

    public LiveData<List<TaxationFrame>> getFrames() {
        return frames;
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

    public void loadTaxationsByHiveId(String hiveId) {
        loading.setValue(true);
        addDisposable(
            repository.getTaxationsByHiveId(hiveId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    taxationList -> {
                        taxations.setValue(taxationList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní taxácií: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void loadFramesByTaxationId(String taxationId) {
        loading.setValue(true);
        addDisposable(
            repository.getFramesByTaxationId(taxationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    frameList -> {
                        frames.setValue(frameList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní rámikov: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createTaxation(
        String hiveId,
        long taxationDate,
        double temperature,
        int totalFrames,
        double foodStoresKg,
        String notes
    ) {
        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hiveId);
        taxation.setTaxationDate(taxationDate);
        taxation.setTemperature(temperature);
        taxation.setTotalFrames(totalFrames);
        taxation.setFoodStoresKg(foodStoresKg);
        taxation.setNotes(notes != null ? notes.trim() : "");
        taxation.setCreatedAt(DateUtils.getCurrentTimestamp());
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.insertTaxation(taxation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Taxácia úspešne vytvorená");
                        loading.setValue(false);
                        loadTaxationsByHiveId(hiveId);
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní taxácie: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createTaxationWithFrames(
        String hiveId,
        long taxationDate,
        double temperature,
        int totalFrames,
        double foodStoresKg,
        String notes,
        List<TaxationFrame> frames
    ) {
        Taxation taxation = new Taxation();
        taxation.setId(UUID.randomUUID().toString());
        taxation.setHiveId(hiveId);
        taxation.setTaxationDate(taxationDate);
        taxation.setTemperature(temperature);
        taxation.setTotalFrames(totalFrames);
        taxation.setFoodStoresKg(foodStoresKg);
        taxation.setNotes(notes != null ? notes.trim() : "");
        taxation.setCreatedAt(DateUtils.getCurrentTimestamp());
        taxation.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.insertTaxationWithFrames(taxation, frames)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Taxácia s rámikmi úspešne vytvorená");
                        loading.setValue(false);
                        loadTaxationsByHiveId(hiveId);
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní taxácie: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateTaxation(Taxation taxation) {
        loading.setValue(true);
        addDisposable(
            repository.updateTaxation(taxation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Taxácia úspešne aktualizovaná");
                        loading.setValue(false);
                        loadTaxationsByHiveId(taxation.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii taxácie: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void deleteTaxation(Taxation taxation) {
        loading.setValue(true);
        addDisposable(
            repository.deleteTaxation(taxation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Taxácia úspešne zmazaná");
                        loading.setValue(false);
                        loadTaxationsByHiveId(taxation.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri mazaní taxácie: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateFrame(TaxationFrame frame) {
        loading.setValue(true);
        addDisposable(
            repository.updateFrame(frame)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii rámika: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }
}
