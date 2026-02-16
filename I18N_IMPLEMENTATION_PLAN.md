# Internationalization (i18n) Implementation Plan

## Context

The Beekeeper Desktop application currently has **all 570+ UI strings hardcoded in Slovak** across FXML files, Java controllers, dialogs, ViewModels, and calculators. The user wants to:

1. **Extract all hardcoded strings to a database** for centralized management
2. **Support bilingual operation** (Slovak and English initially)
3. **Enable easy addition of more languages** in the future
4. **Add a language switcher** to the UI with Slovak as default
5. **Persist language preference** in settings

This change will make the application internationally accessible while maintaining the existing Slovak user base.

---

## Implementation Strategy

### Phase 1: Database Schema & Translation Storage

**Create translations table in DatabaseManager.java:**

```sql
CREATE TABLE IF NOT EXISTS translations (
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

CREATE INDEX IF NOT EXISTS idx_translations_key_language
    ON translations(key, language);
CREATE INDEX IF NOT EXISTS idx_translations_category
    ON translations(category);
```

**Extend settings table:**

```sql
-- Add language column to existing settings table
ALTER TABLE settings ADD COLUMN language TEXT DEFAULT 'sk';
```

**Key naming convention (hierarchical):**
- `menu.file` - Menu items
- `menu.help`
- `button.add` - Buttons
- `button.edit`
- `button.delete`
- `label.name` - Form labels
- `label.location`
- `error.loading` - Error messages
- `error.validation.required`
- `success.created` - Success messages
- `dialog.title.add_apiary` - Dialog titles
- `calculator.varroa.title` - Calculator-specific
- `treatment.thymol.name` - Treatment names

**Categories for organization:**
- `menu` - Main menu items
- `button` - Button labels
- `label` - Form field labels
- `error` - Error messages
- `success` - Success messages
- `dialog` - Dialog titles/content
- `table` - Table column headers
- `calculator` - Calculator-specific strings
- `treatment` - Treatment type descriptions
- `milestone` - Queen rearing milestones
- `validation` - Validation error messages

---

### Phase 2: Translation Manager (Core Service)

**Create shared/i18n/TranslationManager.java:**

```java
package com.beekeeper.shared.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Manages application translations loaded from database.
 * Thread-safe singleton providing fast O(1) translation lookups.
 *
 * Use case: All UI components query this manager for translated strings.
 */
public class TranslationManager {
    private static TranslationManager instance;
    private Map<String, String> translations;
    private String currentLanguage;
    private static final String DEFAULT_LANGUAGE = "sk";

    private TranslationManager() {
        translations = new HashMap<>();
        currentLanguage = DEFAULT_LANGUAGE;
    }

    public static synchronized TranslationManager getInstance() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }

    /**
     * Load translations from loader (JDBC, Room, etc.)
     */
    public void loadTranslations(TranslationLoader loader, String language) {
        this.currentLanguage = language;
        this.translations = loader.loadTranslations(language);
    }

    /**
     * Get translated string for key in current language.
     * Returns [key] if translation not found (makes missing translations obvious).
     */
    public String get(String key) {
        String value = translations.get(key);
        return value != null ? value : "[" + key + "]";
    }

    /**
     * Get formatted string with parameters (e.g., "Error: %s")
     */
    public String get(String key, Object... params) {
        String template = get(key);
        return String.format(template, params);
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public Locale getCurrentLocale() {
        return new Locale(currentLanguage);
    }
}
```

**Create shared/i18n/TranslationLoader.java (interface):**

```java
package com.beekeeper.shared.i18n;

import java.util.Map;

/**
 * Interface for loading translations from platform-specific storage.
 * Desktop uses JDBC, Android would use Room.
 */
public interface TranslationLoader {
    Map<String, String> loadTranslations(String language);
}
```

---

### Phase 3: Desktop-Specific Implementation

**Create desktop/dao/jdbc/JdbcTranslationDao.java:**

