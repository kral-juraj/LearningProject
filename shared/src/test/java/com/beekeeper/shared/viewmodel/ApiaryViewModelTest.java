package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.dao.ApiaryDao;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ApiaryViewModel.
 * Tests business logic with mocked DAO and real Repository.
 */
class ApiaryViewModelTest {

    @Mock
    private ApiaryDao apiaryDao;

    private ApiaryViewModel viewModel;

    // Test scheduler that executes immediately (synchronous)
    private final SchedulerProvider testScheduler = new SchedulerProvider() {
        @Override
        public Scheduler io() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler mainThread() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler computation() {
            return Schedulers.trampoline();
        }
    };

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Use real repository with mocked DAO
        ApiaryRepository repository = new ApiaryRepository(apiaryDao);
        viewModel = new ApiaryViewModel(repository, testScheduler);
    }

    @Test
    void testLoadApiaries_callsDAO() {
        // Given
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.loadApiaries();

        // Then
        verify(apiaryDao, times(1)).getAll();
    }

    @Test
    void testCreateApiary_validInput_callsDAO() {
        // Given
        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.createApiary("Test Včelnica", "Bratislava", 48.1486, 17.1077);

        // Then
        verify(apiaryDao, times(1)).insert(any(Apiary.class));
        verify(apiaryDao, times(1)).getAll(); // Reload after create
    }

    @Test
    void testCreateApiary_emptyName_doesNotCallDAO() {
        // When
        viewModel.createApiary("", "Bratislava", 0, 0);

        // Then: Should NOT call DAO (validation failed)
        verify(apiaryDao, never()).insert(any());
    }

    @Test
    void testCreateApiary_nullName_doesNotCallDAO() {
        // When
        viewModel.createApiary(null, "Bratislava", 0, 0);

        // Then: Should NOT call DAO (validation failed)
        verify(apiaryDao, never()).insert(any());
    }

    @Test
    void testUpdateApiary_validInput_callsDAO() {
        // Given
        Apiary apiary = new Apiary();
        apiary.setId("test-id");
        apiary.setName("Updated Včelnica");
        apiary.setLocation("New Location");

        when(apiaryDao.update(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.updateApiary(apiary);

        // Then
        verify(apiaryDao, times(1)).update(apiary);
        verify(apiaryDao, times(1)).getAll(); // Reload after update
    }

    @Test
    void testUpdateApiary_emptyName_doesNotCallDAO() {
        // Given
        Apiary apiary = new Apiary();
        apiary.setId("test-id");
        apiary.setName("");

        // When
        viewModel.updateApiary(apiary);

        // Then: Should NOT call DAO (validation failed)
        verify(apiaryDao, never()).update(any());
    }

    @Test
    void testDeleteApiary_callsDAO() {
        // Given
        Apiary apiary = new Apiary();
        apiary.setId("test-id");
        apiary.setName("To Delete");

        when(apiaryDao.delete(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.deleteApiary(apiary);

        // Then
        verify(apiaryDao, times(1)).delete(apiary);
        verify(apiaryDao, times(1)).getAll(); // Reload after delete
    }
}
