package com.beekeeper.app.data.repository;

import com.beekeeper.app.data.local.dao.HiveDao;
import com.beekeeper.app.data.local.entity.Hive;

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
public class HiveRepositoryTest {

    @Mock
    private HiveDao mockDao;

    private HiveRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new HiveRepository(mockDao);
    }

    @Test
    public void insertHive_setsIdAndTimestamps() {
        // Arrange
        Hive hive = new Hive();
        hive.setApiaryId("apiary-1");
        hive.setName("U1");
        when(mockDao.insert(any(Hive.class))).thenReturn(Completable.complete());

        // Act
        repository.insertHive(hive).test();

        // Assert
        verify(mockDao).insert(hive);
    }

    @Test
    public void updateHive_updatesTimestamp() {
        // Arrange
        Hive hive = new Hive();
        hive.setId("1");
        hive.setName("Updated Hive");
        when(mockDao.update(any(Hive.class))).thenReturn(Completable.complete());

        // Act
        repository.updateHive(hive).test();

        // Assert
        verify(mockDao).update(hive);
    }

    @Test
    public void deleteHive_callsDaoDelete() {
        // Arrange
        Hive hive = new Hive();
        hive.setId("1");
        when(mockDao.delete(any(Hive.class))).thenReturn(Completable.complete());

        // Act
        repository.deleteHive(hive).test();

        // Assert
        verify(mockDao).delete(hive);
    }

    @Test
    public void getHiveById_returnsSingleHive() {
        // Arrange
        Hive hive = new Hive();
        hive.setId("1");
        hive.setName("U1");
        when(mockDao.getById("1")).thenReturn(Single.just(hive));

        // Act
        Single<Hive> result = repository.getHiveById("1");

        // Assert
        result.test()
            .assertValue(h -> h.getId().equals("1"))
            .assertComplete();
        verify(mockDao).getById("1");
    }

    @Test
    public void getHivesByApiaryId_returnsFilteredList() {
        // Arrange
        String apiaryId = "apiary-1";
        Hive hive1 = new Hive();
        hive1.setId("1");
        hive1.setApiaryId(apiaryId);
        hive1.setName("U1");

        Hive hive2 = new Hive();
        hive2.setId("2");
        hive2.setApiaryId(apiaryId);
        hive2.setName("U2");

        List<Hive> hives = Arrays.asList(hive1, hive2);
        when(mockDao.getByApiaryId(apiaryId)).thenReturn(Flowable.just(hives));

        // Act
        Flowable<List<Hive>> result = repository.getHivesByApiaryId(apiaryId);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 2)
            .assertNotComplete();
        verify(mockDao).getByApiaryId(apiaryId);
    }

    @Test
    public void getActiveHivesByApiaryId_returnsActiveOnly() {
        // Arrange
        String apiaryId = "apiary-1";
        Hive activeHive = new Hive();
        activeHive.setId("1");
        activeHive.setApiaryId(apiaryId);
        activeHive.setActive(true);

        when(mockDao.getActiveByApiaryId(apiaryId)).thenReturn(Flowable.just(Arrays.asList(activeHive)));

        // Act
        Flowable<List<Hive>> result = repository.getActiveHivesByApiaryId(apiaryId);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 1 && list.get(0).isActive())
            .assertNotComplete();
        verify(mockDao).getActiveByApiaryId(apiaryId);
    }

    @Test
    public void getHiveCount_returnsCount() {
        // Arrange
        when(mockDao.getCount()).thenReturn(Single.just(10));

        // Act
        Single<Integer> result = repository.getHiveCount();

        // Assert
        result.test()
            .assertValue(count -> count == 10)
            .assertComplete();
        verify(mockDao).getCount();
    }

    @Test
    public void getHiveCountByApiaryId_returnsFilteredCount() {
        // Arrange
        String apiaryId = "apiary-1";
        when(mockDao.getCountByApiaryId(apiaryId)).thenReturn(Single.just(5));

        // Act
        Single<Integer> result = repository.getHiveCountByApiaryId(apiaryId);

        // Assert
        result.test()
            .assertValue(count -> count == 5)
            .assertComplete();
        verify(mockDao).getCountByApiaryId(apiaryId);
    }
}
