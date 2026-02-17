package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcTranslationDao;
import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioMenuItem;
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

    @FXML
    private Tab calculatorsTab;

    @FXML
    private RadioMenuItem langSlovak;

    @FXML
    private RadioMenuItem langEnglish;

    private ApiaryListController apiaryListController;
    private HiveListController hiveListController;
    private InspectionListController inspectionListController;
    private FeedingListController feedingListController;
    private TaxationListController taxationListController;
    private CalendarEventListController calendarEventListController;
    private CalculatorsController calculatorsController;

    @FXML
    public void initialize() {
        try {
            // Get TranslationManager for passing ResourceBundle to child loaders
            TranslationManager tm = TranslationManager.getInstance();
            I18nResourceBundle bundle = new I18nResourceBundle(tm);

            // Set correct language radio button based on current language
            String currentLang = tm.getCurrentLanguage();
            if ("en".equals(currentLang)) {
                langEnglish.setSelected(true);
            } else {
                langSlovak.setSelected(true);
            }

            // Load Apiaries view
            FXMLLoader apiaryLoader = new FXMLLoader(getClass().getResource("/view/apiary_list.fxml"));
            apiaryLoader.setResources(bundle);
            Parent apiaryView = apiaryLoader.load();
            apiaryListController = apiaryLoader.getController();

            // Load Hives view
            FXMLLoader hiveLoader = new FXMLLoader(getClass().getResource("/view/hive_list.fxml"));
            hiveLoader.setResources(bundle);
            Parent hiveView = hiveLoader.load();
            hiveListController = hiveLoader.getController();

            // Load Inspections view
            FXMLLoader inspectionLoader = new FXMLLoader(getClass().getResource("/view/inspection_list.fxml"));
            inspectionLoader.setResources(bundle);
            Parent inspectionView = inspectionLoader.load();
            inspectionListController = inspectionLoader.getController();

            // Load Feeding view
            FXMLLoader feedingLoader = new FXMLLoader(getClass().getResource("/view/feeding_list.fxml"));
            feedingLoader.setResources(bundle);
            Parent feedingView = feedingLoader.load();
            feedingListController = feedingLoader.getController();

            // Load Taxation view
            FXMLLoader taxationLoader = new FXMLLoader(getClass().getResource("/view/taxation_list.fxml"));
            taxationLoader.setResources(bundle);
            Parent taxationView = taxationLoader.load();
            taxationListController = taxationLoader.getController();

            // Load Calendar view
            FXMLLoader calendarLoader = new FXMLLoader(getClass().getResource("/view/calendar_list.fxml"));
            calendarLoader.setResources(bundle);
            Parent calendarView = calendarLoader.load();
            calendarEventListController = calendarLoader.getController();

            // Load Calculators view
            FXMLLoader calculatorsLoader = new FXMLLoader(getClass().getResource("/view/calculators.fxml"));
            calculatorsLoader.setResources(bundle);
            Parent calculatorsView = calculatorsLoader.load();
            calculatorsController = calculatorsLoader.getController();

            // Set content in next JavaFX pulse to avoid macOS NSTrackingRectTag issues
            javafx.application.Platform.runLater(() -> {
                apiariesTab.setContent(apiaryView);
                hivesTab.setContent(hiveView);
                inspectionsTab.setContent(inspectionView);
                feedingTab.setContent(feedingView);
                taxationTab.setContent(taxationView);
                calendarTab.setContent(calendarView);
                calculatorsTab.setContent(calculatorsView);

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

    /**
     * Handle Settings menu item.
     * Opens Settings dialog for date format configuration.
     *
     * Use case: User wants to change date format from dd.MM.yyyy to MM/dd/yyyy.
     */
    @FXML
    private void handleSettings() {
        com.beekeeper.desktop.dialog.SettingsDialog dialog =
            new com.beekeeper.desktop.dialog.SettingsDialog();
        dialog.showAndWait();
        // If dialog returns true, settings were changed and app will restart
    }

    /**
     * Handle Exit menu item.
     * Closes the application.
     *
     * Use case: User wants to exit application from menu.
     */
    @FXML
    private void handleExit() {
        Platform.exit();
    }

    /**
     * Handle language change from menu.
     * Saves user's preference and shows restart prompt.
     *
     * Use case: User selects Slovak or English from Language menu.
     */
    @FXML
    private void handleLanguageChange(ActionEvent event) {
        RadioMenuItem source = (RadioMenuItem) event.getSource();
        String newLanguage = source.getId().equals("langSlovak") ? "sk" : "en";

        // Save preference
        JdbcTranslationDao dao = new JdbcTranslationDao();
        dao.saveLanguage(newLanguage);

        // Show restart prompt
        TranslationManager tm = TranslationManager.getInstance();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tm.get("dialog.language_changed.title"));
        alert.setHeaderText(tm.get("dialog.language_changed.header"));
        alert.setContentText(tm.get("dialog.language_changed.content"));
        alert.showAndWait();

        // Exit application (user will restart manually)
        Platform.exit();
    }
}
