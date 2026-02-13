package com.beekeeper.app.presentation.inspection;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.beekeeper.app.data.local.entity.Inspection;
import com.beekeeper.app.data.repository.InspectionRepository;

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
public class InspectionViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private InspectionRepository mockRepository;

    @Mock
    private Observer<List<Inspection>> inspectionsObserver;

    @Mock
    private Observer<Boolean> loadingObserver;

    @Mock
    private Observer<String> errorObserver;

    @Mock
    private Observer<String> successObserver;

    private InspectionViewModel viewModel;
    private String testHiveId = "hive-1";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        viewModel = new InspectionViewModel(mockRepository);

        viewModel.getInspections().observeForever(inspectionsObserver);
        viewModel.getLoading().observeForever(loadingObserver);
        viewModel.getError().observeForever(errorObserver);
        viewModel.getSuccess().observeForever(successObserver);
    }

    @Test
    public void loadInspectionsByHiveId_success_updatesLiveData() {
        // Arrange
        Inspection inspection1 = createTestInspection("1", testHiveId);
        Inspection inspection2 = createTestInspection("2", testHiveId);
        List<Inspection> inspections = Arrays.asList(inspection1, inspection2);

        when(mockRepository.getInspectionsByHiveId(testHiveId))
            .thenReturn(Flowable.just(inspections));

        // Act
        viewModel.loadInspectionsByHiveId(testHiveId);

        // Assert
        verify(mockRepository).getInspectionsByHiveId(testHiveId);
        verify(inspectionsObserver).onChanged(inspections);
        verify(loadingObserver).onChanged(true);
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void loadInspectionsByHiveId_error_setsErrorMessage() {
        // Arrange
        when(mockRepository.getInspectionsByHiveId(testHiveId))
            .thenReturn(Flowable.error(new Exception("Database error")));

        // Act
        viewModel.loadInspectionsByHiveId(testHiveId);

        // Assert
        verify(errorObserver).onChanged(anyString());
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void createInspection_success() {
        // Arrange
        when(mockRepository.insertInspection(any(Inspection.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getInspectionsByHiveId(testHiveId))
            .thenReturn(Flowable.just(Arrays.asList()));

        long inspectionDate = System.currentTimeMillis();

        // Act
        viewModel.createInspection(
            testHiveId,
            inspectionDate,
            22.5,
            7,
            5.5,
            8,
            50,
            30,
            2,
            10,
            true,
            "Matka videná",
            false,
            0,
            2,
            "Pokojné",
            "Test poznámka"
        );

        // Assert
        verify(mockRepository).insertInspection(any(Inspection.class));
        verify(successObserver).onChanged("Prehliadka úspešne vytvorená");
    }

    @Test
    public void updateInspection_success() {
        // Arrange
        Inspection inspection = createTestInspection("1", testHiveId);
        when(mockRepository.updateInspection(any(Inspection.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getInspectionsByHiveId(testHiveId))
            .thenReturn(Flowable.just(Arrays.asList(inspection)));

        // Act
        viewModel.updateInspection(inspection);

        // Assert
        verify(mockRepository).updateInspection(inspection);
        verify(successObserver).onChanged("Prehliadka úspešne aktualizovaná");
    }

    @Test
    public void deleteInspection_success() {
        // Arrange
        Inspection inspection = createTestInspection("1", testHiveId);
        when(mockRepository.deleteInspection(any(Inspection.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getInspectionsByHiveId(testHiveId))
            .thenReturn(Flowable.just(Arrays.asList()));

        // Act
        viewModel.deleteInspection(inspection);

        // Assert
        verify(mockRepository).deleteInspection(inspection);
        verify(successObserver).onChanged("Prehliadka úspešne zmazaná");
    }

    @Test
    public void deleteInspection_error_setsErrorMessage() {
        // Arrange
        Inspection inspection = createTestInspection("1", testHiveId);
        when(mockRepository.deleteInspection(any(Inspection.class)))
            .thenReturn(Completable.error(new Exception("Delete failed")));

        // Act
        viewModel.deleteInspection(inspection);

        // Assert
        verify(errorObserver).onChanged(anyString());
        verify(loadingObserver).onChanged(false);
    }

    private Inspection createTestInspection(String id, String hiveId) {
        Inspection inspection = new Inspection();
        inspection.setId(id);
        inspection.setHiveId(hiveId);
        inspection.setInspectionDate(System.currentTimeMillis());
        inspection.setTemperature(22.0);
        inspection.setStrengthEstimate(7);
        inspection.setFoodStoresKg(5.0);
        inspection.setBroodFrames(8);
        inspection.setCappedBroodDm(50);
        inspection.setUncappedBroodDm(30);
        inspection.setPollenFrames(2);
        inspection.setTotalFrames(10);
        inspection.setQueenSeen(true);
        inspection.setQueenNote("");
        inspection.setVarroa(false);
        inspection.setVarroaCount(0);
        inspection.setAggression(2);
        inspection.setBehavior("");
        inspection.setNotes("Test inspection");
        inspection.setRecordingId("");
        inspection.setExtractedFromAudio(false);
        inspection.setCreatedAt(System.currentTimeMillis());
        inspection.setUpdatedAt(System.currentTimeMillis());
        return inspection;
    }
}
