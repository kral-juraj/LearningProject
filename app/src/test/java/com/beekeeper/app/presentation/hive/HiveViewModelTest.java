package com.beekeeper.app.presentation.hive;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.data.repository.HiveRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HiveViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private HiveRepository mockRepository;

    @Mock
    private Observer<List<Hive>> hivesObserver;

    @Mock
    private Observer<Boolean> loadingObserver;

    @Mock
    private Observer<String> errorObserver;

    @Mock
    private Observer<String> successObserver;

    private HiveViewModel viewModel;
    private String testApiaryId = "apiary-1";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        viewModel = new HiveViewModel(mockRepository);

        viewModel.getHives().observeForever(hivesObserver);
        viewModel.getLoading().observeForever(loadingObserver);
        viewModel.getError().observeForever(errorObserver);
        viewModel.getSuccess().observeForever(successObserver);
    }

    @Test
    public void loadHivesByApiaryId_success_updatesLiveData() {
        // Arrange
        Hive hive1 = createTestHive("1", testApiaryId, "U1", "Vertical");
        Hive hive2 = createTestHive("2", testApiaryId, "U2", "Horizontal");
        List<Hive> hives = Arrays.asList(hive1, hive2);

        when(mockRepository.getHivesByApiaryId(testApiaryId)).thenReturn(Flowable.just(hives));

        // Act
        viewModel.loadHivesByApiaryId(testApiaryId);

        // Assert
        verify(mockRepository).getHivesByApiaryId(testApiaryId);
        verify(hivesObserver).onChanged(hives);
        verify(loadingObserver).onChanged(true);
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void loadHivesByApiaryId_error_setsErrorMessage() {
        // Arrange
        when(mockRepository.getHivesByApiaryId(testApiaryId))
            .thenReturn(Flowable.error(new Exception("Database error")));

        // Act
        viewModel.loadHivesByApiaryId(testApiaryId);

        // Assert
        verify(errorObserver).onChanged(anyString());
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void createHive_validInput_success() {
        // Arrange
        when(mockRepository.insertHive(any(Hive.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getHivesByApiaryId(testApiaryId))
            .thenReturn(Flowable.just(Arrays.asList()));

        // Act
        viewModel.createHive(testApiaryId, "U1", "Vertical", "M1", 2024);

        // Assert
        verify(mockRepository).insertHive(any(Hive.class));
        verify(successObserver).onChanged("Úľ úspešne vytvorený");
    }

    @Test
    public void createHive_emptyName_setsError() {
        // Act
        viewModel.createHive(testApiaryId, "", "Vertical", "", 0);

        // Assert
        verify(errorObserver).onChanged("Názov úľa nemôže byť prázdny");
    }

    @Test
    public void createHive_nullName_setsError() {
        // Act
        viewModel.createHive(testApiaryId, null, "Vertical", "", 0);

        // Assert
        verify(errorObserver).onChanged("Názov úľa nemôže byť prázdny");
    }

    @Test
    public void updateHive_validInput_success() {
        // Arrange
        Hive hive = createTestHive("1", testApiaryId, "U1", "Vertical");
        when(mockRepository.updateHive(any(Hive.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getHivesByApiaryId(testApiaryId))
            .thenReturn(Flowable.just(Arrays.asList(hive)));

        // Act
        viewModel.updateHive(hive);

        // Assert
        verify(mockRepository).updateHive(hive);
        verify(successObserver).onChanged("Úľ úspešne aktualizovaný");
    }

    @Test
    public void updateHive_emptyName_setsError() {
        // Arrange
        Hive hive = createTestHive("1", testApiaryId, "", "Vertical");

        // Act
        viewModel.updateHive(hive);

        // Assert
        verify(errorObserver).onChanged("Názov úľa nemôže byť prázdny");
    }

    @Test
    public void deleteHive_success() {
        // Arrange
        Hive hive = createTestHive("1", testApiaryId, "U1", "Vertical");
        when(mockRepository.deleteHive(any(Hive.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getHivesByApiaryId(testApiaryId))
            .thenReturn(Flowable.just(Arrays.asList()));

        // Act
        viewModel.deleteHive(hive);

        // Assert
        verify(mockRepository).deleteHive(hive);
        verify(successObserver).onChanged("Úľ úspešne zmazaný");
    }

    @Test
    public void toggleHiveActive_changesActiveState() {
        // Arrange
        Hive hive = createTestHive("1", testApiaryId, "U1", "Vertical");
        hive.setActive(true);
        when(mockRepository.updateHive(any(Hive.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getHivesByApiaryId(testApiaryId))
            .thenReturn(Flowable.just(Arrays.asList(hive)));

        // Act
        viewModel.toggleHiveActive(hive);

        // Assert
        verify(mockRepository).updateHive(hive);
    }

    private Hive createTestHive(String id, String apiaryId, String name, String type) {
        Hive hive = new Hive();
        hive.setId(id);
        hive.setApiaryId(apiaryId);
        hive.setName(name);
        hive.setType(type);
        hive.setQueenId("");
        hive.setQueenYear(0);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        return hive;
    }
}
