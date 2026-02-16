# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Multi-platform beekeeping management application with **55% shared codebase** between Desktop (JavaFX + JDBC) and Android (Room). The project uses MVVM architecture with RxJava2 for reactive programming.

**Key Facts:**
- **Bilingual application:** Full Slovak/English support with 531 translation keys
- **i18n Architecture:** TranslationManager + database-backed translations
- **Language switcher:** Users can switch between Slovak (default) and English
- **Primary audience:** Slovak beekeepers, with English for international users

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
    TranslationManager tm = TranslationManager.getInstance();

    // CORRECT: Wrap subscriptions in Platform.runLater()
    Platform.runLater(() -> {
        viewModel.getApiaries()
            .subscribe(apiaries -> table.setItems(FXCollections.observableList(apiaries)));

        viewModel.getLoading()
            .subscribe(loading -> statusLabel.setText(
                loading ? tm.get("status.loading") : ""
            ));

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
- 10 tables: apiaries, hives, inspections, feedings, taxations, taxation_frames, calendar_events, settings, inspection_recordings, **translations**
- CASCADE DELETE enabled for referential integrity
- translations table: 531 unique keys (1,062 SK+EN translations)

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
        // CRITICAL: Always use TranslationManager for UI strings
        TranslationManager tm = TranslationManager.getInstance();
        setTitle(existingApiary == null
            ? tm.get("dialog.title.add_apiary")
            : tm.get("dialog.title.edit_apiary"));

        // If dialog uses FXML, MUST pass ResourceBundle:
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dialog.fxml"));
        // loader.setResources(new I18nResourceBundle(tm));
        // Parent content = loader.load();

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
// Use translation key + asterisk for required fields
TranslationManager tm = TranslationManager.getInstance();
Label nameLabel = new Label(tm.get("label.name") + " *");  // Required field
```

Or in FXML:
```xml
<!-- Translation value already includes * if required -->
<Label text="%label.name_required"/>
```

## Internationalization (i18n) Rules - MANDATORY

**CRITICAL:** This application is fully bilingual (Slovak/English) with 531 translation keys stored in the database. ALL new UI strings MUST follow i18n patterns.

### 🚫 NEVER Do This (Hardcoded Strings)

```java
// ❌ WRONG - Hardcoded Slovak string
Button addButton = new Button("Pridať včelnicu");
alert.setTitle("Chyba");
label.setText("Názov:");
tooltip.setText("Koľko litrov krmiva");
```

```xml
<!-- ❌ WRONG - Hardcoded Slovak string in FXML -->
<Button text="Pridať včelnicu" onAction="#handleAdd"/>
<Label text="Názov:"/>
<TableColumn text="Lokalita"/>
```

### ✅ ALWAYS Do This (Translation Keys)

**Rule 1: FXML files MUST use %key syntax**

```xml
<!-- ✅ CORRECT - Translation key -->
<Button text="%button.add_apiary" onAction="#handleAdd"/>
<Label text="%label.name"/>
<TableColumn text="%label.location"/>
<Tooltip text="%tooltip.feed_amount"/>
```

**Rule 2: Java code MUST use TranslationManager**

```java
// ✅ CORRECT - Get translation from TranslationManager
TranslationManager tm = TranslationManager.getInstance();
Button addButton = new Button(tm.get("button.add_apiary"));
alert.setTitle(tm.get("error.title"));
label.setText(tm.get("label.name"));
tooltip.setText(tm.get("tooltip.feed_amount"));

// ✅ CORRECT - Formatted strings with parameters
alert.setHeaderText(tm.get("dialog.header.delete_apiary", apiary.getName()));
statusLabel.setText(tm.get("status.loaded_count", apiaries.size()));
```

**Rule 3: Dialog classes MUST pass ResourceBundle to FXMLLoader**

```java
// ✅ CORRECT - Pass ResourceBundle before loading FXML
public class MyDialog extends Dialog<Result> {
    public MyDialog() {
        TranslationManager tm = TranslationManager.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/my_dialog.fxml"));
        loader.setResources(new I18nResourceBundle(tm));  // CRITICAL!
        Parent content = loader.load();

        getDialogPane().setContent(content);
        setTitle(tm.get("dialog.my_dialog.title"));
    }
}
```

### 🔄 Workflow: Adding New UI Strings

**Step 1: Choose a translation key**

Follow hierarchical naming convention:
- Menu items: `menu.file`, `menu.edit`
- Buttons: `button.add`, `button.save`, `button.delete`
- Labels: `label.name`, `label.location`, `label.date`
- Table columns: `table.column.name`, `table.column.date`
- Dialog titles: `dialog.title.add_apiary`, `dialog.title.edit_hive`
- Tooltips: `tooltip.worker_offspring`, `tooltip.feed_amount`
- Error messages: `error.loading`, `error.validation.required`
- Success messages: `success.created`, `success.updated`
- Status messages: `status.loading`, `status.saved`
- Calculator-specific: `varroa.label.mortality`, `queen.std.start_cells`

**Step 2: Add translations to database**

Create SQL file `/tmp/new_translations.sql`:
```sql
-- Add Slovak and English translations
INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES
(lower(hex(randomblob(16))), 'button.export_data', 'sk', 'Exportovať dáta', 'button', datetime('now')),
(lower(hex(randomblob(16))), 'button.export_data', 'en', 'Export Data', 'button', datetime('now')),

(lower(hex(randomblob(16))), 'tooltip.export_data', 'sk', 'Exportuje všetky dáta do CSV súboru', 'tooltip', datetime('now')),
(lower(hex(randomblob(16))), 'tooltip.export_data', 'en', 'Exports all data to CSV file', 'tooltip', datetime('now'));
```

**Step 3: Insert translations**
```bash
sqlite3 ~/beekeeper-desktop.db < /tmp/new_translations.sql
```

**Step 4: Use translation key in code**

FXML:
```xml
<Button text="%button.export_data">
    <tooltip><Tooltip text="%tooltip.export_data"/></tooltip>
</Button>
```

Java:
```java
TranslationManager tm = TranslationManager.getInstance();
Button exportButton = new Button(tm.get("button.export_data"));
Tooltip tooltip = new Tooltip(tm.get("tooltip.export_data"));
exportButton.setTooltip(tooltip);
```

**Step 5: Verify no [key] placeholders**

Run application in both languages and check:
- No `[button.export_data]` visible on screen
- Text displays correctly in Slovak and English

**Step 6: Update SQL init script (MANDATORY)**

After adding new translations, ALWAYS export updated translations to keep SQL init script current:

```bash
# Export all translations to SQL file
sqlite3 ~/beekeeper-desktop.db << 'EOF' > desktop/src/main/resources/sql/07_translations_all.sql
.mode list
.separator ''
SELECT '-- Complete translations export (' || COUNT(*) || ' translations, ' || COUNT(DISTINCT key) || ' unique keys)' || char(10) ||
'-- Generated: ' || datetime('now') || char(10) ||
'-- Categories: app, button, label, table, dialog, calculator, varroa, queen, treatment, feed_type, event_type, validation, error, success, status' || char(10) || char(10) ||
'INSERT OR REPLACE INTO translations (id, key, language, value, category, createdAt) VALUES' || char(10)
FROM translations;

SELECT
  CASE
    WHEN rownum > 1 THEN ',' || char(10)
    ELSE ''
  END ||
  '(lower(hex(randomblob(16))), ''' || key || ''', ''' || language || ''', ''' || replace(value, '''', '''''') || ''', ''' || COALESCE(category, '') || ''', datetime(''now''))'
FROM (
  SELECT
    key, language, value, category,
    ROW_NUMBER() OVER (ORDER BY category, key, language) as rownum
  FROM translations
  ORDER BY category, key, language
);

SELECT ';' || char(10);
EOF
```

Or use the provided script:
```bash
# Automated export script (easier)
cd desktop/src/main/resources/sql/
./export_translations.sh
```

**Why this is critical:**
- New developers can initialize database with latest translations
- CI/CD pipelines get correct translations
- Database backup/restore works correctly
- Prevents "missing translation" bugs in fresh installations

**When to update:**
- ✅ After adding any new translation keys
- ✅ After fixing translation values
- ✅ Before committing UI changes
- ✅ Before creating pull requests

**Verify export worked:**
```bash
# Check file size increased
ls -lh desktop/src/main/resources/sql/07_translations_all.sql

# Check translation count in SQL file
grep -c "^(lower" desktop/src/main/resources/sql/07_translations_all.sql
# Should match: sqlite3 ~/beekeeper-desktop.db "SELECT COUNT(*) FROM translations;"
```

### 📝 Categories for Organization

Use these categories when adding translations:

| Category | Usage | Examples |
|----------|-------|----------|
| `menu` | Menu bar items | menu.file, menu.edit, menu.help |
| `button` | Button labels | button.add, button.save, button.delete |
| `label` | Form field labels | label.name, label.location, label.date |
| `table` | Table column headers | table.column.name, table.column.status |
| `dialog` | Dialog titles/content | dialog.title.add_apiary, dialog.header.confirm |
| `tooltip` | Tooltip text | tooltip.feed_amount, tooltip.worker_cycle |
| `error` | Error messages | error.loading, error.validation.required |
| `success` | Success messages | success.created, success.updated |
| `status` | Status bar messages | status.loading, status.saved |
| `validation` | Validation errors | validation.required, validation.invalid_number |
| `calculator` | Calculator UI | calculator.varroa.title, calculator.feed.result |
| `varroa` | Varroa calculator | varroa.label.mortality, varroa.tooltip.drone_offspring |
| `queen` | Queen rearing | queen.std.start_cells, queen.split.broodless_period |
| `treatment` | Treatment types | treatment.thymol, treatment.oxalic_acid |
| `feed_type` | Feed types | feed_type.sugar_syrup, feed_type.fondant |
| `event_type` | Calendar events | event_type.treatment, event_type.inspection |

### 🧪 Testing i18n Changes

After adding new translations:

1. **Build and run:**
   ```bash
   gradle desktop:run
   ```

2. **Test in Slovak (default):**
   - Launch app, verify text displays correctly

3. **Test in English:**
   - Click `Language → English (EN)`
   - Restart application
   - Verify all new text displays in English

4. **Check for missing keys:**
   - Look for `[button.my_key]` placeholders on screen
   - If found, translation key is missing from database

5. **Switch back to Slovak:**
   - Click `Language → Slovenčina (SK)`
   - Restart application

### 📚 Translation Database Schema

```sql
CREATE TABLE translations (
    id TEXT PRIMARY KEY NOT NULL,
    key TEXT NOT NULL,
    language TEXT NOT NULL,
    value TEXT NOT NULL,
    category TEXT,
    context TEXT,
    createdAt INTEGER,
    updatedAt INTEGER,
    UNIQUE(key, language)
);

-- Query translations
SELECT key, value FROM translations WHERE language = 'sk' AND category = 'button';

-- Count translations
SELECT COUNT(DISTINCT key) as total_keys FROM translations;
-- Result: 531 keys (as of 2025-02-16)
```

### ⚠️ Common Mistakes to Avoid

1. **❌ Hardcoding strings in Java:**
   ```java
   // WRONG
   alert.setTitle("Chyba pri načítaní");

   // CORRECT
   alert.setTitle(tm.get("error.loading_title"));
   ```

2. **❌ Forgetting ResourceBundle in dialogs:**
   ```java
   // WRONG - causes "No resource specified" error
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dialog.fxml"));
   Parent content = loader.load();

   // CORRECT
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dialog.fxml"));
   loader.setResources(new I18nResourceBundle(TranslationManager.getInstance()));
   Parent content = loader.load();
   ```

3. **❌ Using inconsistent key naming:**
   ```java
   // WRONG - inconsistent
   "buttonAdd", "add_button", "Button.Add"

   // CORRECT - hierarchical, consistent
   "button.add", "button.save", "button.delete"
   ```

4. **❌ Only adding Slovak translation:**
   ```sql
   -- WRONG - only SK
   INSERT INTO translations VALUES (..., 'button.new', 'sk', 'Nový', ...);

   -- CORRECT - both SK and EN
   INSERT INTO translations VALUES (..., 'button.new', 'sk', 'Nový', ...);
   INSERT INTO translations VALUES (..., 'button.new', 'en', 'New', ...);
   ```

5. **❌ Forgetting to update SQL init script:**
   ```bash
   # WRONG - add translations but don't export
   sqlite3 ~/beekeeper-desktop.db < /tmp/new_translations.sql
   git commit -m "Add new feature"  # ❌ Other devs won't have translations!

   # CORRECT - always export after adding translations
   sqlite3 ~/beekeeper-desktop.db < /tmp/new_translations.sql
   cd desktop/src/main/resources/sql/
   ./export_translations.sh  # ✅ Updates 07_translations_all.sql
   git add desktop/src/main/resources/sql/07_translations_all.sql
   git commit -m "Add new feature with translations"
   ```

   **Why this matters:**
   - Fresh database installations get latest translations
   - CI/CD builds have correct translations
   - Other developers don't see `[missing.key]` placeholders
   - Database backup/restore works correctly

### 📖 Reference Documentation

- **I18N_IMPLEMENTATION_PLAN.md** - Original implementation plan
- **I18N_IMPLEMENTATION_STATUS.md** - Complete status report (531 keys)
- **Translation database:** `~/beekeeper-desktop.db` (translations table)
- **TranslationManager:** `shared/i18n/TranslationManager.java`
- **I18nResourceBundle:** `desktop/i18n/I18nResourceBundle.java`
- **JdbcTranslationDao:** `desktop/dao/jdbc/JdbcTranslationDao.java`

## Current Implementation Status

**Implemented Features:**
- ✅ Apiaries (Včelnice) - CRUD operations
- ✅ Hives (Úle) - CRUD, active/inactive toggle
- ✅ Inspections (Prehliadky) - 23-field form with create/edit
- ✅ Feeding (Krmenie) - 9-field form with auto-calculations
- ✅ Taxation (Taxácie) - Master-detail with frames, apiary-based display
- ✅ Calendar (Kalendár) - 11-field events, global tab
- ✅ **Internationalization (i18n)** - Full Slovak/English bilingual support
  - 531 translation keys (1,062 SK+EN translations)
  - Language switcher in menu bar
  - All UI components fully translated (FXML + Java)
  - Calculators: Varroa, Queen Rearing, Feed Calculator
- ✅ 102 tests passing (updated for i18n)

**Key Recent Changes:**
- **2025-02-16:** Complete i18n implementation (531 keys, all UI bilingual)
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
- **I18N_IMPLEMENTATION_PLAN.md** - Original i18n implementation plan (9 phases)
- **I18N_IMPLEMENTATION_STATUS.md** - Complete i18n status report (531 keys, 100% done)

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
- `~/beekeeper-desktop.db` (SQLite database with 10 tables)

**Internationalization (i18n):**
- `shared/src/main/java/com/beekeeper/shared/i18n/TranslationManager.java` (singleton, O(1) lookups)
- `shared/src/main/java/com/beekeeper/shared/i18n/TranslationLoader.java` (interface)
- `desktop/src/main/java/com/beekeeper/desktop/dao/jdbc/JdbcTranslationDao.java` (JDBC impl)
- `desktop/src/main/java/com/beekeeper/desktop/i18n/I18nResourceBundle.java` (FXML bridge)
- `desktop/src/main/java/com/beekeeper/desktop/util/TranslationMigration.java` (population script)

**Shared ViewModels:**
- `shared/src/main/java/com/beekeeper/shared/viewmodel/` (7 ViewModels)

**Desktop Controllers:**
- `desktop/src/main/java/com/beekeeper/desktop/controller/` (8 controllers)

**Dialogs:**
- `desktop/src/main/java/com/beekeeper/desktop/dialog/` (8 dialogs)
