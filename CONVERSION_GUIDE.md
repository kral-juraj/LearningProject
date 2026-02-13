# Multi-Platform Conversion Guide

Complete guide for working with the Beekeeper multi-platform codebase, including Android ↔ Desktop conversion workflows.

## 📋 Table of Contents
- [Architecture Overview](#architecture-overview)
- [Code Reuse Strategy](#code-reuse-strategy)
- [Adding New Features](#adding-new-features)
- [Desktop → Android Reconversion](#desktop--android-reconversion)
- [Platform-Specific Patterns](#platform-specific-patterns)
- [Testing Strategy](#testing-strategy)
- [Best Practices](#best-practices)

---

## 🏗️ Architecture Overview

### Three-Module Structure

```
BeekeeperApp/
├── shared/         # Platform-agnostic code (55% reuse)
├── desktop/        # JavaFX + JDBC (25%)
└── app/            # Android + Room (20%)
```

### Dependency Flow

```
app/ ──depends on──> shared/
                       ↑
desktop/ ──depends on──┘
```

### What Goes Where?

| Component | Module | Reason |
|-----------|--------|--------|
| Entities (POJOs) | `shared/` | No platform dependencies |
| DAO Interfaces | `shared/` | Platform-agnostic contracts |
| Repositories | `shared/` | Pure business logic |
| ViewModels | `shared/` | BehaviorRelay instead of LiveData |
| Utils | `shared/` | No platform dependencies |
| DAO Implementations | `app/` or `desktop/` | Platform-specific (Room vs JDBC) |
| UI Controllers | `app/` or `desktop/` | Platform-specific frameworks |
| Schedulers | `app/` or `desktop/` | Platform-specific threading |

---

## 🔄 Code Reuse Strategy

### Shared Layer (55% of codebase)

**What's Shared:**
```java
// Entities - Pure POJOs
public class Apiary implements Serializable {
    private String id;
    private String name;
    // No @Entity, @PrimaryKey, @NonNull
}

// DAO Interfaces - Pure interfaces
public interface ApiaryDao {
    Completable insert(Apiary apiary);
    Flowable<List<Apiary>> getAll();
}

// Repositories - Business logic
public class ApiaryRepository {
    private final ApiaryDao dao;
    public Completable insertApiary(Apiary apiary) {
        // Validation, timestamps, business rules
    }
}

// ViewModels - BehaviorRelay (not LiveData!)
public class ApiaryViewModel extends BaseViewModel {
    private final BehaviorRelay<List<Apiary>> apiaries;

    public ApiaryViewModel(
        ApiaryRepository repository,
        SchedulerProvider schedulerProvider  // Injected!
    ) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }
}
```

### Platform-Specific Layer

**Android (Room):**
```java
@Dao
public interface RoomApiaryDao extends ApiaryDao {
    @Query("SELECT * FROM apiaries")
    @Override
    Flowable<List<Apiary>> getAll();
}

@Entity(tableName = "apiaries")
public class ApiaryEntity extends Apiary {
    // Add Room annotations
}
```

**Desktop (JDBC):**
```java
public class JdbcApiaryDao implements ApiaryDao {
    @Override
    public Flowable<List<Apiary>> getAll() {
        return Flowable.fromCallable(() -> {
            // JDBC query implementation
        });
    }
}
```

---

## ➕ Adding New Features

### Step-by-Step Workflow

#### 1. Add Entity to Shared Module

```java
// shared/src/main/java/com/beekeeper/shared/entity/NewEntity.java
package com.beekeeper.shared.entity;

import java.io.Serializable;

public class NewEntity implements Serializable {
    private String id;
    private String name;
    private long createdAt;

    // Getters and setters
}
```

#### 2. Add DAO Interface to Shared Module

```java
// shared/src/main/java/com/beekeeper/shared/dao/NewEntityDao.java
package com.beekeeper.shared.dao;

import com.beekeeper.shared.entity.NewEntity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.util.List;

public interface NewEntityDao {
    Completable insert(NewEntity entity);
    Completable update(NewEntity entity);
    Completable delete(NewEntity entity);
    Single<NewEntity> getById(String id);
    Flowable<List<NewEntity>> getAll();
}
```

#### 3. Add Repository to Shared Module

```java
// shared/src/main/java/com/beekeeper/shared/repository/NewEntityRepository.java
package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.NewEntityDao;
import com.beekeeper.shared.entity.NewEntity;
import com.beekeeper.shared.util.DateUtils;
import java.util.UUID;

public class NewEntityRepository {
    private final NewEntityDao dao;

    public NewEntityRepository(NewEntityDao dao) {
        this.dao = dao;
    }

    public Completable insertEntity(NewEntity entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setCreatedAt(DateUtils.getCurrentTimestamp());
        return dao.insert(entity);
    }
}
```

#### 4. Add ViewModel to Shared Module

```java
// shared/src/main/java/com/beekeeper/shared/viewmodel/NewEntityViewModel.java
package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.NewEntity;
import com.beekeeper.shared.repository.NewEntityRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.jakewharton.rxrelay2.BehaviorRelay;
import io.reactivex.Observable;
import java.util.List;

public class NewEntityViewModel extends BaseViewModel {
    private final NewEntityRepository repository;
    private final SchedulerProvider schedulerProvider;
    private final BehaviorRelay<List<NewEntity>> entities = BehaviorRelay.create();

    public NewEntityViewModel(
        NewEntityRepository repository,
        SchedulerProvider schedulerProvider
    ) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<List<NewEntity>> getEntities() {
        return entities;
    }

    public void loadEntities() {
        addDisposable(
            repository.getAllEntities()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(entities::accept)
        );
    }
}
```

#### 5. Implement JDBC DAO (Desktop)

```java
// desktop/src/main/java/com/beekeeper/desktop/dao/jdbc/JdbcNewEntityDao.java
package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.dao.NewEntityDao;
import com.beekeeper.shared.entity.NewEntity;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcNewEntityDao implements NewEntityDao {

    @Override
    public Completable insert(NewEntity entity) {
        return Completable.fromAction(() -> {
            String sql = "INSERT INTO new_entities (id, name, createdAt) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, entity.getId());
                stmt.setString(2, entity.getName());
                stmt.setLong(3, entity.getCreatedAt());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public Flowable<List<NewEntity>> getAll() {
        return Flowable.fromCallable(() -> {
            List<NewEntity> list = new ArrayList<>();
            String sql = "SELECT * FROM new_entities";
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    NewEntity entity = new NewEntity();
                    entity.setId(rs.getString("id"));
                    entity.setName(rs.getString("name"));
                    entity.setCreatedAt(rs.getLong("createdAt"));
                    list.add(entity);
                }
            }
            return list;
        });
    }
}
```

#### 6. Update DatabaseManager (Desktop)

```java
// Add to DatabaseManager.createTables()
stmt.execute(
    "CREATE TABLE IF NOT EXISTS new_entities (" +
    "id TEXT PRIMARY KEY NOT NULL, " +
    "name TEXT, " +
    "createdAt INTEGER)"
);
```

#### 7. Create JavaFX Controller (Desktop)

```java
// desktop/src/main/java/com/beekeeper/desktop/controller/NewEntityController.java
package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcNewEntityDao;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.NewEntity;
import com.beekeeper.shared.repository.NewEntityRepository;
import com.beekeeper.shared.viewmodel.NewEntityViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class NewEntityController {
    @FXML
    private TableView<NewEntity> entityTable;

    private NewEntityViewModel viewModel;

    @FXML
    public void initialize() {
        JdbcNewEntityDao dao = new JdbcNewEntityDao();
        NewEntityRepository repository = new NewEntityRepository(dao);
        DesktopSchedulerProvider scheduler = new DesktopSchedulerProvider();
        viewModel = new NewEntityViewModel(repository, scheduler);

        // Subscribe to ViewModel
        viewModel.getEntities()
            .subscribe(entities -> {
                // Update UI
            });

        viewModel.loadEntities();
    }
}
```

#### 8. Create FXML View (Desktop)

```xml
<!-- desktop/src/main/resources/view/new_entity_list.fxml -->
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<BorderPane xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.beekeeper.desktop.controller.NewEntityController">
    <center>
        <TableView fx:id="entityTable">
            <!-- Define columns -->
        </TableView>
    </center>
</BorderPane>
```

#### 9. Implement Room DAO (Android)

```java
// app/src/main/java/com/beekeeper/app/data/local/dao/RoomNewEntityDao.java
package com.beekeeper.app.data.local.dao;

import androidx.room.*;
import com.beekeeper.shared.dao.NewEntityDao;
import com.beekeeper.shared.entity.NewEntity;

@Dao
public interface RoomNewEntityDao extends NewEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Override
    Completable insert(NewEntity entity);

    @Query("SELECT * FROM new_entities")
    @Override
    Flowable<List<NewEntity>> getAll();
}
```

#### 10. Update Room Database (Android)

```java
// Add to AppDatabase.java
@Entity(tableName = "new_entities")
public class NewEntity extends com.beekeeper.shared.entity.NewEntity {
    @PrimaryKey
    @NonNull
    @Override
    public String getId() {
        return super.getId();
    }
}

// Add DAO to database
@Database(entities = {NewEntity.class, ...}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewEntityDao newEntityDao();
}
```

---

## 🔄 Desktop → Android Reconversion

### When to Reconvert

Convert back to Android when:
- Desktop version is stable and debugged
- Business logic is tested and working
- Ready to deploy mobile version
- Want to add mobile-specific features (GPS, camera, etc.)

### Reconversion Steps

#### Step 1: Verify Shared Code Works

```bash
# Ensure shared module builds cleanly
gradle shared:build

# Run shared module tests
gradle shared:test
```

#### Step 2: Create Android DAO Implementations

For each JDBC DAO, create a Room equivalent:

```java
// Pattern: JdbcXxxDao → RoomXxxDao

// Desktop (JDBC)
public class JdbcApiaryDao implements ApiaryDao {
    // JDBC implementation
}

// Android (Room)
@Dao
public interface RoomApiaryDao extends ApiaryDao {
    @Query("SELECT * FROM apiaries")
    @Override
    Flowable<List<Apiary>> getAll();
}
```

#### Step 3: Create Room Entities

Extend shared entities with Room annotations:

```java
// shared/entity/Apiary.java (no annotations)
public class Apiary implements Serializable {
    private String id;
    private String name;
}

// app/data/local/entity/ApiaryEntity.java
@Entity(tableName = "apiaries")
public class ApiaryEntity extends Apiary {
    @PrimaryKey
    @NonNull
    @Override
    public String getId() {
        return super.getId();
    }
}
```

#### Step 4: Create Android Scheduler Provider

```java
// app/src/main/java/com/beekeeper/app/scheduler/AndroidSchedulerProvider.java
package com.beekeeper.app.scheduler;

import com.beekeeper.shared.scheduler.SchedulerProvider;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AndroidSchedulerProvider implements SchedulerProvider {
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }
}
```

#### Step 5: Adapt ViewModels for Android

Option A: Use shared ViewModels directly with adapter:

```java
public class ApiaryAndroidViewModel extends AndroidViewModel {
    private final ApiaryViewModel sharedViewModel;

    public ApiaryAndroidViewModel(Application app) {
        super(app);
        ApiaryDao dao = AppDatabase.getInstance(app).apiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);
        SchedulerProvider scheduler = new AndroidSchedulerProvider();
        sharedViewModel = new ApiaryViewModel(repository, scheduler);
    }

    // Expose LiveData wrapper
    public LiveData<List<Apiary>> getApiaries() {
        MutableLiveData<List<Apiary>> liveData = new MutableLiveData<>();
        sharedViewModel.getApiaries()
            .subscribe(liveData::postValue);
        return liveData;
    }
}
```

Option B: Convert BehaviorRelay → LiveData:

```java
// In Fragment/Activity
viewModel.getApiaries()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(apiaries -> {
        // Update UI
    });
```

#### Step 6: Create Android UI

```xml
<!-- res/layout/fragment_apiary_list.xml -->
<LinearLayout>
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

```java
// ApiaryListFragment.java
public class ApiaryListFragment extends Fragment {
    private ApiaryViewModel viewModel;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ApiaryDao dao = AppDatabase.getInstance(requireContext()).apiaryDao();
        ApiaryRepository repository = new ApiaryRepository(dao);
        SchedulerProvider scheduler = new AndroidSchedulerProvider();
        viewModel = new ApiaryViewModel(repository, scheduler);

        viewModel.getApiaries()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateUI);
    }
}
```

#### Step 7: Test on Android

```bash
# Open in Android Studio
# Sync Gradle
# Run on emulator/device
```

### Migration Checklist

- [ ] All shared code compiles in `shared/` module
- [ ] All JDBC DAOs converted to Room DAOs
- [ ] Android SchedulerProvider implemented
- [ ] ViewModels accessible from Android
- [ ] UI fragments/activities created
- [ ] Database migrations handled (if version changed)
- [ ] Tests pass on Android
- [ ] Manual testing complete

---

## 🎯 Platform-Specific Patterns

### Schedulers

**Shared (Interface):**
```java
public interface SchedulerProvider {
    Scheduler io();
    Scheduler mainThread();
}
```

**Desktop (JavaFX):**
```java
public class DesktopSchedulerProvider implements SchedulerProvider {
    public Scheduler mainThread() {
        return JavaFxScheduler.platform();
    }
}
```

**Android:**
```java
public class AndroidSchedulerProvider implements SchedulerProvider {
    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }
}
```

### Reactive State Management

**Shared (BehaviorRelay):**
```java
private final BehaviorRelay<List<Apiary>> apiaries = BehaviorRelay.create();

public Observable<List<Apiary>> getApiaries() {
    return apiaries;
}

public void updateApiaries(List<Apiary> list) {
    apiaries.accept(list);
}
```

**Desktop (Direct subscribe):**
```java
viewModel.getApiaries()
    .observeOn(JavaFxScheduler.platform())
    .subscribe(this::updateTable);
```

**Android (LiveData wrapper or direct):**
```java
// Option 1: Direct
viewModel.getApiaries()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(this::updateRecyclerView);

// Option 2: LiveData wrapper
LiveData<List<Apiary>> liveData = toLiveData(viewModel.getApiaries());
liveData.observe(this, this::updateRecyclerView);
```

### Database Queries

**Shared (DAO interface):**
```java
public interface ApiaryDao {
    Single<Apiary> getById(String id);
}
```

**Desktop (JDBC):**
```java
public Single<Apiary> getById(String id) {
    return Single.fromCallable(() -> {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM apiaries WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return mapResultSet(rs);
        }
    });
}
```

**Android (Room):**
```java
@Query("SELECT * FROM apiaries WHERE id = :id")
Single<Apiary> getById(String id);
```

---

## ✅ Testing Strategy

### Shared Module Tests

```java
// shared/src/test/java/com/beekeeper/shared/repository/ApiaryRepositoryTest.java
@Test
public void testInsertApiary() {
    ApiaryDao mockDao = mock(ApiaryDao.class);
    when(mockDao.insert(any())).thenReturn(Completable.complete());

    ApiaryRepository repository = new ApiaryRepository(mockDao);
    Apiary apiary = new Apiary();
    apiary.setName("Test");

    repository.insertApiary(apiary)
        .test()
        .assertComplete();

    verify(mockDao).insert(any());
}
```

### Platform-Specific Tests

**Desktop:**
```java
// Use in-memory SQLite
@Before
public void setup() {
    DatabaseManager.initialize(":memory:");
}

@Test
public void testJdbcDao() {
    JdbcApiaryDao dao = new JdbcApiaryDao();
    Apiary apiary = new Apiary();
    apiary.setId("test-1");
    apiary.setName("Test Apiary");

    dao.insert(apiary).blockingAwait();

    List<Apiary> result = dao.getAll().blockingFirst();
    assertEquals(1, result.size());
}
```

**Android:**
```java
// Use Room in-memory database
@RunWith(AndroidJUnit4.class)
public class RoomApiaryDaoTest {
    private AppDatabase db;

    @Before
    public void setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase.class
        ).build();
    }

    @Test
    public void testRoomDao() {
        // Test Room implementation
    }
}
```

---

## 📝 Best Practices

### 1. Keep Shared Module Clean

**DO:**
- Use pure Java 8 (no platform APIs)
- Keep entities as simple POJOs
- Use RxJava for async operations
- Inject dependencies via constructor

**DON'T:**
- Import Android classes in `shared/`
- Import JavaFX classes in `shared/`
- Hardcode platform-specific paths
- Use static singletons

### 2. Consistent Naming

```
Entity: Apiary.java
DAO Interface: ApiaryDao.java
DAO Android: RoomApiaryDao.java
DAO Desktop: JdbcApiaryDao.java
Repository: ApiaryRepository.java
ViewModel: ApiaryViewModel.java
Controller Desktop: ApiaryListController.java
Fragment Android: ApiaryListFragment.java
```

### 3. Version Control

```
.gitignore should include:
- *.db (database files)
- build/
- .gradle/
- .idea/
- local.properties
```

### 4. Database Schema Sync

Keep schemas in sync:
1. Maintain a master SQL schema file
2. Generate Room migrations for Android
3. Update DatabaseManager DDL for Desktop
4. Document all schema changes

### 5. Documentation

Document platform differences:
```java
/**
 * Saves apiary to database.
 *
 * @implNote Android: Uses Room @Insert
 * @implNote Desktop: Uses JDBC PreparedStatement
 */
Completable insert(Apiary apiary);
```

---

## 🚀 Future Enhancements

### Kotlin Multiplatform (KMP)

Consider migrating to KMP for even better code sharing:
- Shared code: Entities, ViewModels, Repositories
- expect/actual for platform-specific code
- Compose Multiplatform for shared UI

### Cross-Platform Data Sync

Implement sync between platforms:
1. Add `syncStatus` field to entities
2. Implement conflict resolution
3. Use backend API for sync
4. Handle offline mode

### CI/CD Pipeline

```yaml
# .github/workflows/build.yml
name: Build All Platforms

on: [push]

jobs:
  shared:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Test Shared Module
        run: gradle shared:test

  desktop:
    needs: shared
    runs-on: ubuntu-latest
    steps:
      - name: Build Desktop
        run: gradle desktop:build

  android:
    needs: shared
    runs-on: ubuntu-latest
    steps:
      - name: Build Android
        run: gradle app:assembleDebug
```

---

**Last Updated:** February 2025
**Version:** 2.0.0 (Multi-Platform Edition)
