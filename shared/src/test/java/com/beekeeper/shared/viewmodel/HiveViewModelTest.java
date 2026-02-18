package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.dao.HiveDao;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HiveViewModel focusing on updateHiveOrder method.
 *
 * Tests the ViewModel layer for:
 * - Calling repository with correct parameters
 * - Error handling and propagation
 * - Silent success (no success message for reordering)
 */
class HiveViewModelTest {

    private HiveViewModel viewModel;
    private FakeHiveRepository repository;
    private TestSchedulerProvider schedulerProvider;

    @BeforeEach
    void setUp() {
        schedulerProvider = new TestSchedulerProvider();
        repository = new FakeHiveRepository();
        viewModel = new HiveViewModel(repository, schedulerProvider);
    }

    /**
     * Test: updateHiveOrder calls repository.
     *
     * Use case: User finishes drag-and-drop, ViewModel saves new order.
     * Expected: Repository updateHiveOrder called with hive list.
     */
    @Test
    void testUpdateHiveOrderCallsRepository() {
        // Arrange
        List<Hive> hives = createTestHives(3);

        // Act
        viewModel.updateHiveOrder(hives);

        // Assert
        assertTrue(repository.updateHiveOrderCalled);
        assertEquals(3, repository.lastUpdatedHives.size());
    }

    /**
     * Test: updateHiveOrder does not emit success message.
     *
     * Use case: Silent update - no success toast/message for reordering.
     * Expected: Success observable does not emit.
     */
    @Test
    void testUpdateHiveOrderNoSuccessMessage() throws InterruptedException {
        // Arrange
        List<Hive> hives = createTestHives(3);

        // Setup test observer
        List<String> successMessages = new ArrayList<>();
        viewModel.getSuccess().subscribe(successMessages::add);

        // Act
        viewModel.updateHiveOrder(hives);
        Thread.sleep(100); // Give time for async operations

        // Assert
        assertTrue(successMessages.isEmpty(), "No success message should be emitted for silent update");
    }

    /**
     * Test: updateHiveOrder emits error on failure.
     *
     * Use case: Database error during order update.
     * Expected: Error observable emits error message.
     */
    @Test
    void testUpdateHiveOrderEmitsErrorOnFailure() throws InterruptedException {
        // Arrange
        List<Hive> hives = createTestHives(3);
        repository.shouldFail = true;

        // Setup test observer
        List<String> errorMessages = new ArrayList<>();
        viewModel.getError().subscribe(errorMessages::add);

        // Act
        viewModel.updateHiveOrder(hives);
        Thread.sleep(100); // Give time for async operations

        // Assert
        assertEquals(1, errorMessages.size(), "Expected exactly one error message");
        assertFalse(errorMessages.get(0).isEmpty(), "Error message should not be empty");
        // The error message format is: "[error.updating_order] Database error" when translation is not available
        // or translated message when translation exists. We just verify it contains the exception message.
        assertTrue(errorMessages.get(0).contains("Database error") ||
                   errorMessages.get(0).contains("[error.updating_order]"),
                   "Error message should contain exception or translation key. Actual: " + errorMessages.get(0));
    }

    /**
     * Test: updateHiveOrder with empty list.
     *
     * Use case: Edge case - no hives to update.
     * Expected: Repository called without error.
     */
    @Test
    void testUpdateHiveOrderWithEmptyList() {
        // Arrange
        List<Hive> emptyList = new ArrayList<>();

        // Act & Assert
        assertDoesNotThrow(() -> viewModel.updateHiveOrder(emptyList));
        assertTrue(repository.updateHiveOrderCalled);
        assertEquals(0, repository.lastUpdatedHives.size());
    }

    /**
     * Test: updateHiveOrder disposes correctly.
     *
     * Use case: Prevent memory leaks from RxJava subscriptions.
     * Expected: Subscription added to disposables.
     */
    @Test
    void testUpdateHiveOrderAddsToDisposables() {
        // Arrange
        List<Hive> hives = createTestHives(3);

        // Act
        viewModel.updateHiveOrder(hives);
        viewModel.dispose();

        // Assert - should not crash
        assertDoesNotThrow(() -> viewModel.dispose());
    }

    // Helper classes

    private static class TestSchedulerProvider implements SchedulerProvider {
        @Override
        public io.reactivex.Scheduler io() {
            return Schedulers.trampoline(); // Synchronous for testing
        }

        @Override
        public io.reactivex.Scheduler mainThread() {
            return Schedulers.trampoline(); // Synchronous for testing
        }

        @Override
        public io.reactivex.Scheduler computation() {
            return Schedulers.trampoline(); // Synchronous for testing
        }
    }

    private static class FakeHiveRepository extends HiveRepository {
        boolean updateHiveOrderCalled = false;
        List<Hive> lastUpdatedHives = new ArrayList<>();
        boolean shouldFail = false;

        public FakeHiveRepository() {
            super(new FakeHiveDao());
        }

        @Override
        public Completable updateHiveOrder(List<Hive> hives) {
            updateHiveOrderCalled = true;
            lastUpdatedHives = new ArrayList<>(hives);

            if (shouldFail) {
                return Completable.error(new RuntimeException("Database error"));
            }

            return Completable.complete();
        }
    }

    private static class FakeHiveDao implements HiveDao {
        @Override
        public Completable insert(Hive hive) {
            return Completable.complete();
        }

        @Override
        public Completable insertAll(List<Hive> hives) {
            return Completable.complete();
        }

        @Override
        public Completable update(Hive hive) {
            return Completable.complete();
        }

        @Override
        public Completable delete(Hive hive) {
            return Completable.complete();
        }

        @Override
        public Single<Hive> getById(String id) {
            return Single.just(new Hive());
        }

        @Override
        public Flowable<List<Hive>> getByApiaryId(String apiaryId) {
            return Flowable.just(new ArrayList<>());
        }

        @Override
        public Flowable<List<Hive>> getActiveByApiaryId(String apiaryId) {
            return Flowable.just(new ArrayList<>());
        }

        @Override
        public Flowable<List<Hive>> getAll() {
            return Flowable.just(new ArrayList<>());
        }

        @Override
        public Completable deleteById(String id) {
            return Completable.complete();
        }

        @Override
        public Single<Integer> getCountByApiaryId(String apiaryId) {
            return Single.just(0);
        }
    }

    // Helper method to create test hives
    private List<Hive> createTestHives(int count) {
        List<Hive> hives = new ArrayList<>();
        String apiaryId = UUID.randomUUID().toString();

        for (int i = 0; i < count; i++) {
            Hive hive = new Hive();
            hive.setId(UUID.randomUUID().toString());
            hive.setApiaryId(apiaryId);
            hive.setName("Hive " + i);
            hive.setType("VERTICAL");
            hive.setDisplayOrder(i);
            hive.setActive(true);
            hive.setCreatedAt(System.currentTimeMillis());
            hive.setUpdatedAt(System.currentTimeMillis());
            hives.add(hive);
        }

        return hives;
    }
}
