package com.beekeeper.app.data.repository;

import com.beekeeper.app.data.local.dao.InspectionDao;
import com.beekeeper.app.data.local.entity.Inspection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InspectionRepositoryTest {

    @Mock
    private InspectionDao mockDao;

    private InspectionRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new InspectionRepository(mockDao);
    }

    @Test
    public void insertInspection_setsIdAndTimestamps() {
        // Arrange
        Inspection inspection = new Inspection();
        inspection.setHiveId("hive-1");
        inspection.setInspectionDate(System.currentTimeMillis());
        when(mockDao.insert(any(Inspection.class))).thenReturn(Completable.complete());

        // Act
        repository.insertInspection(inspection).test();

        // Assert
        verify(mockDao).insert(inspection);
    }

    @Test
    public void updateInspection_updatesTimestamp() {
        // Arrange
        Inspection inspection = new Inspection();
        inspection.setId("1");
        inspection.setHiveId("hive-1");
        when(mockDao.update(any(Inspection.class))).thenReturn(Completable.complete());

        // Act
        repository.updateInspection(inspection).test();

        // Assert
        verify(mockDao).update(inspection);
    }

    @Test
    public void deleteInspection_callsDaoDelete() {
        // Arrange
        Inspection inspection = new Inspection();
        inspection.setId("1");
        when(mockDao.delete(any(Inspection.class))).thenReturn(Completable.complete());

        // Act
        repository.deleteInspection(inspection).test();

        // Assert
        verify(mockDao).delete(inspection);
    }

    @Test
    public void getInspectionById_returnsSingleInspection() {
        // Arrange
        Inspection inspection = new Inspection();
        inspection.setId("1");
        inspection.setHiveId("hive-1");
        when(mockDao.getById("1")).thenReturn(Single.just(inspection));

        // Act
        Single<Inspection> result = repository.getInspectionById("1");

        // Assert
        result.test()
            .assertValue(i -> i.getId().equals("1"))
            .assertComplete();
        verify(mockDao).getById("1");
    }

    @Test
    public void getInspectionsByHiveId_returnsFilteredList() {
        // Arrange
        String hiveId = "hive-1";
        Inspection inspection1 = new Inspection();
        inspection1.setId("1");
        inspection1.setHiveId(hiveId);

        Inspection inspection2 = new Inspection();
        inspection2.setId("2");
        inspection2.setHiveId(hiveId);

        List<Inspection> inspections = Arrays.asList(inspection1, inspection2);
        when(mockDao.getByHiveId(hiveId)).thenReturn(Flowable.just(inspections));

        // Act
        Flowable<List<Inspection>> result = repository.getInspectionsByHiveId(hiveId);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 2)
            .assertNotComplete();
        verify(mockDao).getByHiveId(hiveId);
    }

    @Test
    public void getInspectionsByHiveIdAndDateRange_returnsFilteredList() {
        // Arrange
        String hiveId = "hive-1";
        long startDate = System.currentTimeMillis() - 86400000L; // 1 day ago
        long endDate = System.currentTimeMillis();

        Inspection inspection = new Inspection();
        inspection.setId("1");
        inspection.setHiveId(hiveId);
        inspection.setInspectionDate(System.currentTimeMillis() - 43200000L); // 12 hours ago

        when(mockDao.getByHiveIdAndDateRange(hiveId, startDate, endDate))
            .thenReturn(Single.just(Arrays.asList(inspection)));

        // Act
        Single<List<Inspection>> result = repository.getInspectionsByHiveIdAndDateRange(hiveId, startDate, endDate);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 1)
            .assertComplete();
        verify(mockDao).getByHiveIdAndDateRange(hiveId, startDate, endDate);
    }

    @Test
    public void getRecentInspections_returnsLimitedList() {
        // Arrange
        int limit = 5;
        Inspection inspection = new Inspection();
        inspection.setId("1");

        when(mockDao.getRecent(limit)).thenReturn(Flowable.just(Arrays.asList(inspection)));

        // Act
        Flowable<List<Inspection>> result = repository.getRecentInspections(limit);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 1)
            .assertNotComplete();
        verify(mockDao).getRecent(limit);
    }

    @Test
    public void getInspectionCountByHiveId_returnsCount() {
        // Arrange
        String hiveId = "hive-1";
        when(mockDao.getCountByHiveId(hiveId)).thenReturn(Single.just(15));

        // Act
        Single<Integer> result = repository.getInspectionCountByHiveId(hiveId);

        // Assert
        result.test()
            .assertValue(count -> count == 15)
            .assertComplete();
        verify(mockDao).getCountByHiveId(hiveId);
    }
}
