package com.beekeeper.app.presentation.hive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeper.app.BeekeeperApplication;
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.data.repository.HiveRepository;
import com.beekeeper.app.presentation.base.BaseViewModel;
import com.beekeeper.app.util.Constants;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HiveViewModel extends BaseViewModel {

    private final HiveRepository repository;
    private final MutableLiveData<List<Hive>> hives = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public HiveViewModel() {
        this.repository = new HiveRepository(
            BeekeeperApplication.getInstance().getDatabase().hiveDao()
        );
    }

    public LiveData<List<Hive>> getHives() {
        return hives;
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

    public void loadHivesByApiaryId(String apiaryId) {
        loading.setValue(true);
        addDisposable(
            repository.getHivesByApiaryId(apiaryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    hiveList -> {
                        hives.setValue(hiveList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní úľov: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createHive(String apiaryId, String name, String type, String queenId, int queenYear) {
        if (name == null || name.trim().isEmpty()) {
            error.setValue("Názov úľa nemôže byť prázdny");
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

        loading.setValue(true);
        addDisposable(
            repository.insertHive(hive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Úľ úspešne vytvorený");
                        loading.setValue(false);
                        loadHivesByApiaryId(apiaryId);
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní úľa: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateHive(Hive hive) {
        if (hive.getName() == null || hive.getName().trim().isEmpty()) {
            error.setValue("Názov úľa nemôže byť prázdny");
            return;
        }

        hive.setName(hive.getName().trim());
        hive.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.updateHive(hive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Úľ úspešne aktualizovaný");
                        loading.setValue(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii úľa: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void deleteHive(Hive hive) {
        loading.setValue(true);
        addDisposable(
            repository.deleteHive(hive)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Úľ úspešne zmazaný");
                        loading.setValue(false);
                        loadHivesByApiaryId(hive.getApiaryId());
                    },
                    throwable -> {
                        error.setValue("Chyba pri mazaní úľa: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void toggleHiveActive(Hive hive) {
        hive.setActive(!hive.isActive());
        updateHive(hive);
    }
}
