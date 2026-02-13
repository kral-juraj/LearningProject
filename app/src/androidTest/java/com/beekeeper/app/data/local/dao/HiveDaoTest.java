package com.beekeeper.app.data.local.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.beekeeper.app.data.local.db.AppDatabase;
import com.beekeeper.app.data.local.entity.Apiary;
import com.beekeeper.app.data.local.entity.Hive;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class HiveDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private HiveDao hiveDao;
    private ApiaryDao apiaryDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        hiveDao = database.hiveDao();
        apiaryDao = database.apiaryDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetHive() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        Hive hive = createTestHive("1", "apiary-1", "U1", "Vertical");

        // Act
        hiveDao.insert(hive).blockingAwait();
        Hive loaded = hiveDao.getById("1").blockingGet();

        // Assert
        assertNotNull(loaded);
        assertEquals("1", loaded.getId());
        assertEquals("U1", loaded.getName());
        assertEquals("Vertical", loaded.getType());
    }

    @Test
    public void getByApiaryId_returnsOnlyHivesFromThatApiary() {
        // Arrange
        Apiary apiary1 = createTestApiary("apiary-1", "Apiary 1");
        Apiary apiary2 = createTestApiary("apiary-2", "Apiary 2");
        apiaryDao.insert(apiary1).blockingAwait();
        apiaryDao.insert(apiary2).blockingAwait();

        hiveDao.insert(createTestHive("1", "apiary-1", "U1", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("2", "apiary-1", "U2", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("3", "apiary-2", "U3", "Horizontal")).blockingAwait();

        // Act
        List<Hive> apiary1Hives = hiveDao.getByApiaryId("apiary-1").blockingFirst();

        // Assert
        assertEquals(2, apiary1Hives.size());
        assertTrue(apiary1Hives.stream().allMatch(h -> h.getApiaryId().equals("apiary-1")));
    }

    @Test
    public void getActiveByApiaryId_returnsOnlyActiveHives() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        Hive activeHive = createTestHive("1", "apiary-1", "U1", "Vertical");
        activeHive.setActive(true);

        Hive inactiveHive = createTestHive("2", "apiary-1", "U2", "Vertical");
        inactiveHive.setActive(false);

        hiveDao.insert(activeHive).blockingAwait();
        hiveDao.insert(inactiveHive).blockingAwait();

        // Act
        List<Hive> activeHives = hiveDao.getActiveByApiaryId("apiary-1").blockingFirst();

        // Assert
        assertEquals(1, activeHives.size());
        assertTrue(activeHives.get(0).isActive());
    }

    @Test
    public void updateHive_changesArePersisted() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        Hive hive = createTestHive("1", "apiary-1", "U1", "Vertical");
        hiveDao.insert(hive).blockingAwait();

        // Act
        hive.setName("U1-Updated");
        hive.setQueenId("M1");
        hive.setQueenYear(2024);
        hiveDao.update(hive).blockingAwait();

        Hive loaded = hiveDao.getById("1").blockingGet();

        // Assert
        assertEquals("U1-Updated", loaded.getName());
        assertEquals("M1", loaded.getQueenId());
        assertEquals(2024, loaded.getQueenYear());
    }

    @Test
    public void deleteHive_removesFromDatabase() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        Hive hive = createTestHive("1", "apiary-1", "U1", "Vertical");
        hiveDao.insert(hive).blockingAwait();

        // Act
        hiveDao.delete(hive).blockingAwait();
        List<Hive> hives = hiveDao.getAll().blockingFirst();

        // Assert
        assertEquals(0, hives.size());
    }

    @Test
    public void deleteApiary_cascadeDeletesHives() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        hiveDao.insert(createTestHive("1", "apiary-1", "U1", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("2", "apiary-1", "U2", "Vertical")).blockingAwait();

        // Act
        apiaryDao.delete(apiary).blockingAwait();
        List<Hive> hives = hiveDao.getAll().blockingFirst();

        // Assert
        assertEquals(0, hives.size());
    }

    @Test
    public void getCount_returnsCorrectCount() {
        // Arrange
        Apiary apiary = createTestApiary("apiary-1", "Test Apiary");
        apiaryDao.insert(apiary).blockingAwait();

        hiveDao.insert(createTestHive("1", "apiary-1", "U1", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("2", "apiary-1", "U2", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("3", "apiary-1", "U3", "Horizontal")).blockingAwait();

        // Act
        int count = hiveDao.getCount().blockingGet();

        // Assert
        assertEquals(3, count);
    }

    @Test
    public void getCountByApiaryId_returnsCorrectCount() {
        // Arrange
        Apiary apiary1 = createTestApiary("apiary-1", "Apiary 1");
        Apiary apiary2 = createTestApiary("apiary-2", "Apiary 2");
        apiaryDao.insert(apiary1).blockingAwait();
        apiaryDao.insert(apiary2).blockingAwait();

        hiveDao.insert(createTestHive("1", "apiary-1", "U1", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("2", "apiary-1", "U2", "Vertical")).blockingAwait();
        hiveDao.insert(createTestHive("3", "apiary-2", "U3", "Horizontal")).blockingAwait();

        // Act
        int apiary1Count = hiveDao.getCountByApiaryId("apiary-1").blockingGet();

        // Assert
        assertEquals(2, apiary1Count);
    }

    private Apiary createTestApiary(String id, String name) {
        Apiary apiary = new Apiary();
        apiary.setId(id);
        apiary.setName(name);
        apiary.setLocation("Test Location");
        apiary.setLatitude(0);
        apiary.setLongitude(0);
        apiary.setCreatedAt(System.currentTimeMillis());
        apiary.setUpdatedAt(System.currentTimeMillis());
        return apiary;
    }

    private Hive createTestHive(String id, String apiaryId, String name, String type) {
        Hive hive = new Hive();
        hive.setId(id);
        hive.setApiaryId(apiaryId);
        hive.setName(name);
        hive.setType(type);
        hive.setQueenId("");
        hive.setQueenYear(0);
        hive.setActive(true);
        hive.setCreatedAt(System.currentTimeMillis());
        hive.setUpdatedAt(System.currentTimeMillis());
        return hive;
    }
}
