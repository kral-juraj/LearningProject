package com.beekeeper.app.presentation.apiary;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.data.repository.ApiaryRepository;

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
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiaryViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiaryRepository mockRepository;

    @Mock
    private Observer<List<Apiary>> apiariesObserver;

    @Mock
    private Observer<Boolean> loadingObserver;

    @Mock
    private Observer<String> errorObserver;

    @Mock
    private Observer<String> successObserver;

    private ApiaryViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup RxJava to use immediate scheduler for testing
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

        // Create ViewModel with mocked repository
        viewModel = new ApiaryViewModel(mockRepository);

        // Observe LiveData
        viewModel.getApiaries().observeForever(apiariesObserver);
        viewModel.getLoading().observeForever(loadingObserver);
        viewModel.getError().observeForever(errorObserver);
        viewModel.getSuccess().observeForever(successObserver);
    }

    @Test
    public void loadApiaries_success_updatesLiveData() {
        // Arrange
        Apiary apiary1 = createTestApiary("1", "Apiary 1", "Location 1");
        Apiary apiary2 = createTestApiary("2", "Apiary 2", "Location 2");
        List<Apiary> apiaries = Arrays.asList(apiary1, apiary2);

        when(mockRepository.getAllApiaries()).thenReturn(Flowable.just(apiaries));

        // Act
        viewModel.loadApiaries();

        // Assert
        verify(mockRepository).getAllApiaries();
        verify(apiariesObserver).onChanged(apiaries);
        verify(loadingObserver).onChanged(true);
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void loadApiaries_error_setsErrorMessage() {
        // Arrange
        String errorMessage = "Database error";
        when(mockRepository.getAllApiaries())
            .thenReturn(Flowable.error(new Exception(errorMessage)));

        // Act
        viewModel.loadApiaries();

        // Assert
        verify(errorObserver).onChanged(anyString());
        verify(loadingObserver).onChanged(false);
    }

    @Test
    public void createApiary_validInput_success() {
        // Arrange
        when(mockRepository.insertApiary(any(Apiary.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getAllApiaries())
            .thenReturn(Flowable.just(Arrays.asList()));

        // Act
        viewModel.createApiary("Test Apiary", "Test Location", 0, 0);

        // Assert
        verify(mockRepository).insertApiary(any(Apiary.class));
        verify(successObserver).onChanged("Včelnica úspešne vytvorená");
    }

    @Test
    public void createApiary_emptyName_setsError() {
        // Act
        viewModel.createApiary("", "Location", 0, 0);

        // Assert
        verify(errorObserver).onChanged("Názov včelnice nemôže byť prázdny");
    }

    @Test
    public void createApiary_nullName_setsError() {
        // Act
        viewModel.createApiary(null, "Location", 0, 0);

        // Assert
        verify(errorObserver).onChanged("Názov včelnice nemôže byť prázdny");
    }

    @Test
    public void updateApiary_validInput_success() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Old Name", "Old Location");
        when(mockRepository.updateApiary(any(Apiary.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getAllApiaries())
            .thenReturn(Flowable.just(Arrays.asList(apiary)));

        // Act
        viewModel.updateApiary(apiary);

        // Assert
        verify(mockRepository).updateApiary(apiary);
        verify(successObserver).onChanged("Včelnica úspešne aktualizovaná");
    }

    @Test
    public void updateApiary_emptyName_setsError() {
        // Arrange
        Apiary apiary = createTestApiary("1", "", "Location");

        // Act
        viewModel.updateApiary(apiary);

        // Assert
        verify(errorObserver).onChanged("Názov včelnice nemôže byť prázdny");
    }

    @Test
    public void deleteApiary_success() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Test", "Location");
        when(mockRepository.deleteApiary(any(Apiary.class)))
            .thenReturn(Completable.complete());
        when(mockRepository.getAllApiaries())
            .thenReturn(Flowable.just(Arrays.asList()));

        // Act
        viewModel.deleteApiary(apiary);

        // Assert
        verify(mockRepository).deleteApiary(apiary);
        verify(successObserver).onChanged("Včelnica úspešne zmazaná");
    }

    @Test
    public void deleteApiary_error_setsErrorMessage() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Test", "Location");
        when(mockRepository.deleteApiary(any(Apiary.class)))
            .thenReturn(Completable.error(new Exception("Delete failed")));

        // Act
        viewModel.deleteApiary(apiary);

        // Assert
        verify(errorObserver).onChanged(anyString());
    }

    private Apiary createTestApiary(String id, String name, String location) {
        Apiary apiary = new Apiary();
        apiary.setId(id);
        apiary.setName(name);
        apiary.setLocation(location);
        apiary.setLatitude(0);
        apiary.setLongitude(0);
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        return apiary;
    }
}
