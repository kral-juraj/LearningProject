# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Multi-platform beekeeping management application with **55% shared codebase** between Desktop (JavaFX + JDBC) and Android (Room). The project uses MVVM architecture with RxJava2 for reactive programming.

**Key Fact:** This is Slovak language application - all UI strings are in Slovak (slovenčina).

## Build & Run Commands

### Desktop Application
```bash
# Run desktop app (recommended)
gradle desktop:run

# Build distributable
gradle desktop:build

# Run tests (41 tests: utils + controllers + dialogs + integration)
gradle desktop:test

# Clean build
gradle clean desktop:build
```

### Multi-Module Build
```bash
# Build all modules
gradle build

# Build specific module
gradle shared:build
gradle desktop:build
gradle app:build  # Requires Android SDK

# Test specific module
gradle shared:test
gradle desktop:test
```

### Database Management
```bash
# Check desktop database
sqlite3 ~/beekeeper-desktop.db

# Show tables
sqlite3 ~/beekeeper-desktop.db ".tables"

# Query data
sqlite3 ~/beekeeper-desktop.db "SELECT * FROM apiaries;"

# Backup database
cp ~/beekeeper-desktop.db ~/beekeeper-backup.db
```

## Multi-Module Architecture

### Three-Module Structure
```
BeekeeperApp/
├── shared/         # Platform-agnostic (40% of codebase)
│   ├── entity/     # Pure POJOs - NO @Entity, @PrimaryKey annotations
│   ├── dao/        # DAO interfaces with RxJava2 return types
│   ├── repository/ # Business logic, validation, timestamp management
│   ├── viewmodel/  # BehaviorRelay (NOT LiveData!)
│   ├── scheduler/  # SchedulerProvider interface
│   └── util/       # Platform-agnostic utilities
│
├── desktop/        # JavaFX + JDBC (60% of desktop code)
│   ├── controller/ # JavaFX controllers
│   ├── dialog/     # Custom Dialog classes
│   ├── dao/jdbc/   # JDBC implementations of shared DAOs
│   ├── db/         # DatabaseManager with schema migrations
│   ├── util/       # Desktop-specific utilities
│   └── Main.java   # Entry point
│
└── app/            # Android (not yet reconverted to shared ViewModels)
    ├── data/local/ # Room database
    └── presentation/ # Android UI
```

**Dependency Flow:**
```
app/     ──┐
           ├──> shared/  (both depend on shared)
desktop/ ──┘
```

### What Goes Where?

| Component | Module | Why |
|-----------|--------|-----|
| Entities (POJOs) | `shared/` | No annotations, pure Java |
| DAO interfaces | `shared/` | Platform-agnostic RxJava2 contracts |
| Repositories | `shared/` | Business logic, validation, timestamps |
| ViewModels | `shared/` | Uses BehaviorRelay (NOT LiveData) |
| DAO implementations | `desktop/` or `app/` | JDBC vs Room |
| UI controllers | `desktop/` or `app/` | JavaFX vs Android |
| Schedulers | `desktop/` or `app/` | Platform-specific threading |

## Critical Architecture Patterns

### 1. Scheduler Abstraction (Cross-Platform Threading)

**Problem:** Desktop uses `JavaFxScheduler.platform()`, Android uses `AndroidSchedulers.mainThread()`

**Solution:** `SchedulerProvider` interface injected into ViewModels

```java
// shared/scheduler/SchedulerProvider.java
public interface SchedulerProvider {
    Scheduler io();
    Scheduler mainThread();
}

// desktop/scheduler/DesktopSchedulerProvider.java
public class DesktopSchedulerProvider implements SchedulerProvider {
    public Scheduler mainThread() {
        return JavaFxScheduler.platform();  // JavaFX UI thread
    }
    public Scheduler io() {
        return Schedulers.io();
    }
}
```

**ViewModels ALWAYS use injected scheduler:**
```java
public class ApiaryViewModel extends BaseViewModel {
    private final SchedulerProvider schedulerProvider;

    public void loadApiaries() {
        repository.getAllApiaries()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())  // NOT JavaFxScheduler.platform()!
            .subscribe(...);
    }
}
```

### 2. BehaviorRelay (NOT LiveData)

**Why:** LiveData is Android-specific. BehaviorRelay works on both platforms.

```java
// ViewModels use BehaviorRelay
private final BehaviorRelay<List<Apiary>> apiaries = BehaviorRelay.create();
private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
private final BehaviorRelay<String> error = BehaviorRelay.create();

public Observable<List<Apiary>> getApiaries() {
    return apiaries;  // Expose as Observable
}

// Controllers subscribe to Observable
viewModel.getApiaries()
    .observeOn(schedulerProvider.mainThread())
    .subscribe(apiaries -> updateUI(apiaries));
```

