package com.beekeeper.desktop;

import com.beekeeper.desktop.db.DatabaseManager;
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
        // Initialize SQLite database
        String userHome = System.getProperty("user.home");
        String dbPath = userHome + "/beekeeper-desktop.db";
        System.out.println("Initializing database at: " + dbPath);
        DatabaseManager.initialize(dbPath);

        // Load main view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        Parent root = loader.load();

        // Configure primary stage with explicit size to avoid macOS resize issues
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Beekeeper Desktop - Včelársky denník");

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

    public static void main(String[] args) {
        launch(args);
    }
}