```java
package com.beekeeper.desktop.dao.jdbc;

import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.shared.i18n.TranslationLoader;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC implementation of TranslationLoader.
 * Loads all translations for given language from SQLite database.
 */
public class JdbcTranslationDao implements TranslationLoader {

    @Override
    public Map<String, String> loadTranslations(String language) {
        Map<String, String> translations = new HashMap<>();
        String sql = "SELECT key, value FROM translations WHERE language = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    translations.put(rs.getString("key"), rs.getString("value"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading translations: " + e.getMessage());
        }

        return translations;
    }

    /**
     * Get user's saved language preference from settings.
     */
    public String getSavedLanguage() {
        String sql = "SELECT language FROM settings LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("language");
            }
        } catch (SQLException e) {
            System.err.println("Error loading language setting: " + e.getMessage());
        }
        return "sk"; // Default
    }

    /**
     * Save user's language preference.
     */
    public void saveLanguage(String language) {
        String sql = "UPDATE settings SET language = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, language);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving language: " + e.getMessage());
        }
    }
}
```

**Create desktop/i18n/I18nResourceBundle.java (for FXML):**

```java
package com.beekeeper.desktop.i18n;

import com.beekeeper.shared.i18n.TranslationManager;
import java.util.*;

/**
 * Custom ResourceBundle that delegates to TranslationManager.
 * Enables JavaFX FXML files to use %key syntax for translations.
 *
 * Use case: Set this as ResourceBundle when loading FXML files.
 */
public class I18nResourceBundle extends ResourceBundle {
    private final TranslationManager manager;

    public I18nResourceBundle(TranslationManager manager) {
        this.manager = manager;
    }

    @Override
    protected Object handleGetObject(String key) {
        return manager.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        // Not needed for FXML usage
        return Collections.emptyEnumeration();
    }
}
```

---

### Phase 4: Main.java Integration

**Update Main.java to initialize translation system:**

```java
@Override
public void start(Stage primaryStage) throws Exception {
    // Initialize SQLite database
    String userHome = System.getProperty("user.home");
    String dbPath = userHome + "/beekeeper-desktop.db";
    System.out.println("Initializing database at: " + dbPath);
    DatabaseManager.initialize(dbPath);

    // Initialize translation system
    TranslationManager tm = TranslationManager.getInstance();
    JdbcTranslationDao translationDao = new JdbcTranslationDao();

    // Load user's preferred language (or default to Slovak)
    String language = translationDao.getSavedLanguage();
    tm.loadTranslations(translationDao, language);
    System.out.println("Loaded translations for language: " + language);

    // Load main view with translations
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
    loader.setResources(new I18nResourceBundle(tm));
    Parent root = loader.load();

    // ... rest of existing code
    primaryStage.setTitle(tm.get("app.title"));
}
```

---

### Phase 5: FXML Migration Pattern

**Before (hardcoded):**
```xml
<Button text="Pridať včelnicu" onAction="#handleAdd"/>
<Label text="Názov:"/>
<TableColumn text="Lokalita"/>
```

**After (translation keys):**
```xml
<Button text="%button.add_apiary" onAction="#handleAdd"/>
<Label text="%label.name"/>
<TableColumn text="%label.location"/>
```

**Migration order (13 files):**
1. main.fxml (menu, tabs, status bar)
2. apiary_list.fxml (simple, 5 buttons + 2 columns)
3. hive_list.fxml
4. inspection_list.fxml
5. feeding_list.fxml
6. taxation_list.fxml
7. calendar_list.fxml
8. calculators.fxml (largest, ~50 strings)
9. inspection_dialog.fxml (largest dialog)
10. feeding_dialog.fxml
11. taxation_dialog.fxml
12. taxation_frame_dialog.fxml
13. calendar_event_dialog.fxml

---

### Phase 6: Controller/Dialog Migration Pattern

**Before (hardcoded):**
```java
statusLabel.setText("Načítavam...");
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle("Zmazať včelnicu");
alert.setHeaderText("Zmazať včelnicu: " + apiary.getName());
```

**After (using TranslationManager):**
```java
TranslationManager tm = TranslationManager.getInstance();
statusLabel.setText(tm.get("status.loading"));
Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
alert.setTitle(tm.get("dialog.title.delete_apiary"));
alert.setHeaderText(tm.get("dialog.header.delete_apiary", apiary.getName()));
```

**Pattern for controllers:**
```java
public class ApiaryListController {
    private TranslationManager tm;

    @FXML
    public void initialize() {
        tm = TranslationManager.getInstance();

        // Use tm.get() for all dynamic strings
        Platform.runLater(() -> {
            viewModel.getApiaries().subscribe(apiaries -> {
                statusLabel.setText(apiaries.size() + tm.get("status.apiaries_count"));
            });

            viewModel.getError().subscribe(error -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(tm.get("error.title"));
                alert.setContentText(error);
                alert.showAndWait();
            });
        });
    }
}
```

