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

    @FXML
    private Tab feedingTab;

    @FXML
    private Tab taxationTab;

    @FXML
    private Tab calendarTab;

    private ApiaryListController apiaryListController;
    private HiveListController hiveListController;
    private InspectionListController inspectionListController;
    private FeedingListController feedingListController;
    private TaxationListController taxationListController;
    private CalendarEventListController calendarEventListController;

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

            // Load Feeding view
            FXMLLoader feedingLoader = new FXMLLoader(getClass().getResource("/view/feeding_list.fxml"));
            Parent feedingView = feedingLoader.load();
            feedingListController = feedingLoader.getController();

            // Load Taxation view
            FXMLLoader taxationLoader = new FXMLLoader(getClass().getResource("/view/taxation_list.fxml"));
            Parent taxationView = taxationLoader.load();
            taxationListController = taxationLoader.getController();

            // Load Calendar view
            FXMLLoader calendarLoader = new FXMLLoader(getClass().getResource("/view/calendar_list.fxml"));
            Parent calendarView = calendarLoader.load();
            calendarEventListController = calendarLoader.getController();

            // Set content in next JavaFX pulse to avoid macOS NSTrackingRectTag issues
            javafx.application.Platform.runLater(() -> {
                apiariesTab.setContent(apiaryView);
                hivesTab.setContent(hiveView);
                inspectionsTab.setContent(inspectionView);
                feedingTab.setContent(feedingView);
                taxationTab.setContent(taxationView);
                calendarTab.setContent(calendarView);

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
                taxationTab.setDisable(false);  // Enable taxation tab for selected apiary

                hiveListController.setApiaryId(apiary.getId());
                taxationListController.setApiaryId(apiary.getId());  // Load all taxations for apiary

                // Switch to Hives tab
                mainTabPane.getSelectionModel().select(hivesTab);
            }
        });

        // When "Zobraziť prehliadky" button is clicked
        hiveListController.setOnHiveSelected(hive -> {
            if (hive != null) {
                inspectionsTab.setDisable(false);
                feedingTab.setDisable(false);
                // Note: taxationTab is already enabled (based on apiary selection)

                inspectionListController.setHiveId(hive.getId());
                feedingListController.setHiveId(hive.getId());

                // Switch to Inspections tab
                mainTabPane.getSelectionModel().select(inspectionsTab);
            }
        });
    }
}
