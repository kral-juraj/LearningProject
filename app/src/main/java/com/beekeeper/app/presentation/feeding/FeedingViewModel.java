package com.beekeeper.app.presentation.feeding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeper.app.BeekeeperApplication;
import com.beekeeper.app.data.local.entity.Feeding;
import com.beekeeper.app.data.repository.FeedingRepository;
import com.beekeeper.app.presentation.base.BaseViewModel;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedingViewModel extends BaseViewModel {

    private final FeedingRepository repository;
    private final MutableLiveData<List<Feeding>> feedings = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public FeedingViewModel() {
        this.repository = new FeedingRepository(
            BeekeeperApplication.getInstance().getDatabase().feedingDao()
        );
    }

    // Constructor for testing
    public FeedingViewModel(FeedingRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Feeding>> getFeedings() {
        return feedings;
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

    public void loadFeedingsByHiveId(String hiveId) {
        loading.setValue(true);
        addDisposable(
            repository.getFeedingsByHiveId(hiveId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    feedingList -> {
                        feedings.setValue(feedingList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní krmení: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createFeeding(
        String hiveId,
        long feedingDate,
        double weightBefore,
        double weightAfter,
        String feedType,
        double amountKg,
        String notes
    ) {
        Feeding feeding = new Feeding();
        feeding.setId(UUID.randomUUID().toString());
        feeding.setHiveId(hiveId);
        feeding.setFeedingDate(feedingDate);
        feeding.setWeightBefore(weightBefore);
        feeding.setWeightAfter(weightAfter);
        feeding.setFeedType(feedType);
        feeding.setAmountKg(amountKg);
        feeding.setNotes(notes != null ? notes.trim() : "");
        feeding.setCreatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.insertFeeding(feeding)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Krmenie úspešne vytvorené");
                        loading.setValue(false);
                        loadFeedingsByHiveId(hiveId);
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní krmenia: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateFeeding(Feeding feeding) {
        loading.setValue(true);
        addDisposable(
            repository.updateFeeding(feeding)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Krmenie úspešne aktualizované");
                        loading.setValue(false);
                        loadFeedingsByHiveId(feeding.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii krmenia: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void deleteFeeding(Feeding feeding) {
        loading.setValue(true);
        addDisposable(
            repository.deleteFeeding(feeding)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Krmenie úspešne zmazané");
                        loading.setValue(false);
                        loadFeedingsByHiveId(feeding.getHiveId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri mazaní krmenia: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }
}