---

### Phase 7: ViewModel Migration Pattern

**Current (emits Slovak strings):**
```java
error.accept("Chyba pri načítaní včelníc: " + throwable.getMessage());
success.accept("Včelnica úspešne vytvorená");
```

**After (emits translation keys + parameters):**

**Option 1: Emit keys, translate in controller**
```java
// ViewModel emits key
error.accept("error.loading_apiaries|" + throwable.getMessage());
success.accept("success.apiary_created");

// Controller translates
viewModel.getError().subscribe(errorMsg -> {
    String[] parts = errorMsg.split("\\|");
    String key = parts[0];
    String translated = parts.length > 1
        ? tm.get(key, parts[1])
        : tm.get(key);
    showErrorAlert(translated);
});
```

**Option 2: Keep ViewModels unchanged (recommended for Phase 1)**

ViewModels can continue emitting full messages. Controllers fetch translated equivalents:

```java
// ViewModel unchanged
error.accept("Chyba pri načítaní včelníc: " + throwable.getMessage());

// Controller maps to translation
viewModel.getError().subscribe(error -> {
    // For now, display as-is (all users are Slovak)
    // In future, map error types to translation keys
    showErrorAlert(error);
});
```

**This allows incremental migration without breaking existing functionality.**

---

### Phase 8: Language Switcher UI

**Add to main.fxml menu bar:**

```xml
<MenuBar>
    <Menu text="%menu.file">
        <MenuItem text="%menu.settings" onAction="#handleSettings"/>
        <SeparatorMenuItem/>
        <MenuItem text="%menu.exit" onAction="#handleExit"/>
    </Menu>

    <Menu text="%menu.language">
        <RadioMenuItem fx:id="langSlovak" text="Slovenčina (SK)"
                       onAction="#handleLanguageChange" selected="true">
            <toggleGroup><ToggleGroup fx:id="languageGroup"/></toggleGroup>
        </RadioMenuItem>
        <RadioMenuItem fx:id="langEnglish" text="English (EN)"
                       onAction="#handleLanguageChange">
            <toggleGroup><fx:reference source="languageGroup"/></toggleGroup>
        </RadioMenuItem>
    </Menu>

    <Menu text="%menu.help">
        <MenuItem text="%menu.about" onAction="#handleAbout"/>
    </Menu>
</MenuBar>
```

**Add to MainController (or create separate controller):**

```java
@FXML
private void handleLanguageChange(ActionEvent event) {
    RadioMenuItem source = (RadioMenuItem) event.getSource();
    String newLanguage = source.getId().equals("langSlovak") ? "sk" : "en";

    // Save preference
    JdbcTranslationDao dao = new JdbcTranslationDao();
    dao.saveLanguage(newLanguage);

    // Show restart prompt
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    TranslationManager tm = TranslationManager.getInstance();
    alert.setTitle(tm.get("dialog.language_changed.title"));
    alert.setHeaderText(tm.get("dialog.language_changed.header"));
    alert.setContentText(tm.get("dialog.language_changed.content"));
    alert.showAndWait();

    // Restart application (simplest approach)
    Platform.exit();
    // User will restart manually, or we could add auto-restart
}
```

---

### Phase 9: Translation Data Migration Script

**Create utility to populate database with all 570+ translations:**

**Create desktop/util/TranslationMigration.java:**

