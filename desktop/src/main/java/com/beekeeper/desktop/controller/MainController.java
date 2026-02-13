package com.beekeeper.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * Main controller for the desktop application.
 * Manages tabs for different features: Apiaries, Hives, Inspections, etc.
 */
public class MainController {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab apiariesTab;

    @FXML
    private Tab hivesTab;

    @FXML
    private Tab inspectionsTab;

    private ApiaryListController apiaryListController;
    private HiveListController hiveListController;
    private InspectionListController inspectionListController;

    @FXML
    public void initialize() {
        try {
            // Load Apiaries view
            FXMLLoader apiaryLoader = new FXMLLoader(getClass().getResource("/view/apiary_list.fxml"));
            Parent apiaryView = apiaryLoader.load();
            apiaryListController = apiaryLoader.getController();

            // Load Hives view
            FXMLLoader hiveLoader = new FXMLLoader(getClass().getResource("/view/hive_list.fxml"));
            Parent hiveView = hiveLoader.load();
            hiveListController = hiveLoader.getController();

            // Load Inspections view
            FXMLLoader inspectionLoader = new FXMLLoader(getClass().getResource("/view/inspection_list.fxml"));
            Parent inspectionView = inspectionLoader.load();
            inspectionListController = inspectionLoader.getController();

            // Set content in next JavaFX pulse to avoid macOS NSTrackingRectTag issues
            javafx.application.Platform.runLater(() -> {
                apiariesTab.setContent(apiaryView);
                hivesTab.setContent(hiveView);
                inspectionsTab.setContent(inspectionView);

                // Set up communication between controllers
                setupControllerCommunication();

                System.out.println("MainController initialized successfully - all tabs loaded");
            });

        } catch (IOException e) {
            System.err.println("Error loading views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupControllerCommunication() {
        // When "Zobraziť úle" button is clicked
        apiaryListController.setOnApiarySelected(apiary -> {
            if (apiary != null) {
                hivesTab.setDisable(false);
                hiveListController.setApiaryId(apiary.getId());
                // Switch to Hives tab
                mainTabPane.getSelectionModel().select(hivesTab);
            }
        });

        // When "Zobraziť prehliadky" button is clicked
        hiveListController.setOnHiveSelected(hive -> {
            if (hive != null) {
                inspectionsTab.setDisable(false);
                inspectionListController.setHiveId(hive.getId());
                // Switch to Inspections tab
                mainTabPane.getSelectionModel().select(inspectionsTab);
            }
        });
    }
}