### 3. Platform.runLater() for macOS Stability

**Critical:** Desktop controllers MUST use `Platform.runLater()` when subscribing to ViewModels to avoid macOS NSTrackingRectTag crashes.

```java
@FXML
public void initialize() {
    // CORRECT: Wrap subscriptions in Platform.runLater()
    Platform.runLater(() -> {
        viewModel.getApiaries()
            .subscribe(apiaries -> table.setItems(FXCollections.observableList(apiaries)));

        viewModel.getLoading()
            .subscribe(loading -> statusLabel.setText(loading ? "Načítavam..." : ""));

        viewModel.getError()
            .subscribe(error -> showErrorAlert(error));

        viewModel.getSuccess()
            .subscribe(msg -> statusLabel.setText(msg));
    });

    // WRONG: Direct subscription crashes on macOS
    viewModel.getApiaries().subscribe(...);  // ❌ DON'T DO THIS
}
```

### 4. DAO Pattern (Interface → Implementation)

**Shared DAO Interface:**
```java
// shared/dao/ApiaryDao.java
public interface ApiaryDao {
    Completable insert(Apiary apiary);
    Flowable<List<Apiary>> getAll();
    Single<Apiary> getById(String id);
    Completable update(Apiary apiary);
    Completable delete(Apiary apiary);
}
```

**Desktop JDBC Implementation:**
```java
// desktop/dao/jdbc/JdbcApiaryDao.java
public class JdbcApiaryDao implements ApiaryDao {
    @Override
    public Flowable<List<Apiary>> getAll() {
        return Flowable.fromCallable(() -> {
            List<Apiary> list = new ArrayList<>();
            String sql = "SELECT * FROM apiaries ORDER BY name";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToApiary(rs));
                }
            }
            return list;
        });
    }
}
```

**Android Room Implementation (future):**
```java
@Dao
public interface RoomApiaryDao extends ApiaryDao {
    @Query("SELECT * FROM apiaries ORDER BY name")
    @Override
    Flowable<List<Apiary>> getAll();
}
```

### 5. Database Schema & Migrations

**Desktop:** `DatabaseManager.java` manages schema with manual migrations

```java
// Foreign keys MUST be enabled explicitly in SQLite
stmt.execute("PRAGMA foreign_keys = ON");

// Tables created in dependency order (parents before children)
CREATE TABLE apiaries (...);
CREATE TABLE hives (
    ...,
    FOREIGN KEY (apiaryId) REFERENCES apiaries(id) ON DELETE CASCADE
);

// Migrations: Add columns with ALTER TABLE
private static void migrateTaxations(Connection conn) {
    String[] columns = {"totalPollenDm", "totalCappedStoresDm", ...};
    for (String column : columns) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE taxations ADD COLUMN " + column + " INTEGER DEFAULT 0");
        } catch (SQLException e) {
            // Column already exists, ignore
        }
    }
}
```

**Key Facts:**
- Desktop DB location: `~/beekeeper-desktop.db`
- Android DB location: Internal app storage
- 9 tables: apiaries, hives, inspections, feedings, taxations, taxation_frames, calendar_events, settings, inspection_recordings
- CASCADE DELETE enabled for referential integrity

### 6. Repository Pattern (Business Logic Layer)

Repositories sit between ViewModels and DAOs, handling:
- Validation
- Timestamp management (createdAt, updatedAt)
- UUID generation
- Business rules

```java
public class ApiaryRepository {
    private final ApiaryDao dao;

    public Completable insertApiary(Apiary apiary) {
        // Generate ID if missing
        if (apiary.getId() == null || apiary.getId().isEmpty()) {
            apiary.setId(UUID.randomUUID().toString());
        }

        // Set timestamps
        long now = System.currentTimeMillis();
        if (apiary.getCreatedAt() == 0) {
            apiary.setCreatedAt(now);
        }
        apiary.setUpdatedAt(now);

        // Validation
        if (apiary.getName() == null || apiary.getName().trim().isEmpty()) {
            return Completable.error(new IllegalArgumentException("Názov je povinný"));
        }

        return dao.insert(apiary);
    }
}
```

### 7. ViewModel Subscription Pattern

ViewModels expose 4 standard observables:
```java
private final BehaviorRelay<List<T>> data = BehaviorRelay.create();
private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
private final BehaviorRelay<String> error = BehaviorRelay.create();
private final BehaviorRelay<String> success = BehaviorRelay.create();

public void loadData() {
    loading.accept(true);
    repository.getData()
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.mainThread())
        .subscribe(
            result -> {
                data.accept(result);
                loading.accept(false);
            },
            throwable -> {
                error.accept("Chyba: " + throwable.getMessage());
                loading.accept(false);
            }
        );
}
```