```java
package com.beekeeper.desktop.util;

import com.beekeeper.desktop.db.DatabaseManager;
import java.sql.*;
import java.util.UUID;

/**
 * One-time migration to populate translations table with all UI strings.
 * Run once during first deployment, or manually to reset translations.
 */
public class TranslationMigration {

    public static void migrate() throws SQLException {
        System.out.println("Starting translation migration...");

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            // Clear existing translations
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM translations");
            }

            // Insert all translations
            insertMenuTranslations(conn);
            insertButtonTranslations(conn);
            insertLabelTranslations(conn);
            insertErrorTranslations(conn);
            insertSuccessTranslations(conn);
            insertDialogTranslations(conn);
            insertTableTranslations(conn);
            insertCalculatorTranslations(conn);
            insertTreatmentTranslations(conn);
            insertMilestoneTranslations(conn);

            conn.commit();
            System.out.println("Translation migration completed successfully!");
        }
    }

    private static void insert(Connection conn, String key, String sk, String en,
                               String category) throws SQLException {
        String sql = "INSERT INTO translations (id, key, language, value, category, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        long now = System.currentTimeMillis();

        // Slovak
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, key);
            stmt.setString(3, "sk");
            stmt.setString(4, sk);
            stmt.setString(5, category);
            stmt.setLong(6, now);
            stmt.executeUpdate();
        }

        // English
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, key);
            stmt.setString(3, "en");
            stmt.setString(4, en);
            stmt.setString(5, category);
            stmt.setLong(6, now);
            stmt.executeUpdate();
        }
    }

    private static void insertMenuTranslations(Connection conn) throws SQLException {
        insert(conn, "menu.file", "Súbor", "File", "menu");
        insert(conn, "menu.settings", "Nastavenia", "Settings", "menu");
        insert(conn, "menu.exit", "Ukončiť", "Exit", "menu");
        insert(conn, "menu.help", "Nápoveda", "Help", "menu");
        insert(conn, "menu.about", "O aplikácii", "About", "menu");
        insert(conn, "menu.language", "Jazyk", "Language", "menu");
        // ... (continue for all menu items)
    }

    private static void insertButtonTranslations(Connection conn) throws SQLException {
        insert(conn, "button.add_apiary", "Pridať včelnicu", "Add Apiary", "button");
        insert(conn, "button.add_hive", "Pridať úľ", "Add Hive", "button");
        insert(conn, "button.edit", "Upraviť", "Edit", "button");
        insert(conn, "button.delete", "Zmazať", "Delete", "button");
        insert(conn, "button.save", "Uložiť", "Save", "button");
        insert(conn, "button.cancel", "Zrušiť", "Cancel", "button");
        insert(conn, "button.refresh", "Obnoviť", "Refresh", "button");
        insert(conn, "button.calculate", "Vypočítať", "Calculate", "button");
        insert(conn, "button.clear", "Vymazať", "Clear", "button");
        // ... (continue for all buttons)
    }

    // ... similar methods for all categories
}
```

---

## Implementation Timeline

### Week 1: Foundation (Database + Core)
- **Day 1-2:** Database schema (translations table, settings column), migration methods
- **Day 3:** TranslationManager, TranslationLoader, JdbcTranslationDao
- **Day 4:** I18nResourceBundle, Main.java integration, basic testing
- **Day 5:** TranslationMigration script, populate with first 100 strings

### Week 2: FXML Migration
- **Day 1:** main.fxml + language switcher UI
- **Day 2:** apiary_list.fxml, hive_list.fxml
- **Day 3:** inspection_list.fxml, feeding_list.fxml
- **Day 4:** taxation_list.fxml, calendar_list.fxml
- **Day 5:** calculators.fxml (largest, ~50 strings)

### Week 3: Dialogs
- **Day 1:** inspection_dialog.fxml (largest, 40+ fields)
- **Day 2:** feeding_dialog.fxml, taxation_dialog.fxml
- **Day 3:** taxation_frame_dialog.fxml, calendar_event_dialog.fxml
- **Day 4:** Varroa/Queen calculator dialogs
- **Day 5:** Testing all dialog flows

### Week 4: Controllers & ViewModels
- **Day 1-2:** Update all 8 controllers to use TranslationManager
- **Day 3:** Update all 8 dialogs
- **Day 4:** Review ViewModel strategy (keep as-is for now)
- **Day 5:** Integration testing, bug fixes

### Week 5: English Translation & Polish
- **Day 1-2:** Complete English translations for all 570+ strings
- **Day 3:** Review beekeeping terminology with domain expert
- **Day 4:** Full testing (SK and EN)
- **Day 5:** Documentation, user guide for language switching

**Total: 5 weeks for complete implementation**

---

## English Beekeeping Terminology Reference

| Slovak | English |
|--------|---------|
| Včelnica | Apiary |
| Úľ | Hive |
| Prehliadka | Inspection |
| Krmenie | Feeding |
| Taxácia | Frame Survey / Hive Assessment |
| Rámik | Frame |
| Plod | Brood |
| Zapečatený plod | Capped brood |
| Nezapečatený plod | Uncapped brood |
| Matka | Queen |
| Matečník | Queen cell |
| Varroa kliešť | Varroa mite |
| Zásoby | Food stores |
| Peľ | Pollen |
| Trúdi plod | Drone brood |
| Lietavky | Foragers |
| Klietkovanie | Queen caging |
| Opačnenec | Nucleus colony / Split |
| Zavíčkovanie | Capping |
