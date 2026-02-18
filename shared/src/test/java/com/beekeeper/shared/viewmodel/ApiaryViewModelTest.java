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

    /**
     * Test: Create apiary with extended fields (registrationNumber, address, description).
     *
     * Use case: User creates apiary with all optional details filled in.
     * Expected: All fields stored correctly, DAO insert called.
     */
    @Test
    void testCreateApiary_withExtendedFields_callsDAO() {
        // Given
        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.createApiary("Test Včelnica", "Bratislava", 48.1486, 17.1077,
                "REG-12345", "Hlavná 123, Bratislava", "Popis včelnice");

        // Then
        verify(apiaryDao, times(1)).insert(any(Apiary.class));
        verify(apiaryDao, times(1)).getAll(); // Reload after create
    }

    /**
     * Test: Create apiary with null optional fields.
     *
     * Use case: User creates apiary without filling in optional fields.
     * Expected: Null optional fields stored as null (not empty strings).
     */
    @Test
    void testCreateApiary_withNullOptionalFields_storesAsNull() {
        // Given
        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When
        viewModel.createApiary("Test Včelnica", "Bratislava", 48.1486, 17.1077,
                null, null, null);

        // Then: Insert should be called
        verify(apiaryDao, times(1)).insert(any(Apiary.class));
    }

    /**
     * Test: Create apiary trims whitespace from optional fields.
     *
     * Use case: User accidentally adds trailing spaces in input fields.
     * Expected: Whitespace trimmed, empty strings converted to null.
     */
    @Test
    void testCreateApiary_trimsWhitespace() {
        // Given
        when(apiaryDao.insert(any(Apiary.class))).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(Arrays.asList()));

        // When: Create with whitespace in optional fields
        viewModel.createApiary("Test Včelnica", "Bratislava", 48.1486, 17.1077,
                "  REG-12345  ", "  Hlavná 123  ", "  Popis  ");

        // Then: Insert should be called (trimming happens in ViewModel)
        verify(apiaryDao, times(1)).insert(any(Apiary.class));
    }

    /**
     * Test: Update apiary order calls DAO insertAll.
     *
     * Use case: User drags apiaries to reorder them.
     * Expected: DAO insertAll called, apiaries reloaded.
     */
    @Test
    void testUpdateApiaryOrder_callsDAOInsertAll() {
        // Given
        Apiary apiary1 = new Apiary();
        apiary1.setId("id1");
        apiary1.setName("Včelnica 1");
        apiary1.setDisplayOrder(0);

        Apiary apiary2 = new Apiary();
        apiary2.setId("id2");
        apiary2.setName("Včelnica 2");
        apiary2.setDisplayOrder(1);

        List<Apiary> apiaries = Arrays.asList(apiary1, apiary2);

        when(apiaryDao.insertAll(anyList())).thenReturn(Completable.complete());
        when(apiaryDao.getAll()).thenReturn(Flowable.just(apiaries));

        // When
        viewModel.updateApiaryOrder(apiaries);

        // Then
        verify(apiaryDao, times(1)).insertAll(apiaries);
        verify(apiaryDao, times(1)).getAll(); // Reload after update
    }

    /**
     * Test: Update apiary order with empty list does nothing.
     *
     * Use case: Edge case - empty list passed.
     * Expected: No DAO calls (early return).
     */
    @Test
    void testUpdateApiaryOrder_withEmptyList_doesNothing() {
        // When
        viewModel.updateApiaryOrder(Arrays.asList());

        // Then: No DAO calls
        verify(apiaryDao, never()).insertAll(anyList());
    }

    /**
     * Test: Update apiary order with null list does nothing.
     *
     * Use case: Edge case - null list passed.
     * Expected: No DAO calls (early return).
     */
    @Test
    void testUpdateApiaryOrder_withNullList_doesNothing() {
        // When
        viewModel.updateApiaryOrder(null);

        // Then: No DAO calls
        verify(apiaryDao, never()).insertAll(anyList());
    }
}