Controllers subscribe to all 4:
```java
Platform.runLater(() -> {
    viewModel.getData().subscribe(data -> tableView.setItems(...));
    viewModel.getLoading().subscribe(loading -> statusLabel.setText(...));
    viewModel.getError().subscribe(error -> showErrorAlert(error));
    viewModel.getSuccess().subscribe(msg -> statusLabel.setText(msg));
});
```

## Desktop UI Patterns

### Controller Initialization
```java
@FXML
public void initialize() {
    // 1. Initialize ViewModel with dependencies
    SchedulerProvider scheduler = new DesktopSchedulerProvider();
    ApiaryDao dao = new JdbcApiaryDao();
    ApiaryRepository repo = new ApiaryRepository(dao);
    viewModel = new ApiaryViewModel(repo, scheduler);

    // 2. Setup table columns with PropertyValueFactory
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

    // 3. Subscribe to ViewModel (wrap in Platform.runLater!)
    Platform.runLater(() -> {
        viewModel.getApiaries().subscribe(...);
    });

    // 4. Load initial data
    viewModel.loadApiaries();
}
```

### Dialog Pattern (Custom Dialogs)
```java
public class ApiaryDialog extends Dialog<Apiary> {
    public ApiaryDialog(Apiary existingApiary) {
        setTitle(existingApiary == null ? "Pridať včelnicu" : "Upraviť včelnicu");

        // Create form fields
        TextField nameField = new TextField();
        TextField locationField = new TextField();

        // Populate for edit mode
        if (existingApiary != null) {
            nameField.setText(existingApiary.getName());
            locationField.setText(existingApiary.getLocation());
        }

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                Apiary apiary = existingApiary != null ? existingApiary : new Apiary();
                apiary.setName(nameField.getText());
                apiary.setLocation(locationField.getText());
                return apiary;
            }
            return null;
        });
    }
}
```

### Master-Detail Forms (e.g., Taxation with Frames)
```java
// TaxationDialog manages local ObservableList<TaxationFrame>
private ObservableList<TaxationFrame> framesList = FXCollections.observableArrayList();

@FXML
private void handleAddFrame() {
    TaxationFrameDialog dialog = new TaxationFrameDialog(null);
    dialog.showAndWait().ifPresent(frame -> {
        frame.setId(UUID.randomUUID().toString());
        framesList.add(frame);
    });
}

// On save, pass both taxation and frames to ViewModel
setResultConverter(buttonType -> {
    if (buttonType == ButtonType.OK) {
        Taxation taxation = buildTaxation();
        viewModel.createTaxationWithFrames(taxation, new ArrayList<>(framesList));
    }
});
```

## Testing

### Test Structure
```
desktop/src/test/java/
├── util/                      # 3 utility tests
│   ├── DateTimeConverterTest.java
│   ├── ValidationHelperTest.java
│   └── EnumHelperTest.java
│
├── controller/                # 16 controller tests (4 × 4)
│   ├── ApiaryListControllerTest.java
│   ├── HiveListControllerTest.java
│   └── ...
│
├── dialog/                    # 8 dialog tests (4 × 2)
│   ├── InspectionDialogTest.java
│   ├── TaxationDialogTest.java
│   └── ...
│
└── integration/               # 8 integration tests (4 × 2)
    ├── CalendarIntegrationTest.java
    ├── FeedingIntegrationTest.java
    ├── InspectionIntegrationTest.java
    └── TaxationIntegrationTest.java
```

### Running Tests
```bash
# Run all desktop tests (41 tests)
gradle desktop:test

# Run specific test class
gradle desktop:test --tests DateTimeConverterTest

# Run with verbose output
gradle desktop:test --info
```

### Integration Tests Use In-Memory SQLite
```java
@BeforeEach
void setUp() {
    // Use in-memory database for isolation
    DatabaseManager.initialize(":memory:");
}
```

## Important Conventions

### Code Documentation (MANDATORY)
**EVERY method you create MUST have a JavaDoc comment explaining:**
- What the method does
- When/why it's used (use case)
- Parameters (if not obvious)
- Return value (if not obvious)

**Example:**
```java
/**
 * Calculates Varroa mite population projection over specified days.
 *
 * Use case: Beekeeper wants to predict mite growth and plan treatments.
 *
 * @param initialCount Starting number of phoretic mites
 * @param days Number of days to project forward
 * @param treatments List of planned treatments to apply
 * @return VarroaProjection with daily counts and recommendations
 */
public static VarroaProjection project(int initialCount, int days, List<VarroaTreatment> treatments) {
    // ...
}
```

**Test methods should have block comments explaining the use case:**
```java
/**
 * Test: Exponential growth with brood present
 *
 * Use case: Spring/summer projection when queen is laying.
 * Mites reproduce in brood cells, population grows exponentially.
 *
 * Expected result: Mite count increases significantly (2x+ per month).
 */
@Test
void testBasicGrowthWithBrood() {
    // ...
}
```

