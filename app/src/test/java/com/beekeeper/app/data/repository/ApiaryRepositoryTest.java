package com.beekeeper.app.data.repository;

import com.beekeeper.app.data.local.dao.ApiaryDao;
import com.beekeeper.app.data.local.entity.Apiary;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiaryRepositoryTest {

    @Mock
    private ApiaryDao mockDao;

    private ApiaryRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new ApiaryRepository(mockDao);
    }

    @Test
    public void insertApiary_setsIdAndTimestamps() {
        // Arrange
        Apiary apiary = new Apiary();
        apiary.setName("Test Apiary");
        apiary.setLocation("Test Location");
        when(mockDao.insert(any(Apiary.class))).thenReturn(Completable.complete());

        // Act
        repository.insertApiary(apiary).test();

        // Assert
        verify(mockDao).insert(apiary);
    }

    @Test
    public void updateApiary_updatesTimestamp() {
        // Arrange
        Apiary apiary = new Apiary();
        apiary.setId("1");
        apiary.setName("Updated Apiary");
        when(mockDao.update(any(Apiary.class))).thenReturn(Completable.complete());

        // Act
        repository.updateApiary(apiary).test();

        // Assert
        verify(mockDao).update(apiary);
    }

    @Test
    public void deleteApiary_callsDaoDelete() {
        // Arrange
        Apiary apiary = new Apiary();
        apiary.setId("1");
        when(mockDao.delete(any(Apiary.class))).thenReturn(Completable.complete());

        // Act
        repository.deleteApiary(apiary).test();

        // Assert
        verify(mockDao).delete(apiary);
    }

    @Test
    public void getApiaryById_returnsSingleApiary() {
        // Arrange
        Apiary apiary = new Apiary();
        apiary.setId("1");
        apiary.setName("Test Apiary");
        when(mockDao.getById("1")).thenReturn(Single.just(apiary));

        // Act
        Single<Apiary> result = repository.getApiaryById("1");

        // Assert
        result.test()
            .assertValue(a -> a.getId().equals("1"))
            .assertComplete();
        verify(mockDao).getById("1");
    }

    @Test
    public void getAllApiaries_returnsFlowableList() {
        // Arrange
        Apiary apiary1 = new Apiary();
        apiary1.setId("1");
        apiary1.setName("Apiary 1");

        Apiary apiary2 = new Apiary();
        apiary2.setId("2");
        apiary2.setName("Apiary 2");

        List<Apiary> apiaries = Arrays.asList(apiary1, apiary2);
        when(mockDao.getAll()).thenReturn(Flowable.just(apiaries));

        // Act
        Flowable<List<Apiary>> result = repository.getAllApiaries();

        // Assert
        result.test()
            .assertValue(list -> list.size() == 2)
            .assertNotComplete();
        verify(mockDao).getAll();
    }

    @Test
    public void getApiariesByLocation_returnsFilteredList() {
        // Arrange
        String location = "Test Location";
        Apiary apiary = new Apiary();
        apiary.setId("1");
        apiary.setLocation(location);

        when(mockDao.getByLocation(location)).thenReturn(Single.just(Arrays.asList(apiary)));

        // Act
        Single<List<Apiary>> result = repository.getApiariesByLocation(location);

        // Assert
        result.test()
            .assertValue(list -> list.size() == 1)
            .assertComplete();
        verify(mockDao).getByLocation(location);
    }

    @Test
    public void getApiaryCount_returnsCount() {
        // Arrange
        when(mockDao.getCount()).thenReturn(Single.just(5));

        // Act
        Single<Integer> result = repository.getApiaryCount();

        // Assert
        result.test()
            .assertValue(count -> count == 5)
            .assertComplete();
        verify(mockDao).getCount();
    }
}
