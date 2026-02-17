package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.HiveDao;
import com.beekeeper.shared.entity.Hive;
import io.reactivex.Completable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for HiveRepository focusing on updateHiveOrder method.
 *
 * Tests the business logic for:
 * - Batch updating hive display order after drag-and-drop
 * - Automatic timestamp management (updatedAt)
 * - Correct delegation to DAO layer
 */
class HiveRepositoryTest {

    @Mock
    private HiveDao hiveDao;

    @Captor
    private ArgumentCaptor<List<Hive>> hivesCaptor;

    private HiveRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new HiveRepository(hiveDao);
    }

    /**
     * Test: updateHiveOrder sets updatedAt timestamp for all hives.
     *
     * Use case: User drags hives to reorder them, all modified hives get timestamp.
     * Expected: updatedAt set for each hive in the list.
     */
    @Test
    void testUpdateHiveOrderSetsTimestamps() {
        // Arrange
        List<Hive> hives = createTestHives(3);
        long beforeTimestamp = System.currentTimeMillis();
        when(hiveDao.insertAll(any())).thenReturn(Completable.complete());

        // Act
        repository.updateHiveOrder(hives).blockingAwait();

        // Assert
        verify(hiveDao).insertAll(hivesCaptor.capture());
        List<Hive> capturedHives = hivesCaptor.getValue();

        for (Hive hive : capturedHives) {
            assertTrue(hive.getUpdatedAt() >= beforeTimestamp,
                "updatedAt should be set to current timestamp");
        }
    }

    /**
     * Test: updateHiveOrder delegates to DAO insertAll.
     *
     * Use case: Repository uses INSERT OR REPLACE strategy for batch updates.
     * Expected: DAO insertAll method called with correct hive list.
     */
    @Test
    void testUpdateHiveOrderDelegatesToDao() {
        // Arrange
        List<Hive> hives = createTestHives(5);
        when(hiveDao.insertAll(any())).thenReturn(Completable.complete());

        // Act
        repository.updateHiveOrder(hives).blockingAwait();

        // Assert
        verify(hiveDao).insertAll(hivesCaptor.capture());
        List<Hive> capturedHives = hivesCaptor.getValue();
        assertEquals(5, capturedHives.size());
    }

    /**
     * Test: updateHiveOrder preserves hive data.
     *
     * Use case: Only displayOrder should change, other fields preserved.
     * Expected: Hive names, IDs, and other fields unchanged.
     */
    @Test
    void testUpdateHiveOrderPreservesHiveData() {
        // Arrange
        List<Hive> hives = createTestHives(3);
        hives.get(0).setName("Ležan");
        hives.get(1).setName("Oddielok");
        hives.get(2).setName("Úľ 3");
        when(hiveDao.insertAll(any())).thenReturn(Completable.complete());

        // Act
        repository.updateHiveOrder(hives).blockingAwait();

        // Assert
        verify(hiveDao).insertAll(hivesCaptor.capture());
        List<Hive> capturedHives = hivesCaptor.getValue();
        assertEquals("Ležan", capturedHives.get(0).getName());
        assertEquals("Oddielok", capturedHives.get(1).getName());
        assertEquals("Úľ 3", capturedHives.get(2).getName());
    }

    /**
     * Test: updateHiveOrder with empty list.
     *
     * Use case: Edge case - no hives to update.
     * Expected: Completes without error.
     */
    @Test
    void testUpdateHiveOrderWithEmptyList() {
        // Arrange
        List<Hive> emptyList = new ArrayList<>();
        when(hiveDao.insertAll(any())).thenReturn(Completable.complete());

        // Act & Assert
        assertDoesNotThrow(() -> repository.updateHiveOrder(emptyList).blockingAwait());
        verify(hiveDao).insertAll(emptyList);
    }

    /**
     * Test: updateHiveOrder propagates DAO errors.
     *
     * Use case: Database error during batch update.
     * Expected: Error propagated to caller for proper handling.
     */
    @Test
    void testUpdateHiveOrderPropagatesErrors() {
        // Arrange
        List<Hive> hives = createTestHives(3);
        RuntimeException dbError = new RuntimeException("Database connection failed");
        when(hiveDao.insertAll(any())).thenReturn(Completable.error(dbError));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            repository.updateHiveOrder(hives).blockingAwait();
        });
        assertEquals("Database connection failed", exception.getMessage());
    }

    /**
     * Test: updateHiveOrder with single hive.
     *
     * Use case: Only one hive in apiary.
     * Expected: Still updates timestamp correctly.
     */
    @Test
    void testUpdateHiveOrderWithSingleHive() {
        // Arrange
        List<Hive> hives = createTestHives(1);
        hives.get(0).setDisplayOrder(0);
        when(hiveDao.insertAll(any())).thenReturn(Completable.complete());

        // Act
        repository.updateHiveOrder(hives).blockingAwait();

        // Assert
        verify(hiveDao).insertAll(hivesCaptor.capture());
        List<Hive> capturedHives = hivesCaptor.getValue();
        assertEquals(1, capturedHives.size());
        assertTrue(capturedHives.get(0).getUpdatedAt() > 0);
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
            hive.setUpdatedAt(System.currentTimeMillis() - 1000); // Old timestamp
            hives.add(hive);
        }

        return hives;
    }
}
