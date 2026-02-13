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
    public void initialize() {
        try {
            // Load Apiaries view
            FXMLLoader apiaryLoader = new FXMLLoader(getClass().getResource("/view/apiary_list.fxml"));
            Parent apiaryView = apiaryLoader.load();
            apiariesTab.setContent(apiaryView);

            System.out.println("MainController initialized successfully");
        } catch (IOException e) {
            System.err.println("Error loading views: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
