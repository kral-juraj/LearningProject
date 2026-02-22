package com.beekeeper.desktop;

import com.beekeeper.desktop.dao.jdbc.JdbcSettingsDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTranslationDao;
import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.desktop.util.DatabaseInitializer;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.util.DateFormatManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for Beekeeper Desktop Application.
 * Initializes database and launches JavaFX UI.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize SQLite database in distribution directory
        // Get the directory where the JAR/application is located
        String appDir = getApplicationDirectory();

        // Create 'data' subdirectory if it doesn't exist
        java.io.File dataDir = new java.io.File(appDir, "data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            System.out.println("Created data directory: " + dataDir.getAbsolutePath());
        }

        // Database will be in: <distribution>/data/beekeeper.db
        String dbPath = new java.io.File(dataDir, "beekeeper.db").getAbsolutePath();
        System.out.println("Initializing database at: " + dbPath);
        DatabaseManager.initialize(dbPath);

        // Load translations and test data on first run
        DatabaseInitializer.initializeIfNeeded();
        System.out.println(DatabaseInitializer.getInitializationStatus());

        // Initialize translation system
        TranslationManager tm = TranslationManager.getInstance();
        JdbcTranslationDao translationDao = new JdbcTranslationDao();

        // Load user's preferred language (or default to Slovak)
        String language = translationDao.getSavedLanguage();
        tm.loadTranslations(translationDao, language);
        System.out.println("Loaded translations for language: " + language);

        // Initialize date format system
        DateFormatManager dfm = DateFormatManager.getInstance();
        JdbcSettingsDao settingsDao = new JdbcSettingsDao();

        // Load user's preferred date formats (or defaults)
        String dateFormat = settingsDao.getDateFormat();
        String timeFormat = settingsDao.getTimeFormat();
        String dateTimeFormat = settingsDao.getDateTimeFormat();
        dfm.loadFormats(dateFormat, timeFormat, dateTimeFormat);
        System.out.println("Loaded date formats - Date: " + dateFormat +
                         ", Time: " + timeFormat + ", DateTime: " + dateTimeFormat);

        // Load main view with translations
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        loader.setResources(new I18nResourceBundle(tm));
        Parent root = loader.load();

        // Configure primary stage with explicit size to avoid macOS resize issues
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(tm.get("app.title"));

        // Set size AFTER scene is set to avoid NSTrackingRectTag issues
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Handle window close
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application closing...");
        });

        primaryStage.show();
    }

    @Override
    public void stop() {
        // Cleanup on application exit
        // Note: Individual connections are managed by try-with-resources in DAOs
        System.out.println("Application stopped");
    }

    /**
     * Get the directory where the application is installed.
     *
     * For distribution: Returns the parent directory of lib/ (where bin/, lib/, data/ are)
     * For development: Returns desktop/ module directory
     *
     * @return Application directory path
     */
    private String getApplicationDirectory() {
        try {
            // Get the location of the JAR file or class
            String jarPath = Main.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

            java.io.File jarFile = new java.io.File(jarPath);

            // If running from JAR in lib/ directory (distribution)
            if (jarFile.getName().endsWith(".jar") &&
                jarFile.getParentFile().getName().equals("lib")) {
                // Go up to distribution root: lib/ -> distribution/
                return jarFile.getParentFile().getParent();
            }

            // For development (running from classes/), use current directory
            return System.getProperty("user.dir");

        } catch (Exception e) {
            System.err.println("Warning: Could not determine application directory, using current directory");
            e.printStackTrace();
            return System.getProperty("user.dir");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
