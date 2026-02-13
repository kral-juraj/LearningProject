package com.beekeeper.app.presentation.apiary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.beekeeper.app.BeekeeperApplication;
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.data.repository.ApiaryRepository;
import com.beekeeper.app.presentation.base.BaseViewModel;
import com.beekeeper.app.util.DateUtils;

import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiaryViewModel extends BaseViewModel {

    private final ApiaryRepository repository;
    private final MutableLiveData<List<Apiary>> apiaries = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<String> success = new MutableLiveData<>();

    public ApiaryViewModel() {
        this.repository = new ApiaryRepository(
            BeekeeperApplication.getInstance().getDatabase().apiaryDao()
        );
    }

    public LiveData<List<Apiary>> getApiaries() {
        return apiaries;
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

    public void loadApiaries() {
        loading.setValue(true);
        addDisposable(
            repository.getAllApiaries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    apiaryList -> {
                        apiaries.setValue(apiaryList);
                        loading.setValue(false);
                    },
                    throwable -> {
                        error.setValue("Chyba pri načítaní včelníc: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void createApiary(String name, String location, double latitude, double longitude) {
        if (name == null || name.trim().isEmpty()) {
            error.setValue("Názov včelnice nemôže byť prázdny");
            return;
        }

        Apiary apiary = new Apiary();
        apiary.setId(UUID.randomUUID().toString());
        apiary.setName(name.trim());
        apiary.setLocation(location != null ? location.trim() : "");
        apiary.setLatitude(latitude);
        apiary.setLongitude(longitude);
        apiary.setCreatedAt(DateUtils.getCurrentTimestamp());
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.insertApiary(apiary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Včelnica úspešne vytvorená");
                        loading.setValue(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.setValue("Chyba pri vytváraní včelnice: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void updateApiary(Apiary apiary) {
        if (apiary.getName() == null || apiary.getName().trim().isEmpty()) {
            error.setValue("Názov včelnice nemôže byť prázdny");
            return;
        }

        apiary.setName(apiary.getName().trim());
        apiary.setLocation(apiary.getLocation() != null ? apiary.getLocation().trim() : "");
        apiary.setUpdatedAt(DateUtils.getCurrentTimestamp());

        loading.setValue(true);
        addDisposable(
            repository.updateApiary(apiary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Včelnica úspešne aktualizovaná");
                        loading.setValue(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.setValue("Chyba pri aktualizácii včelnice: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }

    public void deleteApiary(Apiary apiary) {
        loading.setValue(true);
        addDisposable(
            repository.deleteApiary(apiary)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    () -> {
                        success.setValue("Včelnica úspešne zmazaná");
                        loading.setValue(false);
                        loadApiaries(); // Refresh list
                    },
                    throwable -> {
                        error.setValue("Chyba pri mazaní včelnice: " + throwable.getMessage());
                        loading.setValue(false);
                    }
                )
        );
    }
}