### Naming (Slovak UI Language)
- **Apiaries** = Včelnice
- **Hives** = Úle
- **Inspections** = Prehliadky
- **Feeding** = Krmenie
- **Taxation** = Taxácie
- **Calendar** = Kalendár

### Entity ID Generation
```java
// Always use UUID for new entities
if (entity.getId() == null || entity.getId().isEmpty()) {
    entity.setId(UUID.randomUUID().toString());
}
```

### Timestamp Management
```java
// Timestamps are milliseconds since epoch (long)
long now = System.currentTimeMillis();
entity.setCreatedAt(now);
entity.setUpdatedAt(now);
```

### Date/Time Input Pattern
Desktop uses DatePicker + separate hour/minute TextFields:
```java
DatePicker datePicker = new DatePicker();
TextField hourField = new TextField();
TextField minuteField = new TextField();

// Convert to timestamp
LocalDate date = datePicker.getValue();
int hour = Integer.parseInt(hourField.getText());
int minute = Integer.parseInt(minuteField.getText());
long timestamp = DateTimeConverter.toTimestamp(date, hour, minute);
```

### Required Field Markers
Forms show `*` for required fields:
```java
Label nameLabel = new Label("Názov *");  // Required field
```

## Current Implementation Status (Phase 3 Complete)

**Implemented Features:**
- ✅ Apiaries (Včelnice) - CRUD operations
- ✅ Hives (Úle) - CRUD, active/inactive toggle
- ✅ Inspections (Prehliadky) - 23-field form with create/edit
- ✅ Feeding (Krmenie) - 9-field form with auto-calculations
- ✅ Taxation (Taxácie) - Master-detail with frames, **apiary-based display**
- ✅ Calendar (Kalendár) - 11-field events, global tab
- ✅ 41 tests passing

**Key Recent Changes:**
- Taxation changed from hive-based to apiary-based display
- Added `hiveName` and `totalStarterFrames` columns to taxation table
- Context tracking in ViewModels for correct reload after CRUD operations

**Next Priorities (see DEVELOPMENT_PLAN.md):**
- Phase 4: Analytics & Charts (dashboard, graphs, reports)
- Phase 5: Calculators (Varroa, Queen rearing, Feed calculator)
- Phase 6: Settings dialog (backup, preferences)

## Documentation Files

- **README.md** - Project overview
- **DESKTOP_SETUP.md** - Desktop installation and usage guide
- **CONVERSION_GUIDE.md** - Multi-platform development guide (adding features)
- **PROJECT_STATE.md** - Current state, recent changes, critical context
- **TESTING.md** - Manual testing checklist for all features
- **DEVELOPMENT_PLAN.md** - Future roadmap (Phases 4-13)
- **EXCEL_MIGRATION.md** - One-time Excel → DB migration strategies

## Troubleshooting

### macOS Crashes on Startup
**Symptom:** `NSTrackingRectTag` errors or UI freezes
**Fix:** Ensure all ViewModel subscriptions are wrapped in `Platform.runLater()`

### Foreign Key Constraint Errors
**Symptom:** `FOREIGN KEY constraint failed`
**Fix:** Check `PRAGMA foreign_keys = ON` is set, and parent entities exist before inserting children

### Table Doesn't Reload After CRUD
**Symptom:** UI shows stale data after create/update/delete
**Fix:** Ensure ViewModel tracks context (apiaryId vs hiveId) and calls correct reload method

### Build Errors After Git Pull
```bash
gradle clean build
# If still failing, check for database schema changes in DatabaseManager.java
```

### Tests Failing
```bash
# Check database migrations
gradle desktop:test --info

# Verify in-memory SQLite setup in @BeforeEach
```

## Git Workflow

```bash
# Build artifacts are ignored (.gitignore includes build/, .gradle/, **/build/)
git status

# Commit with descriptive messages (user prefers manual push)
git add .
git commit -m "Add feature X with Y changes"

# User pushes manually
```

## Key Files Reference

**Entry Points:**
- `desktop/src/main/java/com/beekeeper/desktop/Main.java`
- `app/src/main/java/com/beekeeper/app/MainActivity.java`

**Database Schema:**
- `desktop/src/main/java/com/beekeeper/desktop/db/DatabaseManager.java`

**Shared ViewModels:**
- `shared/src/main/java/com/beekeeper/shared/viewmodel/` (6 ViewModels)

**Desktop Controllers:**
- `desktop/src/main/java/com/beekeeper/desktop/controller/` (6 controllers)

**Dialogs:**
- `desktop/src/main/java/com/beekeeper/desktop/dialog/` (7 dialogs)
