package com.beekeeper.app.data.local.dao;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.beekeeper.app.data.local.db.AppDatabase;
import com.beekeeper.app.data.local.entity.Apiary;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ApiaryDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;
    private ApiaryDao apiaryDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        apiaryDao = database.apiaryDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetApiary() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Test Apiary", "Test Location");

        // Act
        apiaryDao.insert(apiary).blockingAwait();
        Apiary loaded = apiaryDao.getById("1").blockingGet();

        // Assert
        assertNotNull(loaded);
        assertEquals("1", loaded.getId());
        assertEquals("Test Apiary", loaded.getName());
        assertEquals("Test Location", loaded.getLocation());
    }

    @Test
    public void insertMultipleApiaries_getAllReturnsAll() {
        // Arrange
        Apiary apiary1 = createTestApiary("1", "Apiary 1", "Location 1");
        Apiary apiary2 = createTestApiary("2", "Apiary 2", "Location 2");
        Apiary apiary3 = createTestApiary("3", "Apiary 3", "Location 3");

        // Act
        apiaryDao.insert(apiary1).blockingAwait();
        apiaryDao.insert(apiary2).blockingAwait();
        apiaryDao.insert(apiary3).blockingAwait();

        List<Apiary> apiaries = apiaryDao.getAll().blockingFirst();

        // Assert
        assertEquals(3, apiaries.size());
    }

    @Test
    public void updateApiary_changesArePersisted() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Original Name", "Original Location");
        apiaryDao.insert(apiary).blockingAwait();

        // Act
        apiary.setName("Updated Name");
        apiary.setLocation("Updated Location");
        apiaryDao.update(apiary).blockingAwait();

        Apiary loaded = apiaryDao.getById("1").blockingGet();

        // Assert
        assertEquals("Updated Name", loaded.getName());
        assertEquals("Updated Location", loaded.getLocation());
    }

    @Test
    public void deleteApiary_removesFromDatabase() {
        // Arrange
        Apiary apiary = createTestApiary("1", "Test Apiary", "Test Location");
        apiaryDao.insert(apiary).blockingAwait();

        // Act
        apiaryDao.delete(apiary).blockingAwait();
        List<Apiary> apiaries = apiaryDao.getAll().blockingFirst();

        // Assert
        assertEquals(0, apiaries.size());
    }

    @Test
    public void getByLocation_filtersCorrectly() {
        // Arrange
        Apiary apiary1 = createTestApiary("1", "Apiary 1", "Prague");
        Apiary apiary2 = createTestApiary("2", "Apiary 2", "Bratislava");
        Apiary apiary3 = createTestApiary("3", "Apiary 3", "Prague");

        apiaryDao.insert(apiary1).blockingAwait();
        apiaryDao.insert(apiary2).blockingAwait();
        apiaryDao.insert(apiary3).blockingAwait();

        // Act
        List<Apiary> pragueApiaries = apiaryDao.getByLocation("Prague").blockingGet();

        // Assert
        assertEquals(2, pragueApiaries.size());
        assertTrue(pragueApiaries.stream().allMatch(a -> a.getLocation().equals("Prague")));
    }

    @Test
    public void getCount_returnsCorrectCount() {
        // Arrange
        apiaryDao.insert(createTestApiary("1", "Apiary 1", "Location 1")).blockingAwait();
        apiaryDao.insert(createTestApiary("2", "Apiary 2", "Location 2")).blockingAwait();
        apiaryDao.insert(createTestApiary("3", "Apiary 3", "Location 3")).blockingAwait();

        // Act
        int count = apiaryDao.getCount().blockingGet();

        // Assert
        assertEquals(3, count);
    }

    @Test
    public void deleteById_removesSpecificApiary() {
        // Arrange
        apiaryDao.insert(createTestApiary("1", "Apiary 1", "Location 1")).blockingAwait();
        apiaryDao.insert(createTestApiary("2", "Apiary 2", "Location 2")).blockingAwait();

        // Act
        apiaryDao.deleteById("1").blockingAwait();
        List<Apiary> remaining = apiaryDao.getAll().blockingFirst();

        // Assert
        assertEquals(1, remaining.size());
        assertEquals("2", remaining.get(0).getId());
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
