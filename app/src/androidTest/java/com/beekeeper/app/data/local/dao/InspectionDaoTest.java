package com.beekeeper.app.data.local.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.beekeeper.app.data.local.db.AppDatabase;
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.data.local.entity.Hive;
import com.beekeeper.app.data.local.entity.Inspection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class InspectionDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private InspectionDao inspectionDao;
    private HiveDao hiveDao;
    private ApiaryDao apiaryDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        inspectionDao = database.inspectionDao();
        hiveDao = database.hiveDao();
        apiaryDao = database.apiaryDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetInspection() {
        // Arrange
        setupApiaryAndHive();
        Inspection inspection = createTestInspection("1", "hive-1", System.currentTimeMillis());

        // Act
        inspectionDao.insert(inspection).blockingAwait();
        Inspection loaded = inspectionDao.getById("1").blockingGet();

        // Assert
        assertNotNull(loaded);
        assertEquals("1", loaded.getId());
        assertEquals("hive-1", loaded.getHiveId());
        assertEquals(7, loaded.getStrengthEstimate());
    }

    @Test
    public void getByHiveId_returnsOnlyInspectionsFromThatHive() {
        // Arrange
        setupApiaryAndHive();
        setupSecondHive();

        long now = System.currentTimeMillis();
        inspectionDao.insert(createTestInspection("1", "hive-1", now)).blockingAwait();
        inspectionDao.insert(createTestInspection("2", "hive-1", now - 86400000)).blockingAwait();
        inspectionDao.insert(createTestInspection("3", "hive-2", now)).blockingAwait();

        // Act
        List<Inspection> hive1Inspections = inspectionDao.getByHiveId("hive-1").blockingFirst();

        // Assert
        assertEquals(2, hive1Inspections.size());
        assertTrue(hive1Inspections.stream().allMatch(i -> i.getHiveId().equals("hive-1")));
    }

    @Test
    public void getByHiveIdAndDateRange_filtersCorrectly() {
        // Arrange
        setupApiaryAndHive();

        long now = System.currentTimeMillis();
        long threeDaysAgo = now - (3 * 86400000L);
        long fiveDaysAgo = now - (5 * 86400000L);
        long sevenDaysAgo = now - (7 * 86400000L);

        inspectionDao.insert(createTestInspection("1", "hive-1", now)).blockingAwait();
        inspectionDao.insert(createTestInspection("2", "hive-1", threeDaysAgo)).blockingAwait();
        inspectionDao.insert(createTestInspection("3", "hive-1", sevenDaysAgo)).blockingAwait();

        // Act - get inspections from last 4 days
        List<Inspection> recent = inspectionDao.getByHiveIdAndDateRange("hive-1", fiveDaysAgo, now).blockingGet();

        // Assert
        assertEquals(2, recent.size());
    }

    @Test
    public void getRecent_returnsLimitedAndOrderedInspections() {
        // Arrange
        setupApiaryAndHive();

        long now = System.currentTimeMillis();
        inspectionDao.insert(createTestInspection("1", "hive-1", now)).blockingAwait();
        inspectionDao.insert(createTestInspection("2", "hive-1", now - 86400000)).blockingAwait();
        inspectionDao.insert(createTestInspection("3", "hive-1", now - 2 * 86400000)).blockingAwait();
        inspectionDao.insert(createTestInspection("4", "hive-1", now - 3 * 86400000)).blockingAwait();

        // Act
        List<Inspection> recent = inspectionDao.getRecent(2).blockingFirst();

        // Assert
        assertEquals(2, recent.size());
        // Should be ordered by date descending
        assertTrue(recent.get(0).getInspectionDate() >= recent.get(1).getInspectionDate());
    }

    @Test
    public void updateInspection_changesArePersisted() {
        // Arrange
        setupApiaryAndHive();
        Inspection inspection = createTestInspection("1", "hive-1", System.currentTimeMillis());
        inspectionDao.insert(inspection).blockingAwait();

        // Act
        inspection.setStrengthEstimate(9);
        inspection.setQueenSeen(true);
        inspection.setNotes("Updated notes");
        inspectionDao.update(inspection).blockingAwait();

        Inspection loaded = inspectionDao.getById("1").blockingGet();

        // Assert
        assertEquals(9, loaded.getStrengthEstimate());
        assertTrue(loaded.isQueenSeen());
        assertEquals("Updated notes", loaded.getNotes());
    }

    @Test
    public void deleteInspection_removesFromDatabase() {
        // Arrange
        setupApiaryAndHive();
        Inspection inspection = createTestInspection("1", "hive-1", System.currentTimeMillis());
        inspectionDao.insert(inspection).blockingAwait();

        // Act
        inspectionDao.delete(inspection).blockingAwait();
        List<Inspection> inspections = inspectionDao.getByHiveId("hive-1").blockingFirst();

        // Assert
        assertEquals(0, inspections.size());
    }

    @Test
    public void deleteHive_cascadeDeletesInspections() {
        // Arrange
        setupApiaryAndHive();
        long now = System.currentTimeMillis();
        inspectionDao.insert(createTestInspection("1", "hive-1", now)).blockingAwait();
        inspectionDao.insert(createTestInspection("2", "hive-1", now - 86400000)).blockingAwait();

        Hive hive = hiveDao.getById("hive-1").blockingGet();

        // Act
        hiveDao.delete(hive).blockingAwait();

        // Give database time to cascade
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Try to get inspections (should throw or return empty)
        List<Inspection> inspections = inspectionDao.getByHiveId("hive-1").blockingFirst();

        // Assert
        assertEquals(0, inspections.size());
    }

    @Test
    public void getCountByHiveId_returnsCorrectCount() {
        // Arrange
        setupApiaryAndHive();
        long now = System.currentTimeMillis();

        inspectionDao.insert(createTestInspection("1", "hive-1", now)).blockingAwait();
        inspectionDao.insert(createTestInspection("2", "hive-1", now - 86400000)).blockingAwait();
        inspectionDao.insert(createTestInspection("3", "hive-1", now - 2 * 86400000)).blockingAwait();

        // Act
        int count = inspectionDao.getCountByHiveId("hive-1").blockingGet();

        // Assert
        assertEquals(3, count);
    }

    private void setupApiaryAndHive() {
        Apiary apiary = new Apiary();
        apiary.setId("apiary-1");
        apiary.setName("Test Apiary");
        apiary.setLocation("Test Location");
        apiary.setLatitude(0);
        apiary.setLongitude(0);
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        apiaryDao.insert(apiary).blockingAwait();

        Hive hive = new Hive();
        hive.setId("hive-1");
        hive.setApiaryId("apiary-1");
        hive.setName("U1");
        hive.setType("Vertical");
        hive.setQueenId("");
        hive.setQueenYear(0);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveDao.insert(hive).blockingAwait();
    }

    private void setupSecondHive() {
        Hive hive = new Hive();
        hive.setId("hive-2");
        hive.setApiaryId("apiary-1");
        hive.setName("U2");
        hive.setType("Vertical");
        hive.setQueenId("");
        hive.setQueenYear(0);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        hiveDao.insert(hive).blockingAwait();
    }

    private Inspection createTestInspection(String id, String hiveId, long date) {
        Inspection inspection = new Inspection();
        inspection.setId(id);
        inspection.setHiveId(hiveId);
        inspection.setInspectionDate(date);
        inspection.setTemperature(22.0);
        inspection.setStrengthEstimate(7);
        inspection.setFoodStoresKg(5.0);
        inspection.setBroodFrames(8);
        inspection.setCappedBroodDm(50);
        inspection.setUncappedBroodDm(30);
        inspection.setPollenFrames(2);
        inspection.setTotalFrames(10);
        inspection.setQueenSeen(false);
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
