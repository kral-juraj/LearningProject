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

        // Configure primary stage
        primaryStage.setTitle("Beekeeper Desktop - Včelársky denník");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Handle window close
        primaryStage.setOnCloseRequest(event -> {
            DatabaseManager.close();
            System.out.println("Database closed");
        });

        primaryStage.show();
    }

    @Override
    public void stop() {
        // Cleanup on application exit
        DatabaseManager.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
